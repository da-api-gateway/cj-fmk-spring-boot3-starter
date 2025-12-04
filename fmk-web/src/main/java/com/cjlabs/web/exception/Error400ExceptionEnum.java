package com.cjlabs.web.exception;

import com.cjlabs.domain.enums.IEnumStrException;

import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.cjlabs.web.exception.Error400Exception.ERROR_400_TYPE_STR;

/**
 * 参数校验异常枚举（Validation Exception Enum）
 * 对应 multi_language_message 表中的 VALIDATION_ERROR 类型
 * type 对应 message_type，key 对应 message_key
 */
@Getter
@AllArgsConstructor
public enum Error400ExceptionEnum implements IEnumStrException {

    // ===================== 参数校验异常 =====================

    /**
     * 未授权，请登录 (Unauthorized, please log in)
     */
    UNAUTHORIZED(ERROR_400_TYPE_STR, "UNAUTHORIZED"),

    /**
     * 登录凭证已过期，请重新登录 (Token expired, please log in again)
     */
    TOKEN_EXPIRED(ERROR_400_TYPE_STR, "TOKEN_EXPIRED"),

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
