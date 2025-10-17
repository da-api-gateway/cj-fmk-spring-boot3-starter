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

}
