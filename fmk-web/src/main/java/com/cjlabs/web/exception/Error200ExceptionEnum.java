package com.cjlabs.web.exception;

import com.cjlabs.domain.enums.IEnumStrException;

import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.cjlabs.web.exception.Error200Exception.ERROR_200_TYPE_STR;

/**
 * 业务异常枚举（Business Exception Enum）
 * 对应 multi_language_message 表中的 BUSINESS_ERROR 类型
 * type 对应 message_type，key 对应 message_key
 */
@Getter
@AllArgsConstructor
public enum Error200ExceptionEnum implements IEnumStrException {

    // ===================== 认证与权限 =====================
    /**
     * 请求参数无效 (Invalid request parameter)
     */
    INVALID_PARAMETER(ERROR_200_TYPE_STR, "INVALID_PARAMETER"),

    /**
     * 缺少必要字段 (Required field is missing)
     */
    MISSING_FIELD(ERROR_200_TYPE_STR, "MISSING_FIELD"),

    /**
     * 账户已被锁定
     */
    ACCOUNT_LOCKED(ERROR_200_TYPE_STR, "ACCOUNT_LOCKED"),

    /**
     * 用户名或密码错误 (Invalid username or password)
     */
    INVALID_CREDENTIALS(ERROR_200_TYPE_STR, "INVALID_CREDENTIALS"),

    /**
     * 密码复杂度不够
     */
    PASSWORD_COMPLEXITY_REQUIREMENT(ERROR_200_TYPE_STR, "PASSWORD_COMPLEXITY_REQUIREMENT"),

    /**
     * 用户名复杂度不够
     */
    USERNAME_COMPLEXITY_REQUIREMENT(ERROR_200_TYPE_STR, "USERNAME_COMPLEXITY_REQUIREMENT"),

    // ===================== 业务操作 =====================
    /**
     * 数据未找到 (data not found)
     */
    DATA_NOT_FOUND(ERROR_200_TYPE_STR, "DATA_NOT_FOUND"),

    /**
     * 余额不足 (Insufficient balance)
     */
    INSUFFICIENT_BALANCE(ERROR_200_TYPE_STR, "INSUFFICIENT_BALANCE"),

    /**
     * 重复操作，请勿重复提交 (Duplicate operation, please do not resubmit)
     */
    DUPLICATE_OPERATION(ERROR_200_TYPE_STR, "DUPLICATE_OPERATION"),

    /**
     * 请求过于频繁，请稍后再试 (Too many requests, please try again later)
     */
    RATE_LIMIT_EXCEEDED(ERROR_200_TYPE_STR, "RATE_LIMIT_EXCEEDED"),

    // ===================== 网络与第三方服务 =====================
    /**
     * 连接失败 (Connection failed)
     */
    CONNECTION_FAILED(ERROR_200_TYPE_STR, "CONNECTION_FAILED"),

    /**
     * 第三方服务异常 (Third-party service error)
     */
    THIRD_PARTY_ERROR(ERROR_200_TYPE_STR, "THIRD_PARTY_ERROR"),

    ;

    /**
     * 对应 message_type
     */
    private final String type;

    /**
     * 对应 message_key
     */
    private final String key;
}
