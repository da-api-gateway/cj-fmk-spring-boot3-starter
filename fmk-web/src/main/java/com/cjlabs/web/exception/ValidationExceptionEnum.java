package com.cjlabs.web.exception;

import com.cjlabs.domain.enums.IEnumStrException;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 参数校验异常枚举（Validation Exception Enum）
 * 对应 multi_language_message 表中的 VALIDATION_ERROR 类型
 * type 对应 message_type，key 对应 message_key
 */
@Getter
@AllArgsConstructor
public enum ValidationExceptionEnum implements IEnumStrException {

    // ===================== 参数校验异常 =====================
    /**
     * 请求参数无效 (Invalid request parameter)
     */
    INVALID_PARAMETER("VALIDATION_ERROR", "INVALID_PARAMETER"),

    /**
     * 缺少必要字段 (Required field is missing)
     */
    MISSING_FIELD("VALIDATION_ERROR", "MISSING_FIELD");

    /**
     * 对应 message_type
     */
    private final String type;

    /**
     * 对应 message_key
     */
    private final String key;
}
