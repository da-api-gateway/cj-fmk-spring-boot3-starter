package com.cjlabs.web.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class Error500Exception extends BaseException {
    public static final String ERROR_500_TYPE_STR = "ERROR_500";

    private String msgType;
    private String msgKey;

    // 默认构造函数
    public Error500Exception(String msgKey) {
        super(ERROR_500_TYPE_STR, msgKey, HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    /**
     * 使用系统异常枚举创建系统异常
     *
     * @param exceptionEnum 系统异常枚举
     */
    public Error500Exception(Error500ExceptionEnum exceptionEnum) {
        super(ERROR_500_TYPE_STR, exceptionEnum.getKey(), HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    /**
     * 使用系统异常枚举和详细消息创建系统异常
     *
     * @param exceptionEnum 系统异常枚举
     * @param message       详细错误消息
     */
    public Error500Exception(Error500ExceptionEnum exceptionEnum, String message) {
        super(ERROR_500_TYPE_STR, exceptionEnum.getKey(), HttpStatus.INTERNAL_SERVER_ERROR.value(), message);
    }
}
