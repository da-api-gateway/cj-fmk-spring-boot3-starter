// package com.cjlabs.db.token;
//
// import com.cjlabs.core.types.longs.FmkUserId;
// import com.cjlabs.core.types.strings.FmkToken;
// import com.cjlabs.domain.enums.ClientTypeEnum;
// import com.cjlabs.web.threadlocal.FmkUserInfo;
//
// import lombok.extern.slf4j.Slf4j;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
// import org.springframework.stereotype.Service;
//
// import java.util.Map;
// import java.util.Optional;
// import java.util.concurrent.ConcurrentHashMap;
//
// /**
//  * Token 服务内存实现
//  * 使用内存存储 Token，适合开发环境和单机环境
//  * 当 fmk.token.save=memory 时启用（默认）
//  */
// @Slf4j
// @Service("fmkTokenService")
// @ConditionalOnProperty(name = "fmk.token.save", havingValue = "memory", matchIfMissing = true)
// public class FmkTokenServiceMemoryImpl implements IFmkTokenService {
//
//     @Autowired
//     private FmkTokenProperties tokenProperties;  // 🔥 注入配置
//
//     // Token到用户ID的映射
//     private static final Map<FmkToken, FmkUserId> TOKEN_INFO_USER_ID_MAP = new ConcurrentHashMap<>();
//
//     // Token到设备信息的映射
//     private static final Map<FmkToken, FmkClientInfo> TOKEN_INFO_CLIENT_TYPE_MAP = new ConcurrentHashMap<>();
//
//     // 用户ID到用户信息的映射
//     private static final Map<FmkUserId, FmkUserInfo> USER_ID_USER_INFO_MAP = new ConcurrentHashMap<>();
//
//     /**
//      * 存储用户登录信息
//      *
//      * @param userId     用户ID
//      * @param userInfo   用户信息
//      * @param token      Token
//      * @param clientInfo 设备信息
//      */
//     public void storeUserLogin(FmkUserId userId,
//                                FmkUserInfo userInfo,
//                                FmkToken token,
//                                FmkClientInfo clientInfo) {
//         if (userId == null || token == null) {
//             log.warn("FmkTokenServiceMemoryImpl|storeUserLogin|参数无效|userId={}|token={}", userId, token);
//             return;
//         }
//
//         try {
//             // 检查缓存大小限制
//             int maxSize = tokenProperties.getMemory().getMaxSize();
//             if (TOKEN_INFO_USER_ID_MAP.size() >= maxSize) {
//                 log.warn("FmkTokenServiceMemoryImpl|storeUserLogin|缓存已满|maxSize={}|currentSize={}",
//                         maxSize, TOKEN_INFO_USER_ID_MAP.size());
//                 // TODO: 实现LRU淘汰策略
//             }
//
//             // 1. 存储Token到用户ID的映射
//             TOKEN_INFO_USER_ID_MAP.put(token, userId);
//
//             // 2. 存储Token到设备信息的映射
//             if (clientInfo != null) {
//                 TOKEN_INFO_CLIENT_TYPE_MAP.put(token, clientInfo);
//             }
//
//             // 3. 缓存用户信息
//             if (userInfo != null) {
//                 USER_ID_USER_INFO_MAP.put(userId, userInfo);
//             }
//
//             log.info("FmkTokenServiceMemoryImpl|storeUserLogin|存储成功|userId={}|token={}|maxSize={}",
//                     userId.getValue(), maskToken(token.getValue()), maxSize);
//         } catch (Exception e) {
//             log.error("FmkTokenServiceMemoryImpl|storeUserLogin|存储失败", e);
//         }
//     }
//
//     /**
//      * 根据Token获取用户ID
//      */
//     public Optional<FmkUserId> getUserIdByToken(FmkToken token) {
//         if (token == null) {
//             return Optional.empty();
//         }
//
//         return Optional.ofNullable(TOKEN_INFO_USER_ID_MAP.get(token));
//     }
//
//     /**
//      * 根据Token获取设备信息
//      */
//     public Optional<FmkClientInfo> getDeviceInfoByToken(FmkToken token) {
//         if (token == null) {
//             return Optional.empty();
//         }
//
//         FmkClientInfo clientInfo = TOKEN_INFO_CLIENT_TYPE_MAP.get(token);
//         if (clientInfo != null) {
//             // 更新最后活跃时间
//             clientInfo.updateLastActiveTime();
//         }
//         return Optional.ofNullable(clientInfo);
//     }
//
//     /**
//      * 根据Token获取用户信息
//      */
//     public Optional<FmkUserInfo> getUserInfoByToken(FmkToken token) {
//         if (token == null) {
//             log.info("FmkTokenServiceMemoryImpl|getUserInfoByToken|token为空");
//             return Optional.empty();
//         }
//
//         try {
//             // 1. 获取用户ID
//             FmkUserId userId = TOKEN_INFO_USER_ID_MAP.get(token);
//             if (userId == null) {
//                 log.info("FmkTokenServiceMemoryImpl|getUserInfoByToken|未找到用户ID|token={}", maskToken(token.getValue()));
//                 return Optional.empty();
//             }
//
//             // 2. 获取用户信息
//             FmkUserInfo userInfo = USER_ID_USER_INFO_MAP.get(userId);
//             if (userInfo == null) {
//                 log.info("FmkTokenServiceMemoryImpl|getUserInfoByToken|未找到用户信息|userId={}", userId.getValue());
//                 return Optional.empty();
//             }
//
//             // 3. 更新设备最后活跃时间
//             updateClientLastActiveTime(token);
//
//             return Optional.of(userInfo);
//         } catch (Exception e) {
//             log.error("FmkTokenServiceMemoryImpl|getUserInfoByToken|获取用户信息异常|token={}", maskToken(token.getValue()), e);
//             return Optional.empty();
//         }
//     }
//
//     /**
//      * 缓存用户信息
//      */
//     public void cacheUserInfo(FmkUserId userId, FmkUserInfo userInfo) {
//         if (userId == null || userInfo == null) {
//             return;
//         }
//
//         USER_ID_USER_INFO_MAP.put(userId, userInfo);
//         log.info("FmkTokenServiceMemoryImpl|cacheUserInfo|缓存用户信息|userId={}", userId.getValue());
//     }
//
//     /**
//      * 根据用户ID获取缓存的用户信息
//      */
//     public Optional<FmkUserInfo> getCachedUserInfo(FmkUserId userId) {
//         if (userId == null) {
//             return Optional.empty();
//         }
//
//         FmkUserInfo userInfo = USER_ID_USER_INFO_MAP.get(userId);
//         return Optional.ofNullable(userInfo);
//     }
//
//     /**
//      * 验证Token是否有效
//      */
//     public boolean validateToken(FmkToken token) {
//         if (token == null) {
//             return false;
//         }
//
//         // 检查Token是否存在且对应用户ID
//         FmkUserId userId = TOKEN_INFO_USER_ID_MAP.get(token);
//         if (userId == null) {
//             return false;
//         }
//
//         // 更新设备最后活跃时间
//         updateClientLastActiveTime(token);
//
//         return true;
//     }
//
//     /**
//      * 移除用户所有Token
//      */
//     public void removeAllUserTokens(FmkUserId userId) {
//         if (userId == null) {
//             return;
//         }
//
//         // 1. 找出该用户的所有Token
//         TOKEN_INFO_USER_ID_MAP.entrySet().removeIf(entry -> {
//             if (userId.equals(entry.getValue())) {
//                 FmkToken token = entry.getKey();
//                 // 2. 从设备信息映射中移除
//                 TOKEN_INFO_CLIENT_TYPE_MAP.remove(token);
//                 return true;
//             }
//             return false;
//         });
//
//         // 3. 移除用户信息缓存
//         USER_ID_USER_INFO_MAP.remove(userId);
//
//         log.info("FmkTokenServiceMemoryImpl|removeAllUserTokens|移除用户所有Token|userId={}", userId.getValue());
//     }
//
//     /**
//      * 移除特定Token
//      */
//     public void removeToken(FmkToken token) {
//         if (token == null) {
//             return;
//         }
//
//         // 1. 从用户ID映射中移除
//         TOKEN_INFO_USER_ID_MAP.remove(token);
//
//         // 2. 从设备信息映射中移除
//         TOKEN_INFO_CLIENT_TYPE_MAP.remove(token);
//
//         log.info("FmkTokenServiceMemoryImpl|removeToken|移除Token|token={}", maskToken(token.getValue()));
//     }
//
//     /**
//      * 更新设备最后活跃时间
//      */
//     private void updateClientLastActiveTime(FmkToken token) {
//         FmkClientInfo clientInfo = TOKEN_INFO_CLIENT_TYPE_MAP.get(token);
//         if (clientInfo != null) {
//             clientInfo.updateLastActiveTime();
//         }
//     }
//
//     /**
//      * Token 掩码处理
//      */
//     private String maskToken(String token) {
//         if (token == null || token.length() <= 10) {
//             return "***";
//         }
//         return token.substring(0, 10) + "...";
//     }
//
//     // ==================== IFmkTokenService 接口实现（需要补充） ====================
//
//     @Override
//     public FmkToken createAndSaveToken(FmkUserId userId, FmkUserInfo userInfo, FmkTokenInfo tokenInfo) {
//         FmkToken token = FmkToken.generate();
//
//         // 🔥 使用构造函数代替 of() 方法
//         FmkClientInfo clientInfo = new FmkClientInfo();
//         ClientTypeEnum clientType = tokenInfo.getClientType();
//         clientInfo.setClientType(clientType);
//         clientInfo.setIpAddress(tokenInfo.getIpAddress());
//         clientInfo.setUserAgent(tokenInfo.getUserAgent());
//
//         storeUserLogin(userId, userInfo, token, clientInfo);
//         return token;
//     }
//
//     @Override
//     public Optional<FmkTokenInfo> getTokenInfo(FmkToken token) {
//         // 内存实现暂不支持完整的TokenInfo，只返回基本信息
//         return getUserIdByToken(token)
//                 .map(userId -> FmkTokenInfo.builder()
//                         .token(token)
//                         .userId(userId)
//                         .build());
//     }
//
//     @Override
//     public boolean refreshToken(FmkToken token) {
//         // 内存实现暂不支持刷新过期时间
//         return validateToken(token);
//     }
//
//     @Override
//     public boolean revokeToken(FmkToken token) {
//         removeToken(token);
//         return true;
//     }
//
//     @Override
//     public int revokeAllUserTokens(FmkUserId userId) {
//         removeAllUserTokens(userId);
//         return 1; // 简化处理，返回1表示成功
//     }
//
//     @Override
//     public int cleanExpiredTokens() {
//         // 内存实现暂不支持自动清理过期Token
//         log.info("FmkTokenServiceMemoryImpl|cleanExpiredTokens|内存实现暂不支持自动清理");
//         return 0;
//     }
// }