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

}
