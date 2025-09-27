package com.cjlabs.web.token;

import com.xodo.business.common.user.enums.DeviceTypeEnum;
import com.xodo.fmk.core.FmkUserInfo;
import com.xodo.fmk.jdk.basetype.type.FmkToken;
import com.xodo.fmk.jdk.basetype.type.FmkUserId;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 框架级 Token 服务
 * 主要用于请求拦截器中的 token 验证
 */
@Slf4j
@Component
public class FmkTokenService {

    // ============ Redis 1: 用户设备Token映射 ============
    // hashkey: userid, field: device, value: token
    private static final Map<FmkUserId, Map<DeviceTypeEnum, FmkToken>> USER_DEVICE_TOKEN_MAP = new ConcurrentHashMap<>();

    // ============ Redis 2: Token设备信息映射 ============
    // key: token, value: device info
    private static final Map<FmkToken, DeviceInfo> TOKEN_DEVICE_INFO_MAP = new ConcurrentHashMap<>();

    // ============ Redis 3: 用户信息缓存 ============
    // key: userid, value: userinfo
    private static final Map<FmkUserId, FmkUserInfo> USER_INFO_CACHE_MAP = new ConcurrentHashMap<>();

    /**
     * 存储用户Token信息
     *
     * @param userId     用户ID
     * @param deviceType 设备类型 (iphone, pc, android, web)
     * @param token      Token
     * @param deviceInfo 设备信息
     */
    public void storeUserToken(FmkUserId userId,
                               DeviceTypeEnum deviceType,
                               FmkToken token,
                               DeviceInfo deviceInfo) {
        if (userId == null || Objects.isNull(deviceType) || token == null) {
            log.warn("FmkTokenService|storeUserToken|参数无效|userId={}|deviceType={}|token={}",
                    userId, deviceType, token);
            return;
        }

        try {
            // 1. 存储到 USER_DEVICE_TOKEN_MAP (Redis 1)
            USER_DEVICE_TOKEN_MAP
                    .computeIfAbsent(userId, k -> new ConcurrentHashMap<>())
                    .put(deviceType, token);

            // 2. 存储到 TOKEN_DEVICE_INFO_MAP (Redis 2)
            if (deviceInfo == null) {
                deviceInfo = new DeviceInfo(deviceType, null, null, null);
            }
            TOKEN_DEVICE_INFO_MAP.put(token, deviceInfo);

            log.info("FmkTokenService|storeUserToken|存储成功|userId={}|deviceType={}|token={}",
                    userId.getValue(), deviceType, token.getValue());

        } catch (Exception e) {
            log.error("FmkTokenService|storeUserToken|存储失败", e);
        }
    }

    /**
     * 根据用户ID和设备类型获取Token
     */
    public Optional<FmkToken> getTokenByUserAndDevice(FmkUserId userId, DeviceTypeEnum deviceType) {
        if (userId == null || Objects.isNull(deviceType)) {
            return Optional.empty();
        }

        Map<DeviceTypeEnum, FmkToken> deviceTokenMap = USER_DEVICE_TOKEN_MAP.get(userId);
        if (deviceTokenMap == null) {
            return Optional.empty();
        }

        FmkToken token = deviceTokenMap.get(deviceType);
        return Optional.ofNullable(token);
    }

    /**
     * 根据Token获取设备信息
     */
    public Optional<DeviceInfo> getDeviceInfoByToken(FmkToken token) {
        if (token == null) {
            return Optional.empty();
        }

        DeviceInfo deviceInfo = TOKEN_DEVICE_INFO_MAP.get(token);
        return Optional.ofNullable(deviceInfo);
    }

    /**
     * 根据Token获取用户信息
     */
    public Optional<FmkUserInfo> getUserByToken(FmkToken token) {
        if (token == null) {
            log.info("FmkTokenService|getUserByToken|token为空");
            return Optional.empty();
        }

        try {
            // 1. 先从设备信息中找到对应的用户（通过反向查找）
            FmkUserId userId = findUserIdByToken(token);
            if (userId == null) {
                log.info("FmkTokenService|getUserByToken|未找到token对应的用户|token={}", token.getValue());
                return Optional.empty();
            }

            // 2. 从用户信息缓存中获取用户信息
            FmkUserInfo userInfo = USER_INFO_CACHE_MAP.get(userId);
            if (userInfo != null) {
                // 更新设备最后活跃时间
                updateDeviceLastActiveTime(token);
                return Optional.of(userInfo);
            }

            log.info("FmkTokenService|getUserByToken|用户信息缓存中未找到|userId={}", userId.getValue());
            return Optional.empty();

        } catch (Exception e) {
            log.error("FmkTokenService|getUserByToken|获取用户信息异常|token={}", token.getValue(), e);
            return Optional.empty();
        }
    }

    /**
     * 缓存用户信息
     */
    public void cacheUserInfo(FmkUserId userId, FmkUserInfo userInfo) {
        if (userId == null || userInfo == null) {
            return;
        }

        USER_INFO_CACHE_MAP.put(userId, userInfo);
        log.info("FmkTokenService|cacheUserInfo|缓存用户信息|userId={}", userId.getValue());
    }

    /**
     * 根据用户ID获取缓存的用户信息
     */
    public Optional<FmkUserInfo> getCachedUserInfo(FmkUserId userId) {
        if (userId == null) {
            return Optional.empty();
        }

        FmkUserInfo userInfo = USER_INFO_CACHE_MAP.get(userId);
        return Optional.ofNullable(userInfo);
    }

    /**
     * 获取用户所有设备的Token
     */
    public Map<DeviceTypeEnum, FmkToken> getUserAllDeviceTokens(FmkUserId userId) {
        if (userId == null) {
            return new ConcurrentHashMap<>();
        }

        Map<DeviceTypeEnum, FmkToken> deviceTokenMap = USER_DEVICE_TOKEN_MAP.get(userId);
        return deviceTokenMap != null ? new ConcurrentHashMap<>(deviceTokenMap) : new ConcurrentHashMap<>();
    }

    /**
     * 移除用户特定设备的Token
     */
    public void removeUserDeviceToken(FmkUserId userId, DeviceTypeEnum deviceType) {
        if (userId == null || Objects.isNull(deviceType)) {
            return;
        }

        Map<DeviceTypeEnum, FmkToken> deviceTokenMap = USER_DEVICE_TOKEN_MAP.get(userId);
        if (deviceTokenMap != null) {
            FmkToken removedToken = deviceTokenMap.remove(deviceType);
            if (removedToken != null) {
                // 同时从设备信息映射中移除
                TOKEN_DEVICE_INFO_MAP.remove(removedToken);
                log.info("FmkTokenService|removeUserDeviceToken|移除成功|userId={}|deviceType={}|token={}",
                        userId.getValue(), deviceType, removedToken.getValue());
            }
        }
    }

    /**
     * 移除用户所有设备的Token
     */
    public void removeAllUserTokens(FmkUserId userId) {
        if (userId == null) {
            return;
        }

        Map<DeviceTypeEnum, FmkToken> deviceTokenMap = USER_DEVICE_TOKEN_MAP.remove(userId);
        if (deviceTokenMap != null) {
            // 从设备信息映射中移除所有相关Token
            for (FmkToken token : deviceTokenMap.values()) {
                TOKEN_DEVICE_INFO_MAP.remove(token);
            }
            log.info("FmkTokenService|removeAllUserTokens|移除用户所有Token|userId={}|tokenCount={}",
                    userId.getValue(), deviceTokenMap.size());
        }

        // 移除用户信息缓存
        USER_INFO_CACHE_MAP.remove(userId);
    }

    /**
     * 移除特定Token
     */
    public void removeToken(FmkToken token) {
        if (token == null) {
            return;
        }

        // 1. 从设备信息映射中移除
        TOKEN_DEVICE_INFO_MAP.remove(token);

        // 2. 从用户设备Token映射中移除
        USER_DEVICE_TOKEN_MAP.forEach((userId, deviceTokenMap) -> {
            deviceTokenMap.entrySet().removeIf(entry -> entry.getValue().equals(token));
        });

        log.info("FmkTokenService|removeToken|移除Token|token={}", token.getValue());
    }

    /**
     * 清理过期Token（可定时调用）
     */
    public void cleanExpiredTokens(int expireHours) {
        LocalDateTime expireTime = LocalDateTime.now().minusHours(expireHours);

        int cleanCount = 0;
        for (Map.Entry<FmkToken, DeviceInfo> entry : TOKEN_DEVICE_INFO_MAP.entrySet()) {
            DeviceInfo deviceInfo = entry.getValue();
            if (deviceInfo.getLastActiveTime().isBefore(expireTime)) {
                FmkToken expiredToken = entry.getKey();
                removeToken(expiredToken);
                cleanCount++;
            }
        }

        if (cleanCount > 0) {
            log.info("FmkTokenService|cleanExpiredTokens|清理过期Token|count={}", cleanCount);
        }
    }

    /**
     * 获取缓存统计信息
     */
    public Map<String, Object> getCacheStats() {
        Map<String, Object> stats = new ConcurrentHashMap<>();
        stats.put("userDeviceTokenCount", USER_DEVICE_TOKEN_MAP.size());
        stats.put("tokenDeviceInfoCount", TOKEN_DEVICE_INFO_MAP.size());
        stats.put("userInfoCacheCount", USER_INFO_CACHE_MAP.size());

        // 计算总Token数量
        int totalTokens = USER_DEVICE_TOKEN_MAP.values().stream()
                .mapToInt(Map::size)
                .sum();
        stats.put("totalTokens", totalTokens);

        return stats;
    }

    // ============ 私有方法 ============

    /**
     * 根据Token反向查找用户ID
     */
    private FmkUserId findUserIdByToken(FmkToken token) {
        for (Map.Entry<FmkUserId, Map<DeviceTypeEnum, FmkToken>> entry : USER_DEVICE_TOKEN_MAP.entrySet()) {
            Map<DeviceTypeEnum, FmkToken> deviceTokenMap = entry.getValue();
            if (deviceTokenMap.containsValue(token)) {
                return entry.getKey();
            }
        }
        return null;
    }

    /**
     * 更新设备最后活跃时间
     */
    private void updateDeviceLastActiveTime(FmkToken token) {
        DeviceInfo deviceInfo = TOKEN_DEVICE_INFO_MAP.get(token);
        if (deviceInfo != null) {
            deviceInfo.setLastActiveTime(LocalDateTime.now());
        }
    }

}
