package com.cjlabs.web.exception;

public interface ExceptionDbInterface {

    interface AUTHENTICATION_USER {
        // 权限验证相关的
        String AUTHENTICATION_USER = "AUTHENTICATION_USER";

        interface MsgValue {
            String ACCOUNT_LOCKED = "ACCOUNT_LOCKED";
            String TOKEN_EXPIRED = "TOKEN_EXPIRED";
        }
    }
}
