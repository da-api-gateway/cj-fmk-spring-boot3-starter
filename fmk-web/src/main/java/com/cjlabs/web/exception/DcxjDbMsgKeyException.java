package com.cjlabs.web.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DcxjDbMsgKeyException extends RuntimeException {
    private String msgType;
    private String msgKey;

    // 默认构造函数
    public DcxjDbMsgKeyException() {
        super();
    }

    // // 带错误信息的构造函数
    // public DcxjException(String message) {
    //     super(message);
    // }
    //
    // // 带错误信息和原因的构造函数
    // public DcxjException(String message, Throwable cause) {
    //     super(message, cause);
    // }

    // 带错误码和错误信息的构造函数
    public DcxjDbMsgKeyException(String msgType, String msgKey) {
        super();
        this.msgType = msgType;
        this.msgKey = msgKey;
    }

    // 带错误码和错误信息的构造函数
    public DcxjDbMsgKeyException(MultiLanguageMessageExceptionEnum exceptionEnum) {
        super();
        this.msgType = exceptionEnum.getType();
        this.msgKey = exceptionEnum.getKey();
    }

    // 带错误码、错误信息和原因的构造函数
    // public DcxjException(String msgType, String msgKey, Throwable cause) {
    //     super("", cause);
    //     this.msgType = msgType;
    //     this.msgKey = msgKey;
    // }

}
