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
}
