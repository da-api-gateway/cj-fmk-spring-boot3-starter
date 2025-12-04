package com.cjlabs.web.token.enums;

import com.cjlabs.domain.enums.IEnumStr;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TokenSaveTypeEnum implements IEnumStr {

    MEMORY("MEMORY", "内存"),
    REDIS("REDIS", "redis"),
    ;

    private final String code;
    private final String msg;

    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public String getMsg() {
        return this.msg;
    }
}