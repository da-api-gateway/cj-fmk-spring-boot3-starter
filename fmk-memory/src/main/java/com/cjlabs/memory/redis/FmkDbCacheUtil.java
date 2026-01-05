package com.cjlabs.memory.redis;

import com.cjlabs.core.time.FmkInstantUtil;
import com.cjlabs.web.json.FmkJacksonUtil;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 基于 ZSet 的多版本数据库缓存工具类
 * <p>
 * 特点：
 * 1. 自动按时间排序
 * 2. 自动清理旧版本
 * 3. 支持时间范围查询
 * 4. 使用简单，性能高效
 * 5. 直接存储数据，无需包装类
 * <p>
 * 使用场景：
 * - 数据库数据的多版本缓存
 * - 需要保留历史变更记录
 * - 需要回滚到历史版本
 */
@Slf4j
@Component
public class FmkDbCacheUtil {

    @Autowired
    private FmkRedisUtil redisUtil;

    @Autowired
    private FmkRedisProperties redisProperties;

    private static final int DEFAULT_MAX_VERSIONS = 10;

    private static final String KEY_PREFIX = "db:versioned:";

    private final ObjectMapper objectMapper = FmkJacksonUtil.getMapper();

    /**
     * 添加新版本（自动清理旧版本）
     *
     * @param entityType 实体类型（如：user, order）
     * @param entityId   实体ID
     * @param data       数据对象
     * @return 版本时间戳
     */
    public <T> Instant addVersion(String entityType, String entityId, T data) {
        return addVersion(entityType, entityId, data, DEFAULT_MAX_VERSIONS);
    }

    /**
     * 添加新版本（指定保留版本数）
     *
     * @param entityType  实体类型
     * @param entityId    实体ID
     * @param data        数据对象
     * @param maxVersions 最大保留版本数
     * @return 版本时间戳
     */
    public <T> Instant addVersion(String entityType, String entityId, T data, int maxVersions) {
        String key = buildKey(entityType, entityId);

        Instant now = FmkInstantUtil.now();
        long currentFullNanos = FmkInstantUtil.toFullNanos(now);

        // 使用 FmkJacksonUtil 序列化数据
        String jsonData = FmkJacksonUtil.toJson(data);
        if (jsonData == null) {
            log.error("序列化数据失败: key={}", key);
            throw new RuntimeException("添加版本缓存失败：数据序列化失败");
        }

        // 使用 RedisUtil 添加到 ZSet（时间戳作为分数）
        boolean success = redisUtil.zAdd(key, jsonData, currentFullNanos);
        if (!success) {
            log.warn("添加版本可能失败（数据已存在）: key={}, timestamp={}", key, currentFullNanos);
        }

        // 自动清理旧版本
        autoCleanOldVersions(key, maxVersions);

        log.debug("添加版本缓存: key={}, timestamp={}", key, currentFullNanos);

        return now;
    }

    /**
     * 获取最新版本
     *
     * @param entityType 实体类型
     * @param entityId   实体ID
     * @param clazz      数据类型
     * @return 最新版本的数据
     */
    public <T> T getLatest(String entityType, String entityId, Class<T> clazz) {
        String key = buildKey(entityType, entityId);

        // 使用 RedisUtil 获取分数最高的一个（最新的）
        Set<Object> result = redisUtil.getRedisTemplate()
                .opsForZSet()
                .reverseRange(buildFullKey(key), 0, 0);

        if (result == null || result.isEmpty()) {
            log.debug("缓存未命中: key={}", key);
            return null;
        }

        return parseData(result.iterator().next(), clazz);
    }

    /**
     * 获取版本数量
     *
     * @param entityType 实体类型
     * @param entityId   实体ID
     * @return 版本数量
     */
    public long getVersionCount(String entityType, String entityId) {
        String key = buildKey(entityType, entityId);
        Long count = redisUtil.getRedisTemplate()
                .opsForZSet()
                .zCard(buildFullKey(key));
        return count != null ? count : 0L;
    }

    /**
     * 清理旧版本（只保留最新的 N 个）
     *
     * @param entityType 实体类型
     * @param entityId   实体ID
     * @param keepCount  保留数量
     * @return 删除的数量
     */
    public long cleanOldVersions(String entityType, String entityId, int keepCount) {
        String key = buildKey(entityType, entityId);
        long count = getVersionCount(entityType, entityId);

        if (count <= keepCount) {
            return 0L;
        }

        // 删除最旧的版本
        Long removed = redisUtil.getRedisTemplate()
                .opsForZSet()
                .removeRange(buildFullKey(key), 0, count - keepCount - 1);

        log.info("清理旧版本: key={}, removed={}", key, removed);
        return removed != null ? removed : 0L;
    }

    // ==================== 私有辅助方法 ====================

    /**
     * 自动清理旧版本
     */
    private void autoCleanOldVersions(String key, int maxVersions) {
        Long count = redisUtil.getRedisTemplate()
                .opsForZSet()
                .zCard(buildFullKey(key));

        if (count != null && count > maxVersions) {
            // 删除最旧的版本（保留最新的 maxVersions 个）
            Long removed = redisUtil.getRedisTemplate()
                    .opsForZSet()
                    .removeRange(buildFullKey(key), 0, count - maxVersions - 1);

            log.debug("自动清理旧版本: key={}, removed={}", key, removed);
        }
    }

    /**
     * 转换为版本数据列表
     */
    private <T> List<VersionedData<T>> convertToVersionedDataList(Set<ZSetOperations.TypedTuple<Object>> result,
                                                                  Class<T> clazz) {
        return result.stream()
                .map(tuple -> {
                    T data = parseData(tuple.getValue(), clazz);

                    // 获取 score（完整纳秒时间戳）
                    Double score = tuple.getScore();

                    // 将 Double score 转为 long（完整纳秒），然后转为 Instant
                    Instant timestamp = null;
                    if (score != null) {
                        long fullNanos = score.longValue();
                        timestamp = FmkInstantUtil.fromFullNanos(fullNanos);
                    }

                    return new VersionedData<>(timestamp, data);
                })
                .filter(vd -> vd.getData() != null) // 过滤解析失败的数据
                .collect(Collectors.toList());
    }

    /**
     * 解析数据（使用 FmkJacksonUtil）
     */
    private <T> T parseData(Object jsonData, Class<T> clazz) {
        if (jsonData == null) {
            return null;
        }

        String json = jsonData.toString();

        // 使用 FmkJacksonUtil 反序列化
        T result = FmkJacksonUtil.parseObj(json, clazz);
        if (result == null) {
            log.error("解析数据失败: json={}", json);
        }

        return result;
    }

    /**
     * 构建 Redis Key（不带前缀）
     */
    private String buildKey(String entityType, String entityId) {
        return KEY_PREFIX + entityType + ":" + entityId;
    }

    /**
     * 构建完整的 Redis Key（带前缀）
     */
    private String buildFullKey(String key) {
        String prefix = redisProperties.getKeyPrefix();
        if (prefix == null || prefix.isEmpty()) {
            return key;
        }
        return prefix + key;
    }

    // ==================== 内部类 ====================

    /**
     * 版本数据（返回给调用者）
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class VersionedData<T> {
        /**
         * 版本时间戳
         */
        private Instant timestamp;

        /**
         * 数据内容
         */
        private T data;

    }
}