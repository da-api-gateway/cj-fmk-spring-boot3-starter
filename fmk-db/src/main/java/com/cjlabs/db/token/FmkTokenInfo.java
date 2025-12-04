//package com.cjlabs.db.token;
//
//import com.cjlabs.core.types.longs.FmkUserId;
//import com.cjlabs.core.types.strings.FmkToken;
//import com.cjlabs.db.token.enums.TokenStatusEnum;
//import com.cjlabs.domain.enums.ClientTypeEnum;
//import lombok.Builder;
//import lombok.Data;
//
//import java.time.Instant;
//
///**
// * Token 信息 DTO
// */
//@Data
//@Builder
//public class FmkTokenInfo {
//    /**
//     * Token 值
//     */
//    private FmkToken token;
//
//    /**
//     * 用户ID
//     */
//    private FmkUserId userId;
//
//    /**
//     * 用户名快照
//     */
//    private String username;
//
//    /**
//     * 客户端类型
//     */
//    private ClientTypeEnum clientType;
//
//    /**
//     * IP 地址
//     */
//    private String ipAddress;
//
//    /**
//     * User-Agent
//     */
//    private String userAgent;
//
//    /**
//     * 过期时间
//     */
//    private Instant expireTime;
//
//    /**
//     * Token 状态
//     */
//    private TokenStatusEnum status;
//
//    /**
//     * 最后活跃时间
//     */
//    private Instant lastActiveTime;
//
//    /**
//     * 备注
//     */
//    private String remark;
//}