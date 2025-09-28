package com.cjlabs.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum LanguageEnum implements IEnumStr {
    ZH("ZH", "中文"),
    EN("EN", "英文"),

    ;

    private final String code;
    private final String msg;

}
