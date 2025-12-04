//package com.cjlabs.db.token.mysql;
//
//import com.cjlabs.core.types.longs.FmkUserId;
//import com.cjlabs.core.types.strings.FmkToken;
//import com.cjlabs.db.domain.FmkBaseEntity;
//import com.cjlabs.db.token.enums.TokenStatusEnum;
//import com.cjlabs.domain.enums.ClientTypeEnum;
//
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.EqualsAndHashCode;
//import lombok.NoArgsConstructor;
//
//import java.time.Instant;
//
///**
// * sys_user_info_token 管理员访问令牌表
// * <p>
// * 2025-12-03 04:34:19
// */
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@EqualsAndHashCode(callSuper = true)
//public class LoginInfoToken extends FmkBaseEntity {
//
//    /**
//     * 登录用户ID
//     */
//    private FmkUserId userId;
//
//    /**
//     * 用户名快照
//     */
//    private String username;
//
//    /**
//     * 访问令牌（建议存储哈希后的值）
//     */
//    private FmkToken token;
//
//    /**
//     * 客户端类型，例如 WEB / ADMIN / MOBILE
//     */
//    private ClientTypeEnum clientType;
//
//    /**
//     * 登录 IP（IPv4/IPv6）
//     */
//    private String ipAddress;
//
//    /**
//     * UA 信息快照
//     */
//    private String userAgent;
//
//    /**
//     * 令牌过期时间（UTC 毫秒）
//     */
//    private Instant expireTime;
//
//    /**
//     * 令牌状态
//     */
//    private TokenStatusEnum status;
//
//    /**
//     * 备注
//     */
//    private String remark;
//
//}