package com.cjlabs.domain.exception;

import lombok.Getter;

public class FmkBusinessException extends RuntimeException {

    @Getter
    private final String code;
    private final String message;

    public FmkBusinessException(String code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
