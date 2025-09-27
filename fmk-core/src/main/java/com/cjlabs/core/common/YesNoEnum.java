package com.cjlabs.core.common;

import com.xodo.fmk.core.enums.IEnumStr;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum YesNoEnum implements IEnumStr {
    YES("YES", "是"),
    NO("NO", "否");

    private final String code;
    private final String msg;
}