package com.cjlabs.web.token.service;

import com.cjlabs.core.time.FmkInstantUtil;
import com.cjlabs.core.types.longs.FmkUserId;
import com.cjlabs.core.types.strings.FmkToken;
import com.cjlabs.web.threadlocal.FmkUserInfo;
import com.cjlabs.web.token.FmkTokenProperties;
import com.cjlabs.web.token.IFmkTokenService;
import com.cjlabs.web.token.bo.FmkClientInfo;
import com.cjlabs.web.token.bo.FmkTokenInfo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Token 服务内存实现
 * 使用内存存储 Token，适合开发环境和单机环境
 * 当 fmk.token.type=memory 时启用（默认）
 */
@Slf4j
@Service("fmkTokenService")
@ConditionalOnProperty(name = "fmk.token.type", havingValue = "memory", matchIfMissing = true)
public class FmkTokenServiceMemoryImpl implements IFmkTokenService {

    @Autowired
    private FmkTokenProperties tokenProperties;

    // Token到 TokenInfo 的映射（统一存储所有 Token 信息）
    private static final Map<FmkToken, FmkTokenInfo> TOKEN_TOKEN_INFO_MAP = new ConcurrentHashMap<>();
    // 用户ID到用户信息的映射
    private static final Map<FmkUserId, FmkUserInfo> USER_ID_USER_INFO_MAP = new ConcurrentHashMap<>();

    @Override
    public FmkToken createAndSaveToken(FmkUserInfo userInfo, FmkClientInfo clientInfo) {
        if (userInfo == null) {
            log.warn("FmkTokenServiceMemoryImpl|createAndSaveToken|userInfo为空");
            return null;
        }

        try {
            // 检查缓存大小限制
            int maxSize = tokenProperties.getMemory().getMaxSize();
            if (TOKEN_TOKEN_INFO_MAP.size() >= maxSize) {
                log.warn("FmkTokenServiceMemoryImpl|createAndSaveToken|缓存已满|maxSize={}|currentSize={}", maxSize, TOKEN_TOKEN_INFO_MAP.size());
                // TODO: 实现LRU淘汰策略
                // 这里应该要tg通知了
                // return null;
            }

            // 生成 Token
            FmkToken token = FmkToken.generate();

            // 创建 TokenInfo
            FmkTokenInfo tokenInfo = new FmkTokenInfo();
            tokenInfo.setToken(token);
            tokenInfo.setUserId(userInfo.getUserId());

            FmkTokenProperties.MemoryConfig memory = tokenProperties.getMemory();
            int expireHours = memory.getExpireHours();
            Instant plus = FmkInstantUtil.now().plus(expireHours, ChronoUnit.HOURS);
            tokenInfo.setExpireTime(plus);

            // 设置客户端信息
            if (clientInfo != null) {
                clientInfo.updateLastActiveTime();
                tokenInfo.setClientInfo(clientInfo);
            }

            // 存储 TokenInfo
            TOKEN_TOKEN_INFO_MAP.put(token, tokenInfo);

            // 存储用户信息
            USER_ID_USER_INFO_MAP.put(userInfo.getUserId(), userInfo);

            log.info("FmkTokenServiceMemoryImpl|createAndSaveToken|创建成功|userId={}|token={}|maxSize={}",
                    userInfo.getUserId().getValue(), maskToken(token.getValue()), maxSize);

            return token;
        } catch (Exception e) {
            log.error("FmkTokenServiceMemoryImpl|createAndSaveToken|创建失败", e);
            return null;
        }
    }

    @Override
    public boolean validateToken(FmkToken token) {
        if (token == null) {
            return false;
        }

        // 检查Token是否存在
        FmkTokenInfo tokenInfo = TOKEN_TOKEN_INFO_MAP.get(token);
        if (tokenInfo == null) {
            return false;
        }

        Instant expireTime = tokenInfo.getExpireTime();
        if (expireTime == null) {
            return false;
        }

        // 检查Token是否过期
        if (FmkInstantUtil.now().compareTo(expireTime) > 0) {
            log.info("FmkTokenServiceMemoryImpl|validateToken|Token已过期|token={}|expireTime={}",
                    maskToken(token.getValue()), expireTime);
            return false;
        }

        updateLastActiveTime(token);

        return true;
    }

    @Override
    public Optional<FmkUserId> getUserIdByToken(FmkToken token) {
        if (token == null) {
            return Optional.empty();
        }

        FmkTokenInfo tokenInfo = TOKEN_TOKEN_INFO_MAP.get(token);
        if (tokenInfo == null) {
            return Optional.empty();
        }

        return Optional.ofNullable(tokenInfo.getUserId());
    }

    @Override
    public Optional<FmkUserInfo> getUserInfoByToken(FmkToken token) {
        if (token == null) {
            return Optional.empty();
        }

        FmkTokenInfo tokenInfo = TOKEN_TOKEN_INFO_MAP.get(token);
        if (tokenInfo == null) {
            return Optional.empty();
        }

        FmkUserInfo fmkUserInfo = USER_ID_USER_INFO_MAP.get(tokenInfo.getUserId());

        return Optional.ofNullable(fmkUserInfo);
    }

    @Override
    public Optional<FmkToken> getTokenByUserId(FmkUserId userId) {
        if (userId == null) {
            return Optional.empty();
        }

        for (Map.Entry<FmkToken, FmkTokenInfo> entry : TOKEN_TOKEN_INFO_MAP.entrySet()) {
            FmkTokenInfo tokenInfo = entry.getValue();
            if (tokenInfo.getUserId().equals(userId)) {
                return Optional.of(entry.getKey());
            }
        }

        return Optional.empty();
    }

    @Override
    public Optional<FmkUserInfo> getUserInfoByUserId(FmkUserId userId) {
        if (userId == null) {
            return Optional.empty();
        }

        FmkUserInfo fmkUserInfo = USER_ID_USER_INFO_MAP.get(userId);

        return Optional.ofNullable(fmkUserInfo);
    }

    @Override
    public Optional<FmkClientInfo> getClientInfoByToken(FmkToken token) {
        if (token == null) {
            return Optional.empty();
        }

        FmkTokenInfo tokenInfo = TOKEN_TOKEN_INFO_MAP.get(token);
        if (tokenInfo == null) {
            return Optional.empty();
        }

        FmkClientInfo clientInfo = tokenInfo.getClientInfo();

        return Optional.ofNullable(clientInfo);
    }

    @Override
    public Optional<FmkTokenInfo> getTokenInfoByToken(FmkToken token) {
        if (Objects.isNull(token)) {
            return Optional.empty();
        }

        FmkTokenInfo tokenInfo = TOKEN_TOKEN_INFO_MAP.get(token);
        if (tokenInfo == null) {
            return Optional.empty();
        }

        return Optional.of(tokenInfo);
    }

    @Override
    public boolean refreshToken(FmkToken token) {
        // 内存实现暂不支持刷新过期时间
        return validateToken(token);
    }

    @Override
    public boolean revokeToken(FmkToken token) {
        if (token == null) {
            return false;
        }

        // 从 TokenInfo 映射中移除
        FmkTokenInfo removed = TOKEN_TOKEN_INFO_MAP.remove(token);

        if (removed != null) {
            log.info("FmkTokenServiceMemoryImpl|revokeToken|移除Token|token={}|userId={}",
                    maskToken(token.getValue()), removed.getUserId().getValue());
            return true;
        }

        return false;
    }

    @Override
    public void revokeAllUserTokens(FmkUserId userId) {
        if (userId == null) {
            return;
        }

        Optional<FmkToken> tokenOptional = getTokenByUserId(userId);
        if (tokenOptional.isEmpty()) {
            return;
        }

        TOKEN_TOKEN_INFO_MAP.remove(tokenOptional.get());

        // 移除用户信息缓存
        USER_ID_USER_INFO_MAP.remove(userId);

        log.info("FmkTokenServiceMemoryImpl|revokeAllUserTokens|移除用户所有Token|userId={}", userId.getValue());

    }

    @Override
    public void cleanExpiredTokens() {
        Instant now = FmkInstantUtil.now();

        // 收集需要删除的 Token（避免在迭代时修改Map）
        List<FmkToken> expiredTokens = new ArrayList<>();

        for (Map.Entry<FmkToken, FmkTokenInfo> entry : TOKEN_TOKEN_INFO_MAP.entrySet()) {
            FmkTokenInfo tokenInfo = entry.getValue();
            Instant expireTime = tokenInfo.getExpireTime();

            // 检查是否过期
            if (expireTime != null && now.compareTo(expireTime) > 0) {
                expiredTokens.add(entry.getKey());
            }
        }

        // 删除过期的 Token
        for (FmkToken token : expiredTokens) {
            FmkTokenInfo removed = TOKEN_TOKEN_INFO_MAP.remove(token);
            if (removed != null) {
                log.info("FmkTokenServiceMemoryImpl|cleanExpiredTokens|清理过期Token|token={}|userId={}|expireTime={}",
                        maskToken(token.getValue()),
                        removed.getUserId().getValue(),
                        removed.getExpireTime());
            }
        }
    }

    // ==================== 私有辅助方法 ====================

    /**
     * 更新设备最后活跃时间
     */
    private void updateExpireTime(FmkToken token) {
        FmkTokenInfo tokenInfo = TOKEN_TOKEN_INFO_MAP.get(token);
        if (tokenInfo != null) {
            Instant expireTime = tokenInfo.getExpireTime();

            FmkTokenProperties.MemoryConfig memory = tokenProperties.getMemory();
            int expireHours = memory.getExpireHours();
            Instant plus = expireTime.plus(expireHours, ChronoUnit.HOURS);
            tokenInfo.setExpireTime(plus);
        }
    }

    private void updateLastActiveTime(FmkToken token) {
        if (token == null) {
            return;
        }

        FmkTokenInfo tokenInfo = TOKEN_TOKEN_INFO_MAP.get(token);
        if (tokenInfo != null && tokenInfo.getClientInfo() != null) {
            tokenInfo.getClientInfo().updateLastActiveTime();
        }
    }

    /**
     * Token 掩码处理
     */
    private String maskToken(String token) {
        if (token == null || token.length() <= 10) {
            return "***";
        }
        return token.substring(0, 10) + "...";
    }
}