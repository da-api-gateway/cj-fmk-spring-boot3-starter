package com.cjlabs.db.enums;

import com.cjlabs.domain.enums.IEnumStr;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * SQL 排序方向枚举
 */
@Getter
@RequiredArgsConstructor
public enum DbOrderEnum implements IEnumStr {
    /**
     * 升序
     */
    ASC("ASC"),
    
    /**
     * 降序
     */
    DESC("DESC");

    private final String code;
    private final String msg;
}