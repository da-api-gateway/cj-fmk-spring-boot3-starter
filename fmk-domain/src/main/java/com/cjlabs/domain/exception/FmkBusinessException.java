package com.cjlabs.domain.exception;

public class FmkBusinessException extends RuntimeException {

    private final String code;
    private final String message;

    public FmkBusinessException(String code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
