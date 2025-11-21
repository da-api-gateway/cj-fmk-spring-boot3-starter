package com.cjlabs.db.domain;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.cjlabs.db.enums.DbOrderEnum;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class FmkOrderItem implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 需要进行排序的字段
     */
    private String column;
    /**
     * 是否正序排列，默认 true
     */
    private DbOrderEnum orderFlag;

    public static FmkOrderItem asc(String column) {
        return build(column, DbOrderEnum.ASC);
    }

    public static FmkOrderItem desc(String column) {
        return build(column, DbOrderEnum.DESC);
    }

    public static List<FmkOrderItem> ascList(String... columns) {
        return Arrays.stream(columns).map(FmkOrderItem::asc).collect(Collectors.toList());
    }

    public static List<FmkOrderItem> descList(String... columns) {
        return Arrays.stream(columns).map(FmkOrderItem::desc).collect(Collectors.toList());
    }

    private static FmkOrderItem build(String column, DbOrderEnum orderFlag) {
        return new FmkOrderItem().setColumn(column).setOrderFlag(orderFlag);
    }

    public FmkOrderItem setColumn(String column) {
        this.column = StringUtils.replaceAllBlank(column);
        return this;
    }

    public FmkOrderItem setOrderFlag(DbOrderEnum orderFlag) {
        this.orderFlag = orderFlag;
        return this;
    }
}
