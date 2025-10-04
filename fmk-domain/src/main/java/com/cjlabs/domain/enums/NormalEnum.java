package com.cjlabs.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum NormalEnum implements IEnumStr {
    NORMAL("NORMAL", "正常"),
    ABNORMAL("ABNORMAL", "不正常"),

    ;

    private final String code;
    private final String msg;

}
