package com.cjlabs.web.token;

import com.cjlabs.core.types.longs.FmkUserId;
import com.cjlabs.core.types.strings.FmkToken;
import com.cjlabs.web.threadlocal.FmkUserInfo;
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
     * @param token      Token
     * @param deviceInfo 设备信息
     */
    public void storeUserToken(FmkUserId userId,
                               FmkToken token,
                               DeviceInfo deviceInfo) {
        if (userId == null || token == null) {
            log.warn("FmkTokenService|storeUserToken|参数无效|userId={}|token={}", userId, token);
            return;
        }

        try {
            // 2. 存储到 TOKEN_DEVICE_INFO_MAP (Redis 2)
            if (deviceInfo == null) {
                deviceInfo = new DeviceInfo(null, null, null);
            }
            TOKEN_DEVICE_INFO_MAP.put(token, deviceInfo);

            log.info("FmkTokenService|storeUserToken|存储成功|userId={}|token={}",
                    userId.getValue(), token.getValue());

        } catch (Exception e) {
            log.error("FmkTokenService|storeUserToken|存储失败", e);
        }
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
     * 移除用户所有设备的Token
     */
    public void removeAllUserTokens(FmkUserId userId) {
        if (userId == null) {
            return;
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

        log.info("FmkTokenService|removeToken|移除Token|token={}", token.getValue());
    }

    // ============ 私有方法 ============


    /**
     * 更新设备最后活跃时间
     */
    private void updateDeviceLastActiveTime(FmkToken token) {
        DeviceInfo deviceInfo = TOKEN_DEVICE_INFO_MAP.get(token);
        if (deviceInfo != null) {
            deviceInfo.setLastActiveTime(System.currentTimeMillis());
        }
    }

}
