package com.cjlabs.domain.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Error500Exception extends BaseException {
    public static final String ERROR_500_TYPE_STR = "ERROR_500";

    private String msgType;
    private String msgKey;

    // 默认构造函数
    public Error500Exception(String msgKey) {
        super(ERROR_500_TYPE_STR, msgKey, 500);
    }

    /**
     * 使用系统异常枚举创建系统异常
     *
     * @param exceptionEnum 系统异常枚举
     */
    public Error500Exception(Error500ExceptionEnum exceptionEnum) {
        super(ERROR_500_TYPE_STR, exceptionEnum.getKey(), 500);
    }

    /**
     * 使用系统异常枚举和详细消息创建系统异常
     *
     * @param exceptionEnum 系统异常枚举
     * @param message       详细错误消息
     */
    public Error500Exception(Error500ExceptionEnum exceptionEnum, String message) {
        super(ERROR_500_TYPE_STR, exceptionEnum.getKey(), 500, message);
    }
}
