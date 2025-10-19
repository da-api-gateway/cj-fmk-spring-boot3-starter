package com.cjlabs.web.exception;

import com.cjlabs.domain.enums.IEnumStrException;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SystemExceptionEnum implements IEnumStrException {

    // code = type
    // msg = key

    UNKNOWN_ERROR("SYSTEM_ERROR", "UNKNOWN_ERROR"),



    ;

    private final String type;
    private final String key;
}
