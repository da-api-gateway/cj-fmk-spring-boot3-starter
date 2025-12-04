package com.cjlabs.web.token;

import com.cjlabs.core.types.longs.FmkUserId;
import com.cjlabs.core.types.strings.FmkToken;
import com.cjlabs.web.threadlocal.FmkClientInfo;
import com.cjlabs.web.threadlocal.FmkUserInfo;
import com.cjlabs.web.token.bo.FmkTokenInfo;

import java.util.Optional;

/**
 * Token 服务接口
 * 定义了 Token 的核心操作，支持多种存储实现（内存、数据库、Redis）
 */
public interface IFmkTokenService {

    /**
     * 创建并存储 Token
     *
     * @param userInfo   用户信息
     * @param clientInfo 客户端信息
     * @return 生成的 Token
     */
    FmkToken createAndSaveToken(FmkUserInfo userInfo, FmkClientInfo clientInfo);

    /**
     * 验证 Token 是否有效
     */
    boolean validateToken(FmkToken token);

    Optional<FmkToken> getTokenByUserId(FmkUserId userId);

    /**
     * 根据 Token 获取用户ID
     */
    Optional<FmkUserId> getUserIdByToken(FmkToken token);

    /**
     * 根据 Token 获取用户信息（返回简单DTO）
     */
    Optional<FmkUserInfo> getUserInfoByToken(FmkToken token);

    /**
     * 根据 Token 获取设备信息
     */
    Optional<FmkClientInfo> getClientInfoByToken(FmkToken token);

    Optional<FmkUserInfo> getUserInfoByUserId(FmkUserId userId);

    /**
     * 根据 Token 获取完整 Token 信息
     */
    Optional<FmkTokenInfo> getTokenInfoByToken(FmkToken token);

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
    void revokeAllUserTokens(FmkUserId userId);

    /**
     * 清理过期的 Token（定时任务调用）
     */
    void cleanExpiredTokens();
}