package com.cjlabs.domain.enums;

import com.xodo.fmk.core.enums.IEnumStr;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EnableFlagEnum implements IEnumStr {
    ENABLED("ENABLED", "有效"),
    DISABLED("DISABLED", "无效"),

    ;

    private final String code;
    private final String msg;

}
