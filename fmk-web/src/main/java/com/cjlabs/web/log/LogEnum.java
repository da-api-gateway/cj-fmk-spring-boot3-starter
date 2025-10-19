package com.cjlabs.web.log;

import com.cjlabs.domain.enums.IEnumStr;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum LogEnum implements IEnumStr {

    ;

    private final String code;
    private final String msg;
}
