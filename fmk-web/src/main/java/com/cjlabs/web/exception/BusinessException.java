package com.cjlabs.web.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BusinessException extends BaseException {
    private String msgType;
    private String msgKey;

    // 默认构造函数
    public BusinessException(String msgKey) {
        super("BUSINESS_ERROR", msgKey, 200);
    }

    public BusinessException(BusinessExceptionEnum businessExceptionEnum) {
        super("BUSINESS_ERROR", businessExceptionEnum.getKey(), 200);
    }

      /**
     * 使用业务异常枚举和详细消息创建业务异常
     * 
     * @param businessExceptionEnum 业务异常枚举
     * @param message 详细错误消息
     */
    public BusinessException(BusinessExceptionEnum businessExceptionEnum, String message) {
        super("BUSINESS_ERROR", businessExceptionEnum.getKey(), 200, message);
    }
}
