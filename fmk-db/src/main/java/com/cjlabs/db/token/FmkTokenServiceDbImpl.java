package com.cjlabs.db.token;

import com.cjlabs.core.time.FmkInstantUtil;
import com.cjlabs.core.types.longs.FmkUserId;
import com.cjlabs.core.types.strings.FmkToken;
import com.cjlabs.db.token.enums.TokenStatusEnum;
import com.cjlabs.db.token.mapper.LoginInfoTokenWrapMapper;
import com.cjlabs.db.token.mysql.LoginInfoToken;
import com.cjlabs.web.threadlocal.FmkUserInfo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

/**
 * Token 服务数据库实现
 * 使用数据库存储 Token，适合生产环境
 */
@Slf4j
@Service("fmkTokenService")
@ConditionalOnProperty(name = "fmk.token.save", havingValue = "database", matchIfMissing = false)
public class FmkTokenServiceDbImpl implements IFmkTokenService {

    @Autowired
    private LoginInfoTokenWrapMapper loginInfoTokenWrapMapper;

    @Override
    public FmkToken createAndSaveToken(FmkUserId userId, FmkUserInfo userInfo, FmkTokenInfo tokenInfo) {
        try {
            // 生成 Token
            FmkToken token = FmkToken.generate();

            // 构建实体
            LoginInfoToken entity = new LoginInfoToken();
            entity.setUserId(userId);
            entity.setUsername(userInfo != null ? userInfo.getUserName() : "");
            entity.setToken(token);
            entity.setClientType(tokenInfo.getClientType());
            entity.setIpAddress(tokenInfo.getIpAddress());
            entity.setUserAgent(tokenInfo.getUserAgent());
            entity.setExpireTime(calculateExpireTime());
            entity.setStatus(TokenStatusEnum.ACTIVE);
            entity.setRemark(tokenInfo.getRemark());

            // 保存到数据库
            loginInfoTokenWrapMapper.save(entity);

            log.info("FmkTokenServiceDbImpl|createAndSaveToken|Token创建成功|userId={}|token={}",
                    userId.getValue(), maskToken(token.getValue()));

            return token;
        } catch (Exception e) {
            log.error("FmkTokenServiceDbImpl|createAndSaveToken|Token创建失败|userId={}", userId.getValue(), e);
            throw new RuntimeException("创建Token失败", e);
        }
    }

    @Override
    public boolean validateToken(FmkToken token) {
        if (token == null) {
            return false;
        }

        try {
            boolean checkedTokenValid = loginInfoTokenWrapMapper.checkTokenValid(token);
            if (checkedTokenValid) {
                // 更新最后活跃时间（可选，异步更新以提高性能）
                refreshTokenLastActiveTime(token);
            }
            return checkedTokenValid;
        } catch (Exception e) {
            log.error("FmkTokenServiceDbImpl|validateToken|Token验证失败|token={}", maskToken(token.getValue()), e);
            return false;
        }
    }

    @Override
    public Optional<FmkUserId> getUserIdByToken(FmkToken token) {
        if (token == null) {
            return Optional.empty();
        }

        // 👉 调用 WrapMapper 的业务方法
        return loginInfoTokenWrapMapper.getUserIdByToken(token);
    }

    @Override
    public Optional<FmkTokenInfo> getTokenInfo(FmkToken token) {
        if (token == null) {
            return Optional.empty();
        }

        // 👉 调用 WrapMapper 的业务方法
        // 👉 调用 WrapMapper 的业务方法，直接使用 Optional.map()
        return loginInfoTokenWrapMapper.getTokenInfoByToken(token).map(this::convertToDto);
    }

    @Override
    public boolean refreshToken(FmkToken token) {
        if (token == null) {
            return false;
        }

        // 👉 调用 WrapMapper 的业务方法
        return loginInfoTokenWrapMapper.refreshTokenExpireTime(token);
    }

    @Override
    public boolean revokeToken(FmkToken token) {
        if (token == null) {
            return false;
        }

        // 👉 调用 WrapMapper 的业务方法
        return loginInfoTokenWrapMapper.revokeTokenByToken(token);
    }

    @Override
    public int revokeAllUserTokens(FmkUserId userId) {
        if (userId == null) {
            return 0;
        }

        // 👉 调用 WrapMapper 的业务方法
        return loginInfoTokenWrapMapper.revokeAllTokensByUserId(userId);
    }

    @Override
    public int cleanExpiredTokens() {
        // 👉 调用 WrapMapper 的业务方法，清理过期Token
        int updated = loginInfoTokenWrapMapper.cleanExpiredTokens();

        log.info("FmkTokenServiceDbImpl|cleanExpiredTokens|清理过期Token完成|count={}", updated);

        return updated;
    }

    // ==================== 私有辅助方法 ====================

    /**
     * 计算过期时间
     */
    private Instant calculateExpireTime() {
        return FmkInstantUtil.now().plus(12, ChronoUnit.HOURS);
    }

    /**
     * 刷新最后活跃时间（异步执行，避免影响性能）
     */
    private void refreshTokenLastActiveTime(FmkToken token) {
        // TODO: 使用异步方式更新，或者使用 Redis 缓存最后活跃时间
        // CompletableFuture.runAsync(() -> { ... });

        loginInfoTokenWrapMapper.updateLastActiveTime(token);
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

    /**
     * 转换为 DTO
     */
    private FmkTokenInfo convertToDto(LoginInfoToken entity) {
        return FmkTokenInfo.builder()
                .token(entity.getToken())
                .userId(entity.getUserId())
                .username(entity.getUsername())
                .clientType(entity.getClientType())
                .ipAddress(entity.getIpAddress())
                .userAgent(entity.getUserAgent())
                .expireTime(entity.getExpireTime())
                .status(entity.getStatus())
                .remark(entity.getRemark())
                .build();
    }
}