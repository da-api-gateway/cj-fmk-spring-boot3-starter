package com.cjlabs.db.enums;

import com.cjlabs.domain.enums.IEnumStr;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 数据库字段名枚举
 */
@Getter
@RequiredArgsConstructor
public enum DbFieldNameEnum implements IEnumStr {
    /**
     * 删除标识字段（下划线）
     */
    DEL_FLAG("del_flag", "delFlag"),

    /**
     * 创建日期
     */
    CREATE_DATE("create_date", "createDate"),

    /**
     * 更新日期
     */
    UPDATE_DATE("update_date", "updateDate"),

    /**
     * ID
     */
    ID("id", "id"),

    /**
     * 名称
     */
    NAME("name", "name"),

    /**
     * 状态
     */
    STATUS("status", "status"),

    /**
     * 用户ID
     */
    USER_ID("user_id", "userId"),

    /**
     * 用户名
     */
    USERNAME("username", "username"),

    ;


    private final String code;

    private final String msg;

}