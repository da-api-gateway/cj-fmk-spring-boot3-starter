package com.cjlabs.web.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class Error400Exception extends BaseException {
    public static final String ERROR_400_TYPE_STR = "ERROR_400";

    private String msgType;
    private String msgKey;

    // 默认构造函数
    public Error400Exception(String msgKey) {
        super(ERROR_400_TYPE_STR, msgKey, HttpStatus.BAD_REQUEST.value());
    }

    // 默认构造函数
    public Error400Exception(Error400ExceptionEnum exceptionEnum) {
        super(ERROR_400_TYPE_STR, exceptionEnum.getKey(), HttpStatus.BAD_REQUEST.value());
    }

    /**
     * 使用验证异常枚举和详细消息创建验证异常
     *
     * @param exceptionEnum 验证异常枚举
     * @param message       详细错误消息
     */
    public Error400Exception(Error400ExceptionEnum exceptionEnum, String message) {
        super(ERROR_400_TYPE_STR, exceptionEnum.getKey(), HttpStatus.BAD_REQUEST.value(), message);
    }
}
