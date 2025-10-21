package com.cjlabs.web.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SystemException extends BaseException {
    private String msgType;
    private String msgKey;

    // 默认构造函数
    public SystemException(String msgKey) {
        super("SYSTEM_ERROR", msgKey, 500);
    }

 /**
     * 使用系统异常枚举创建系统异常
     * 
     * @param exceptionEnum 系统异常枚举
     */
    public SystemException(SystemExceptionEnum exceptionEnum) {
        super("SYSTEM_ERROR", exceptionEnum.getKey(), 500);
    }

      /**
     * 使用系统异常枚举和详细消息创建系统异常
     * 
     * @param exceptionEnum 系统异常枚举
     * @param message 详细错误消息
     */
    public SystemException(SystemExceptionEnum exceptionEnum, String message) {
        super("SYSTEM_ERROR", exceptionEnum.getKey(), 500, message);
    }
}
