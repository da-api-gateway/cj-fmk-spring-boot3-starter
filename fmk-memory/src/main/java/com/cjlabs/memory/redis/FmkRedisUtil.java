package com.cjlabs.memory.redis;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Redis 工具类
 * 提供常用的 Redis 操作方法
 */
@Slf4j
@Component
public class FmkRedisUtil {

    /**
     * -- GETTER --
     *  获取 RedisTemplate
     *
     * @return RedisTemplate
     */
    @Getter
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * -- GETTER --
     *  获取 StringRedisTemplate
     *
     * @return StringRedisTemplate
     */
    @Getter
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private FmkRedisProperties redisProperties;

    // =============================String 操作=============================

    /**
     * 设置缓存
     *
     * @param key   键
     * @param value 值
     */
    public void set(String key, Object value) {
        set(key, value, redisProperties.getDefaultExpireSeconds());
    }

    /**
     * 设置缓存并指定过期时间
     *
     * @param key     键
     * @param value   值
     * @param seconds 过期时间（秒）
     */
    public void set(String key, Object value, long seconds) {
        try {
            String fullKey = buildKey(key);
            if (seconds > 0) {
                redisTemplate.opsForValue().set(fullKey, value, seconds, TimeUnit.SECONDS);
            } else {
                redisTemplate.opsForValue().set(fullKey, value);
            }
            log.debug("Redis SET: key={}, expire={}s", fullKey, seconds);
        } catch (Exception e) {
            log.error("Redis SET 失败: key={}", key, e);
            throw new RuntimeException("Redis SET 操作失败", e);
        }
    }

    /**
     * 获取缓存
     *
     * @param key 键
     * @return 值
     */
    public Object get(String key) {
        try {
            String fullKey = buildKey(key);
            Object value = redisTemplate.opsForValue().get(fullKey);
            log.debug("Redis GET: key={}, found={}", fullKey, value != null);
            return value;
        } catch (Exception e) {
            log.error("Redis GET 失败: key={}", key, e);
            return null;
        }
    }

    /**
     * 获取缓存（泛型版本）
     *
     * @param key   键
     * @param clazz 类型
     * @return 值
     */
    @SuppressWarnings("unchecked")
    public <T> T get(String key, Class<T> clazz) {
        Object value = get(key);
        if (value == null) {
            return null;
        }
        try {
            return (T) value;
        } catch (ClassCastException e) {
            log.error("Redis GET 类型转换失败: key={}, targetClass={}", key, clazz.getName(), e);
            return null;
        }
    }

    /**
     * 删除缓存
     *
     * @param key 键
     * @return 是否删除成功
     */
    public boolean delete(String key) {
        try {
            String fullKey = buildKey(key);
            Boolean result = redisTemplate.delete(fullKey);
            log.debug("Redis DELETE: key={}, result={}", fullKey, result);
            return Boolean.TRUE.equals(result);
        } catch (Exception e) {
            log.error("Redis DELETE 失败: key={}", key, e);
            return false;
        }
    }

    /**
     * 批量删除
     *
     * @param keys 键集合
     * @return 删除的数量
     */
    public long delete(Collection<String> keys) {
        try {
            Set<String> fullKeys = new HashSet<>();
            keys.forEach(key -> fullKeys.add(buildKey(key)));
            Long count = redisTemplate.delete(fullKeys);
            log.debug("Redis BATCH DELETE: count={}", count);
            return count != null ? count : 0;
        } catch (Exception e) {
            log.error("Redis BATCH DELETE 失败", e);
            return 0;
        }
    }

    /**
     * 判断 key 是否存在
     *
     * @param key 键
     * @return true=存在 false=不存在
     */
    public boolean exists(String key) {
        try {
            String fullKey = buildKey(key);
            Boolean result = redisTemplate.hasKey(fullKey);
            return Boolean.TRUE.equals(result);
        } catch (Exception e) {
            log.error("Redis EXISTS 失败: key={}", key, e);
            return false;
        }
    }

    /**
     * 设置过期时间
     *
     * @param key     键
     * @param seconds 过期时间（秒）
     * @return 是否设置成功
     */
    public boolean expire(String key, long seconds) {
        try {
            String fullKey = buildKey(key);
            Boolean result = redisTemplate.expire(fullKey, seconds, TimeUnit.SECONDS);
            log.debug("Redis EXPIRE: key={}, seconds={}, result={}", fullKey, seconds, result);
            return Boolean.TRUE.equals(result);
        } catch (Exception e) {
            log.error("Redis EXPIRE 失败: key={}", key, e);
            return false;
        }
    }

    /**
     * 获取过期时间
     *
     * @param key 键
     * @return 过期时间（秒），-1=永久有效，-2=不存在
     */
    public long getExpire(String key) {
        try {
            String fullKey = buildKey(key);
            Long expire = redisTemplate.getExpire(fullKey, TimeUnit.SECONDS);
            return expire != null ? expire : -2;
        } catch (Exception e) {
            log.error("Redis GET EXPIRE 失败: key={}", key, e);
            return -2;
        }
    }

    /**
     * 递增
     *
     * @param key   键
     * @param delta 递增值
     * @return 递增后的值
     */
    public long increment(String key, long delta) {
        try {
            String fullKey = buildKey(key);
            Long result = redisTemplate.opsForValue().increment(fullKey, delta);
            log.debug("Redis INCREMENT: key={}, delta={}, result={}", fullKey, delta, result);
            return result != null ? result : 0;
        } catch (Exception e) {
            log.error("Redis INCREMENT 失败: key={}", key, e);
            throw new RuntimeException("Redis INCREMENT 操作失败", e);
        }
    }

    /**
     * 递减
     *
     * @param key   键
     * @param delta 递减值
     * @return 递减后的值
     */
    public long decrement(String key, long delta) {
        try {
            String fullKey = buildKey(key);
            Long result = redisTemplate.opsForValue().decrement(fullKey, delta);
            log.debug("Redis DECREMENT: key={}, delta={}, result={}", fullKey, delta, result);
            return result != null ? result : 0;
        } catch (Exception e) {
            log.error("Redis DECREMENT 失败: key={}", key, e);
            throw new RuntimeException("Redis DECREMENT 操作失败", e);
        }
    }

    // =============================Hash 操作=============================

    /**
     * Hash 设置
     *
     * @param key   键
     * @param field 字段
     * @param value 值
     */
    public void hSet(String key, String field, Object value) {
        try {
            String fullKey = buildKey(key);
            redisTemplate.opsForHash().put(fullKey, field, value);
            log.debug("Redis HSET: key={}, field={}", fullKey, field);
        } catch (Exception e) {
            log.error("Redis HSET 失败: key={}, field={}", key, field, e);
            throw new RuntimeException("Redis HSET 操作失败", e);
        }
    }

    /**
     * Hash 获取
     *
     * @param key   键
     * @param field 字段
     * @return 值
     */
    public Object hGet(String key, String field) {
        try {
            String fullKey = buildKey(key);
            return redisTemplate.opsForHash().get(fullKey, field);
        } catch (Exception e) {
            log.error("Redis HGET 失败: key={}, field={}", key, field, e);
            return null;
        }
    }

    /**
     * Hash 批量设置
     *
     * @param key 键
     * @param map 多个键值对
     */
    public void hSetAll(String key, Map<String, Object> map) {
        try {
            String fullKey = buildKey(key);
            redisTemplate.opsForHash().putAll(fullKey, map);
            log.debug("Redis HMSET: key={}, size={}", fullKey, map.size());
        } catch (Exception e) {
            log.error("Redis HMSET 失败: key={}", key, e);
            throw new RuntimeException("Redis HMSET 操作失败", e);
        }
    }

    /**
     * Hash 获取所有键值对
     *
     * @param key 键
     * @return 键值对 Map
     */
    public Map<Object, Object> hGetAll(String key) {
        try {
            String fullKey = buildKey(key);
            return redisTemplate.opsForHash().entries(fullKey);
        } catch (Exception e) {
            log.error("Redis HGETALL 失败: key={}", key, e);
            return Collections.emptyMap();
        }
    }

    /**
     * Hash 删除字段
     *
     * @param key    键
     * @param fields 字段（可多个）
     * @return 删除的数量
     */
    public long hDelete(String key, Object... fields) {
        try {
            String fullKey = buildKey(key);
            Long count = redisTemplate.opsForHash().delete(fullKey, fields);
            log.debug("Redis HDEL: key={}, count={}", fullKey, count);
            return count;
        } catch (Exception e) {
            log.error("Redis HDEL 失败: key={}", key, e);
            return 0;
        }
    }

    /**
     * Hash 判断字段是否存在
     *
     * @param key   键
     * @param field 字段
     * @return true=存在 false=不存在
     */
    public boolean hExists(String key, String field) {
        try {
            String fullKey = buildKey(key);
            return redisTemplate.opsForHash().hasKey(fullKey, field);
        } catch (Exception e) {
            log.error("Redis HEXISTS 失败: key={}, field={}", key, field, e);
            return false;
        }
    }

    // =============================Set 操作=============================

    /**
     * Set 添加元素
     *
     * @param key    键
     * @param values 值（可多个）
     * @return 添加的数量
     */
    public long sAdd(String key, Object... values) {
        try {
            String fullKey = buildKey(key);
            Long count = redisTemplate.opsForSet().add(fullKey, values);
            log.debug("Redis SADD: key={}, count={}", fullKey, count);
            return count != null ? count : 0;
        } catch (Exception e) {
            log.error("Redis SADD 失败: key={}", key, e);
            return 0;
        }
    }

    /**
     * Set 获取所有元素
     *
     * @param key 键
     * @return 元素集合
     */
    public Set<Object> sMembers(String key) {
        try {
            String fullKey = buildKey(key);
            Set<Object> members = redisTemplate.opsForSet().members(fullKey);
            return members != null ? members : Collections.emptySet();
        } catch (Exception e) {
            log.error("Redis SMEMBERS 失败: key={}", key, e);
            return Collections.emptySet();
        }
    }

    /**
     * Set 判断元素是否存在
     *
     * @param key   键
     * @param value 值
     * @return true=存在 false=不存在
     */
    public boolean sIsMember(String key, Object value) {
        try {
            String fullKey = buildKey(key);
            Boolean result = redisTemplate.opsForSet().isMember(fullKey, value);
            return Boolean.TRUE.equals(result);
        } catch (Exception e) {
            log.error("Redis SISMEMBER 失败: key={}", key, e);
            return false;
        }
    }

    /**
     * Set 移除元素
     *
     * @param key    键
     * @param values 值（可多个）
     * @return 移除的数量
     */
    public long sRemove(String key, Object... values) {
        try {
            String fullKey = buildKey(key);
            Long count = redisTemplate.opsForSet().remove(fullKey, values);
            log.debug("Redis SREM: key={}, count={}", fullKey, count);
            return count != null ? count : 0;
        } catch (Exception e) {
            log.error("Redis SREM 失败: key={}", key, e);
            return 0;
        }
    }

    // =============================List 操作=============================

    /**
     * List 右侧添加元素
     *
     * @param key   键
     * @param value 值
     * @return 列表长度
     */
    public long lPush(String key, Object value) {
        try {
            String fullKey = buildKey(key);
            Long size = redisTemplate.opsForList().rightPush(fullKey, value);
            log.debug("Redis RPUSH: key={}, size={}", fullKey, size);
            return size != null ? size : 0;
        } catch (Exception e) {
            log.error("Redis RPUSH 失败: key={}", key, e);
            return 0;
        }
    }

    /**
     * List 批量右侧添加元素
     *
     * @param key    键
     * @param values 值列表
     * @return 列表长度
     */
    public long lPushAll(String key, Collection<Object> values) {
        try {
            String fullKey = buildKey(key);
            Long size = redisTemplate.opsForList().rightPushAll(fullKey, values);
            log.debug("Redis RPUSH ALL: key={}, size={}", fullKey, size);
            return size != null ? size : 0;
        } catch (Exception e) {
            log.error("Redis RPUSH ALL 失败: key={}", key, e);
            return 0;
        }
    }

    /**
     * List 左侧弹出元素
     *
     * @param key 键
     * @return 元素
     */
    public Object lPop(String key) {
        try {
            String fullKey = buildKey(key);
            return redisTemplate.opsForList().leftPop(fullKey);
        } catch (Exception e) {
            log.error("Redis LPOP 失败: key={}", key, e);
            return null;
        }
    }

    /**
     * List 获取指定范围的元素
     *
     * @param key   键
     * @param start 开始位置
     * @param end   结束位置（-1 表示到末尾）
     * @return 元素列表
     */
    public List<Object> lRange(String key, long start, long end) {
        try {
            String fullKey = buildKey(key);
            List<Object> list = redisTemplate.opsForList().range(fullKey, start, end);
            return list != null ? list : Collections.emptyList();
        } catch (Exception e) {
            log.error("Redis LRANGE 失败: key={}", key, e);
            return Collections.emptyList();
        }
    }

    /**
     * List 获取长度
     *
     * @param key 键
     * @return 长度
     */
    public long lSize(String key) {
        try {
            String fullKey = buildKey(key);
            Long size = redisTemplate.opsForList().size(fullKey);
            return size != null ? size : 0;
        } catch (Exception e) {
            log.error("Redis LLEN 失败: key={}", key, e);
            return 0;
        }
    }

    // =============================ZSet 操作=============================

    /**
     * ZSet 添加元素
     *
     * @param key   键
     * @param value 值
     * @param score 分数
     * @return 是否添加成功
     */
    public boolean zAdd(String key, Object value, double score) {
        try {
            String fullKey = buildKey(key);
            Boolean result = redisTemplate.opsForZSet().add(fullKey, value, score);
            log.debug("Redis ZADD: key={}, score={}, result={}", fullKey, score, result);
            return Boolean.TRUE.equals(result);
        } catch (Exception e) {
            log.error("Redis ZADD 失败: key={}", key, e);
            return false;
        }
    }

    /**
     * ZSet 获取指定范围的元素（按分数从小到大）
     *
     * @param key   键
     * @param start 开始位置
     * @param end   结束位置（-1 表示到末尾）
     * @return 元素集合
     */
    public Set<Object> zRange(String key, long start, long end) {
        try {
            String fullKey = buildKey(key);
            Set<Object> set = redisTemplate.opsForZSet().range(fullKey, start, end);
            return set != null ? set : Collections.emptySet();
        } catch (Exception e) {
            log.error("Redis ZRANGE 失败: key={}", key, e);
            return Collections.emptySet();
        }
    }

    /**
     * ZSet 移除元素
     *
     * @param key    键
     * @param values 值（可多个）
     * @return 移除的数量
     */
    public long zRemove(String key, Object... values) {
        try {
            String fullKey = buildKey(key);
            Long count = redisTemplate.opsForZSet().remove(fullKey, values);
            log.debug("Redis ZREM: key={}, count={}", fullKey, count);
            return count != null ? count : 0;
        } catch (Exception e) {
            log.error("Redis ZREM 失败: key={}", key, e);
            return 0;
        }
    }

    // =============================工具方法=============================

    /**
     * 构建完整的 key（添加前缀）
     *
     * @param key 原始 key
     * @return 完整 key
     */
    private String buildKey(String key) {
        String prefix = redisProperties.getKeyPrefix();
        if (prefix == null || prefix.isEmpty()) {
            return key;
        }
        return prefix + key;
    }

}