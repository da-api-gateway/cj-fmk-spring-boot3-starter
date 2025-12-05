package com.cjlabs.domain.exception;

import lombok.Getter;

/**
 * 应用内所有自定义异常的父类
 */
@Getter
public class BaseException extends RuntimeException {

    private final String errorType;   // 错误类型（SYSTEM_ERROR, BUSINESS_ERROR, VALIDATION_ERROR）
    private final String errorKey;    // 错误key（例如 ORDER_NOT_FOUND）
    private final Integer httpCode; // HTTP 状态码（200/400/500）

    public BaseException(String errorType, String errorKey, Integer httpCode) {
        super();
        this.errorType = errorType;
        this.errorKey = errorKey;
        this.httpCode = httpCode;
    }

  public BaseException(String errorType, String errorKey, Integer httpCode, String message) {
        super(message);
        this.errorType = errorType;
        this.errorKey = errorKey;
        this.httpCode = httpCode;
    }
}
