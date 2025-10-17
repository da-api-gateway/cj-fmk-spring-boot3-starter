package com.cjlabs.web.exception;

import com.cjlabs.domain.enums.IEnumStrException;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ExceptionEnum implements IEnumStrException {

    // code = type
    // msg = key

    SYSTEM_EXCEPTION("EXCEPTION", "SYSTEM_EXCEPTION"),



    ;

    private final String type;
    private final String key;
}
