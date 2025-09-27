package com.cjlabs.web.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DcxjCommonException extends RuntimeException {
    private String zhMsg;
    private String enMsg;

    // 默认构造函数
    public DcxjCommonException() {
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
    public DcxjCommonException(String zhMsg, String enMsg) {
        super();
        this.zhMsg = zhMsg;
        this.enMsg = enMsg;
    }

    // 带错误码和错误信息的构造函数
    public DcxjCommonException(MultiLanguageMessageExceptionEnum exceptionEnum) {
        super();
        this.zhMsg = exceptionEnum.getMsgZh();
        this.enMsg = exceptionEnum.getMsgEn();
    }

    // 带错误码、错误信息和原因的构造函数
    // public DcxjException(String msgType, String msgKey, Throwable cause) {
    //     super("", cause);
    //     this.msgType = msgType;
    //     this.msgKey = msgKey;
    // }

}
