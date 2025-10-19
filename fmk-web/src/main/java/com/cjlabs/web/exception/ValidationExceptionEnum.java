package com.cjlabs.web.exception;

import com.cjlabs.domain.enums.IEnumStrException;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ValidationExceptionEnum implements IEnumStrException {

    // code = type
    // msg = key

    INVALID_PARAMETER("VALIDATION_ERROR", "INVALID_PARAMETER"),



    ;

    private final String type;
    private final String key;
}
