package com.cjlabs.web.exception;

import com.cjlabs.domain.enums.IEnumStrException;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 业务异常枚举（Business Exception Enum）
 * 对应 multi_language_message 表中的 BUSINESS_ERROR 类型
 * type 对应 message_type，key 对应 message_key
 */
@Getter
@AllArgsConstructor
public enum BusinessExceptionEnum implements IEnumStrException {

    // ===================== 认证与权限 =====================
    /**
     * 未授权，请登录 (Unauthorized, please log in)
     */
    UNAUTHORIZED("BUSINESS_ERROR", "UNAUTHORIZED"),

    /**
     * 登录凭证已过期，请重新登录 (Token expired, please log in again)
     */
    TOKEN_EXPIRED("BUSINESS_ERROR", "TOKEN_EXPIRED"),

    /**
     * 用户名或密码错误 (Invalid username or password)
     */
    INVALID_CREDENTIALS("BUSINESS_ERROR", "INVALID_CREDENTIALS"),

    /**
     * 密码复杂度不够
     */
    PASSWORD_COMPLEXITY_REQUIREMENT("BUSINESS_ERROR", "PASSWORD_COMPLEXITY_REQUIREMENT"),

    // ===================== 业务操作 =====================
    /**
     * 数据未找到 (data not found)
     */
    DATA_NOT_FOUND("BUSINESS_ERROR", "DATA_NOT_FOUND"),

    /**
     * 余额不足 (Insufficient balance)
     */
    INSUFFICIENT_BALANCE("BUSINESS_ERROR", "INSUFFICIENT_BALANCE"),

    /**
     * 重复操作，请勿重复提交 (Duplicate operation, please do not resubmit)
     */
    DUPLICATE_OPERATION("BUSINESS_ERROR", "DUPLICATE_OPERATION"),

    /**
     * 请求过于频繁，请稍后再试 (Too many requests, please try again later)
     */
    RATE_LIMIT_EXCEEDED("BUSINESS_ERROR", "RATE_LIMIT_EXCEEDED"),

    // ===================== 网络与第三方服务 =====================
    /**
     * 连接失败 (Connection failed)
     */
    CONNECTION_FAILED("BUSINESS_ERROR", "CONNECTION_FAILED"),

    /**
     * 第三方服务异常 (Third-party service error)
     */
    THIRD_PARTY_ERROR("BUSINESS_ERROR", "THIRD_PARTY_ERROR");

    /**
     * 对应 message_type
     */
    private final String type;

    /**
     * 对应 message_key
     */
    private final String key;
}
