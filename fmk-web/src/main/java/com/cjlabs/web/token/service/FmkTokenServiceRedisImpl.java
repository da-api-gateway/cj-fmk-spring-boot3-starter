// package com.cjlabs.web.token.service;
//
// import com.cjlabs.core.types.longs.FmkUserId;
// import com.cjlabs.core.types.strings.FmkToken;
// import com.cjlabs.web.threadlocal.FmkClientInfo;
// import com.cjlabs.web.threadlocal.FmkUserInfo;
// import com.cjlabs.web.token.FmkTokenProperties;
// import com.cjlabs.web.token.IFmkTokenService;
// import com.cjlabs.web.token.bo.FmkTokenInfo;
//
// import lombok.extern.slf4j.Slf4j;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
// import org.springframework.stereotype.Service;
//
// import java.util.Optional;
//
// /**
//  * Token 服务 Redis 实现
//  * 使用 Redis 存储 Token，性能最优，推荐生产环境使用
//  * 当 fmk.token.type=redis 时启用
//  */
// @Slf4j
// @Service("fmkTokenServiceRedis")
// @ConditionalOnProperty(name = "fmk.token.type", havingValue = "redis")
// public class FmkTokenServiceRedisImpl implements IFmkTokenService {
//
//     @Autowired
//     private FmkTokenProperties tokenProperties;
//
//
//     @Override
//     public FmkToken createAndSaveToken(FmkUserInfo userInfo, FmkClientInfo clientInfo) {
//         return null;
//     }
//
//     @Override
//     public boolean validateToken(FmkToken token) {
//         return false;
//     }
//
//     @Override
//     public Optional<FmkToken> getTokenByUserId(FmkUserId userId) {
//         return Optional.empty();
//     }
//
//     @Override
//     public Optional<FmkUserId> getUserIdByToken(FmkToken token) {
//         return Optional.empty();
//     }
//
//     @Override
//     public Optional<FmkUserInfo> getUserInfoByToken(FmkToken token) {
//         return Optional.empty();
//     }
//
//     @Override
//     public Optional<FmkClientInfo> getClientInfoByToken(FmkToken token) {
//         return Optional.empty();
//     }
//
//     @Override
//     public Optional<FmkUserInfo> getUserInfoByUserId(FmkUserId userId) {
//         return Optional.empty();
//     }
//
//     @Override
//     public Optional<FmkTokenInfo> getTokenInfoByToken(FmkToken token) {
//         return Optional.empty();
//     }
//
//     @Override
//     public boolean refreshToken(FmkToken token) {
//         return false;
//     }
//
//     @Override
//     public boolean revokeToken(FmkToken token) {
//         return false;
//     }
//
//     @Override
//     public void revokeAllUserTokens(FmkUserId userId) {
//
//     }
//
//     @Override
//     public void cleanExpiredTokens() {
//
//     }
// }