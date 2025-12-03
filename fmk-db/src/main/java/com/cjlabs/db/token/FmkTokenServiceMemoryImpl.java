package com.cjlabs.db.token;

import com.cjlabs.core.types.longs.FmkUserId;
import com.cjlabs.core.types.strings.FmkToken;
import com.cjlabs.web.threadlocal.FmkUserInfo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 框架级 Token 服务
 * 主要用于请求拦截器中的 token 验证和用户信息管理
 */
@Slf4j
@Component
public class FmkTokenServiceMemoryImpl {

    // Token到用户ID的映射
    private static final Map<FmkToken, FmkUserId> TOKEN_INFO_USER_ID_MAP = new ConcurrentHashMap<>();

    // Token到设备信息的映射
    private static final Map<FmkToken, FmkClientInfo> TOKEN_INFO_CLIENT_TYPE_MAP = new ConcurrentHashMap<>();

    // 用户ID到用户信息的映射
    private static final Map<FmkUserId, FmkUserInfo> USER_ID_USER_INFO_MAP = new ConcurrentHashMap<>();

    /**
     * 存储用户登录信息
     *
     * @param userId     用户ID
     * @param userInfo   用户信息
     * @param token      Token
     * @param clientInfo 设备信息
     */
    public void storeUserLogin(FmkUserId userId,
                               FmkUserInfo userInfo,
                               FmkToken token,
                               FmkClientInfo clientInfo) {
        if (userId == null || token == null) {
            log.warn("FmkTokenService|storeUserLogin|参数无效|userId={}|token={}", userId, token);
            return;
        }

        try {
            // 1. 存储Token到用户ID的映射
            TOKEN_INFO_USER_ID_MAP.put(token, userId);

            // 2. 存储Token到设备信息的映射
            if (clientInfo != null) {
                TOKEN_INFO_CLIENT_TYPE_MAP.put(token, clientInfo);
            }

            // 3. 缓存用户信息
            if (userInfo != null) {
                USER_ID_USER_INFO_MAP.put(userId, userInfo);
            }

            log.info("FmkTokenService|storeUserLogin|存储成功|userId={}|token={}",
                    userId.getValue(), token.getValue());
        } catch (Exception e) {
            log.error("FmkTokenService|storeUserLogin|存储失败", e);
        }
    }

    /**
     * 根据Token获取用户ID
     */
    public Optional<FmkUserId> getUserIdByToken(FmkToken token) {
        if (token == null) {
            return Optional.empty();
        }

        return Optional.ofNullable(TOKEN_INFO_USER_ID_MAP.get(token));
    }

    /**
     * 根据Token获取设备信息
     */
    public Optional<FmkClientInfo> getDeviceInfoByToken(FmkToken token) {
        if (token == null) {
            return Optional.empty();
        }

        FmkClientInfo clientInfo = TOKEN_INFO_CLIENT_TYPE_MAP.get(token);
        if (clientInfo != null) {
            // 更新最后活跃时间
            clientInfo.updateLastActiveTime();
        }
        return Optional.ofNullable(clientInfo);
    }

    /**
     * 根据Token获取用户信息
     */
    public Optional<FmkUserInfo> getUserInfoByToken(FmkToken token) {
        if (token == null) {
            log.info("FmkTokenService|getUserInfoByToken|token为空");
            return Optional.empty();
        }

        try {
            // 1. 获取用户ID
            FmkUserId userId = TOKEN_INFO_USER_ID_MAP.get(token);
            if (userId == null) {
                log.info("FmkTokenService|getUserInfoByToken|未找到用户ID|token={}", token.getValue());
                return Optional.empty();
            }

            // 2. 获取用户信息
            FmkUserInfo userInfo = USER_ID_USER_INFO_MAP.get(userId);
            if (userInfo == null) {
                log.info("FmkTokenService|getUserInfoByToken|未找到用户信息|userId={}", userId.getValue());
                return Optional.empty();
            }

            // 3. 更新设备最后活跃时间
            updateClientLastActiveTime(token);

            return Optional.of(userInfo);
        } catch (Exception e) {
            log.error("FmkTokenService|getUserInfoByToken|获取用户信息异常|token={}", token.getValue(), e);
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

        USER_ID_USER_INFO_MAP.put(userId, userInfo);
        log.info("FmkTokenService|cacheUserInfo|缓存用户信息|userId={}", userId.getValue());
    }

    /**
     * 根据用户ID获取缓存的用户信息
     */
    public Optional<FmkUserInfo> getCachedUserInfo(FmkUserId userId) {
        if (userId == null) {
            return Optional.empty();
        }

        FmkUserInfo userInfo = USER_ID_USER_INFO_MAP.get(userId);
        return Optional.ofNullable(userInfo);
    }

    /**
     * 验证Token是否有效
     */
    public boolean validateToken(FmkToken token) {
        if (token == null) {
            return false;
        }

        // 检查Token是否存在且对应用户ID
        FmkUserId userId = TOKEN_INFO_USER_ID_MAP.get(token);
        if (userId == null) {
            return false;
        }

        // 更新设备最后活跃时间
        updateClientLastActiveTime(token);

        return true;
    }

    /**
     * 移除用户所有Token
     */
    public void removeAllUserTokens(FmkUserId userId) {
        if (userId == null) {
            return;
        }

        // 1. 找出该用户的所有Token
        TOKEN_INFO_USER_ID_MAP.entrySet().removeIf(entry -> {
            if (userId.equals(entry.getValue())) {
                FmkToken token = entry.getKey();
                // 2. 从设备信息映射中移除
                TOKEN_INFO_CLIENT_TYPE_MAP.remove(token);
                return true;
            }
            return false;
        });

        // 3. 移除用户信息缓存
        USER_ID_USER_INFO_MAP.remove(userId);

        log.info("FmkTokenService|removeAllUserTokens|移除用户所有Token|userId={}", userId.getValue());
    }

    /**
     * 移除特定Token
     */
    public void removeToken(FmkToken token) {
        if (token == null) {
            return;
        }

        // 1. 从用户ID映射中移除
        TOKEN_INFO_USER_ID_MAP.remove(token);

        // 2. 从设备信息映射中移除
        TOKEN_INFO_CLIENT_TYPE_MAP.remove(token);

        log.info("FmkTokenService|removeToken|移除Token|token={}", token.getValue());
    }

    /**
     * 更新设备最后活跃时间
     */
    private void updateClientLastActiveTime(FmkToken token) {
        FmkClientInfo clientInfo = TOKEN_INFO_CLIENT_TYPE_MAP.get(token);
        if (clientInfo != null) {
            clientInfo.updateLastActiveTime();
        }
    }
}