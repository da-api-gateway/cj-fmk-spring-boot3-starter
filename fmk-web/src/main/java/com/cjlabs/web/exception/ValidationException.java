package com.cjlabs.web.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ValidationException extends BaseException {
    private String msgType;
    private String msgKey;

    // 默认构造函数
    public ValidationException(String msgKey) {
        super("VALIDATION_ERROR", msgKey, 400);
    }

    // 默认构造函数
    public ValidationException(ValidationExceptionEnum exceptionEnum) {
        super("VALIDATION_ERROR", exceptionEnum.getKey(), 400);
    }

  /**
     * 使用验证异常枚举和详细消息创建验证异常
     * 
     * @param exceptionEnum 验证异常枚举
     * @param message 详细错误消息
     */
    public ValidationException(ValidationExceptionEnum exceptionEnum, String message) {
        super("VALIDATION_ERROR", exceptionEnum.getKey(), 400, message);
    }
}
