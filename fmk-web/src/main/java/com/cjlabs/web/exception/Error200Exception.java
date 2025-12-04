package com.cjlabs.web.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class Error200Exception extends BaseException {

    public static final String ERROR_200_TYPE_STR = "ERROR_200";

    private String msgType;
    private String msgKey;

    // 默认构造函数
    public Error200Exception(String msgKey) {
        super(ERROR_200_TYPE_STR, msgKey, HttpStatus.OK.value());
    }

    public Error200Exception(Error200ExceptionEnum error200ExceptionEnum) {
        super(ERROR_200_TYPE_STR, error200ExceptionEnum.getKey(), HttpStatus.OK.value());
    }

    /**
     * 使用业务异常枚举和详细消息创建业务异常
     *
     * @param error200ExceptionEnum 业务异常枚举
     * @param message               详细错误消息
     */
    public Error200Exception(Error200ExceptionEnum error200ExceptionEnum, String message) {
        super(ERROR_200_TYPE_STR, error200ExceptionEnum.getKey(), HttpStatus.OK.value(), message);
    }
}
