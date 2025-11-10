package com.cjlabs.db.enums;

import com.cjlabs.domain.enums.IEnumStr;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 批量操作类型枚举
 */
@Getter
@RequiredArgsConstructor
public enum BatchOperationTypeEnum implements IEnumStr {
    /**
     * 插入操作
     */
    INSERT("INSERT", "插入"),

    /**
     * 更新操作
     */
    UPDATE("UPDATE", "更新"),

    ;

    private final String code;

    private final String msg;
}