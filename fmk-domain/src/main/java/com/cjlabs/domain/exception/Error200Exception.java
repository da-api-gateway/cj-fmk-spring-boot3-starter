package com.cjlabs.domain.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Error200Exception extends BaseException {

    public static final String ERROR_200_TYPE_STR = "ERROR_200";

    private String msgType;
    private String msgKey;

    // 默认构造函数
    public Error200Exception(String msgKey) {
        super(ERROR_200_TYPE_STR, msgKey, 200);
    }

    public Error200Exception(Error200ExceptionEnum error200ExceptionEnum) {
        super(ERROR_200_TYPE_STR, error200ExceptionEnum.getKey(), 200);
    }

    /**
     * 使用业务异常枚举和详细消息创建业务异常
     *
     * @param error200ExceptionEnum 业务异常枚举
     * @param message               详细错误消息
     */
    public Error200Exception(Error200ExceptionEnum error200ExceptionEnum, String message) {
        super(ERROR_200_TYPE_STR, error200ExceptionEnum.getKey(), 200, message);
    }
}
