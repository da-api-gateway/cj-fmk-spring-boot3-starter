// package com.cjlabs.db.token;
//
// import com.cjlabs.core.time.FmkInstantUtil;
// import com.cjlabs.core.types.longs.FmkUserId;
// import com.cjlabs.core.types.strings.FmkToken;
// import com.cjlabs.web.json.FmkJacksonUtil;
// import com.cjlabs.web.threadlocal.FmkUserInfo;
//
// import lombok.extern.slf4j.Slf4j;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
// import org.springframework.data.redis.core.RedisTemplate;
// import org.springframework.stereotype.Service;
//
// import java.time.Instant;
// import java.time.temporal.ChronoUnit;
// import java.util.Optional;
// import java.util.Set;
// import java.util.UUID;
// import java.util.concurrent.TimeUnit;
//
// /**
//  * Token 服务 Redis 实现
//  * 使用 Redis 存储 Token，性能最优，推荐生产环境使用
//  */
// @Slf4j
// @Service("fmkTokenService")
// @ConditionalOnProperty(name = "fmk.token.storage", havingValue = "redis", matchIfMissing = true)
// public class FmkTokenServiceRedisImpl implements IFmkTokenService {
//
//     @Autowired
//     private RedisTemplate<String, String> redisTemplate;
//
//     /**
//      * Redis Key 前缀
//      */
//     private static final String TOKEN_PREFIX = "fmk:token:";
//     private static final String USER_TOKEN_PREFIX = "fmk:user:tokens:";
//
//     /**
//      * 默认Token过期时间：7天
//      */
//     private static final long DEFAULT_EXPIRE_DAYS = 7;
//
//     @Override
//     public FmkToken createAndSaveToken(FmkUserId userId, FmkUserInfo userInfo, FmkTokenInfo tokenInfo) {
//         try {
//             // 生成 Token
//             FmkToken token = generateToken();
//             tokenInfo.setToken(token);
//             tokenInfo.setUserId(userId);
//             tokenInfo.setExpireTime(calculateExpireTime());
//
//             // 存储到 Redis
//             String tokenKey = TOKEN_PREFIX + token.getValue();
//             String tokenJson = FmkJacksonUtil.toJson(tokenInfo);
//
//             redisTemplate.opsForValue().set(
//                     tokenKey,
//                     tokenJson,
//                     DEFAULT_EXPIRE_DAYS,
//                     TimeUnit.DAYS
//             );
//
//             // 建立用户ID到Token的映射（用于批量撤销）
//             String userTokenKey = USER_TOKEN_PREFIX + userId.getValue();
//             redisTemplate.opsForSet().add(userTokenKey, token.getValue());
//             redisTemplate.expire(userTokenKey, DEFAULT_EXPIRE_DAYS, TimeUnit.DAYS);
//
//             log.info("FmkTokenServiceRedisImpl|createAndStoreToken|Token创建成功|userId={}",
//                     userId.getValue());
//
//             return token;
//         } catch (Exception e) {
//             log.error("FmkTokenServiceRedisImpl|createAndStoreToken|Token创建失败", e);
//             throw new RuntimeException("创建Token失败", e);
//         }
//     }
//
//     @Override
//     public FmkToken createAndSaveToken(FmkUserId userId, FmkUserInfo userInfo, FmkTokenInfo tokenInfo) {
//         return null;
//     }
//
//     @Override
//     public boolean validateToken(FmkToken token) {
//         if (token == null) {
//             return false;
//         }
//
//         String tokenKey = TOKEN_PREFIX + token.getValue();
//         return Boolean.TRUE.equals(redisTemplate.hasKey(tokenKey));
//     }
//
//     @Override
//     public Optional<FmkUserId> getUserIdByToken(FmkToken token) {
//         return getTokenInfo(token).map(FmkTokenInfo::getUserId);
//     }
//
//     @Override
//     public Optional<FmkUserInfo> getUserInfoByToken(FmkToken token) {
//         // 从其他服务获取用户信息，这里只返回 userId
//         return getUserIdByToken(token).flatMap(userId -> {
//             // TODO: 调用 UserService 获取完整用户信息
//             return Optional.empty();
//         });
//     }
//
//     @Override
//     public Optional<FmkTokenInfo> getTokenInfo(FmkToken token) {
//         if (token == null) {
//             return Optional.empty();
//         }
//
//         try {
//             String tokenKey = TOKEN_PREFIX + token.getValue();
//             String tokenJson = redisTemplate.opsForValue().get(tokenKey);
//
//             if (tokenJson == null) {
//                 return Optional.empty();
//             }
//
//             FmkTokenInfo tokenInfo = FmkJacksonUtil.toObject(tokenJson, FmkTokenInfo.class);
//             return Optional.ofNullable(tokenInfo);
//         } catch (Exception e) {
//             log.error("FmkTokenServiceRedisImpl|getTokenInfo|获取Token信息失败", e);
//             return Optional.empty();
//         }
//     }
//
//     @Override
//     public boolean refreshToken(FmkToken token) {
//         if (token == null) {
//             return false;
//         }
//
//         String tokenKey = TOKEN_PREFIX + token.getValue();
//         return Boolean.TRUE.equals(redisTemplate.expire(tokenKey, DEFAULT_EXPIRE_DAYS, TimeUnit.DAYS));
//     }
//
//     @Override
//     public boolean revokeToken(FmkToken token) {
//         if (token == null) {
//             return false;
//         }
//
//         String tokenKey = TOKEN_PREFIX + token.getValue();
//         return Boolean.TRUE.equals(redisTemplate.delete(tokenKey));
//     }
//
//     @Override
//     public int revokeAllUserTokens(FmkUserId userId) {
//         if (userId == null) {
//             return 0;
//         }
//
//         try {
//             String userTokenKey = USER_TOKEN_PREFIX + userId.getValue();
//             Set<String> tokens = redisTemplate.opsForSet().members(userTokenKey);
//
//             if (tokens == null || tokens.isEmpty()) {
//                 return 0;
//             }
//
//             int count = 0;
//             for (String tokenValue : tokens) {
//                 String tokenKey = TOKEN_PREFIX + tokenValue;
//                 if (Boolean.TRUE.equals(redisTemplate.delete(tokenKey))) {
//                     count++;
//                 }
//             }
//
//             // 清理用户Token集合
//             redisTemplate.delete(userTokenKey);
//
//             log.info("FmkTokenServiceRedisImpl|revokeAllUserTokens|撤销用户所有Token|userId={}|count={}",
//                     userId.getValue(), count);
//
//             return count;
//         } catch (Exception e) {
//             log.error("FmkTokenServiceRedisImpl|revokeAllUserTokens|撤销失败", e);
//             return 0;
//         }
//     }
//
//     @Override
//     public int cleanExpiredTokens() {
//         // Redis 自动过期，不需要手动清理
//         log.info("FmkTokenServiceRedisImpl|cleanExpiredTokens|Redis自动过期，无需清理");
//         return 0;
//     }
//
//     // ==================== 私有辅助方法 ====================
//
//     private FmkToken generateToken() {
//         String tokenValue = UUID.randomUUID().toString().replace("-", "") +
//                 UUID.randomUUID().toString().replace("-", "");
//         return FmkToken.of(tokenValue);
//     }
//
//     private Instant calculateExpireTime() {
//         return FmkInstantUtil.now().plus(DEFAULT_EXPIRE_DAYS, ChronoUnit.DAYS);
//     }
// }