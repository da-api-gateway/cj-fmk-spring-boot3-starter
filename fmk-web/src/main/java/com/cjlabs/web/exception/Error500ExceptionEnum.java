package com.cjlabs.web.exception;

import com.cjlabs.domain.enums.IEnumStrException;

import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.cjlabs.web.exception.Error500Exception.ERROR_500_TYPE_STR;

/**
 * 系统异常枚举（System Exception Enum）
 * 对应 multi_language_message 表中的 SYSTEM_ERROR 类型
 * type 对应 message_type，key 对应 message_key
 */
@Getter
@AllArgsConstructor
public enum Error500ExceptionEnum implements IEnumStrException {

    // ===================== 系统异常 =====================
    /**
     * 系统未知错误 (Unknown system error)
     */
    UNKNOWN_ERROR(ERROR_500_TYPE_STR, "UNKNOWN_ERROR"),

    /**
     * 服务暂时不可用 (Service temporarily unavailable)
     */
    SERVICE_UNAVAILABLE(ERROR_500_TYPE_STR, "SERVICE_UNAVAILABLE"),

    /**
     * 请求超时 (Request timeout)
     */
    TIMEOUT(ERROR_500_TYPE_STR, "TIMEOUT");

    /**
     * 对应 message_type
     */
    private final String type;

    /**
     * 对应 message_key
     */
    private final String key;
}
