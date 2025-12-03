package com.cjlabs.db.token;

import com.cjlabs.core.types.longs.FmkUserId;
import com.cjlabs.core.types.strings.FmkToken;
import com.cjlabs.web.threadlocal.FmkUserInfo;

import java.util.Optional;

/**
 * Token 服务接口
 * 定义了 Token 的核心操作，支持多种存储实现（内存、数据库、Redis）
 */
public interface IFmkTokenService {

    /**
     * 创建并存储 Token
     */
    FmkToken createAndSaveToken(FmkUserId userId, FmkUserInfo userInfo, FmkTokenInfo tokenInfo);

    /**
     * 验证 Token 是否有效
     */
    boolean validateToken(FmkToken token);

    /**
     * 根据 Token 获取用户ID
     */
    Optional<FmkUserId> getUserIdByToken(FmkToken token);

    /**
     * 根据 Token 获取完整 Token 信息
     */
    Optional<FmkTokenInfo> getTokenInfo(FmkToken token);

    /**
     * 刷新 Token 过期时间
     */
    boolean refreshToken(FmkToken token);

    /**
     * 撤销指定 Token
     */
    boolean revokeToken(FmkToken token);

    /**
     * 撤销用户的所有 Token（用于强制登出）
     */
    int revokeAllUserTokens(FmkUserId userId);

    /**
     * 清理过期的 Token（定时任务调用）
     */
    int cleanExpiredTokens();
}