package com.cjlabs.db.enums;

import com.cjlabs.domain.enums.IEnumStr;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * SQL 聚合函数结果字段枚举
 */
@Getter
@RequiredArgsConstructor
public enum DbAggregateEnum implements IEnumStr {
    /**
     * 统计数量字段
     */
    COUNT_VALUE("count_value", "countValue"),

    /**
     * 求和字段
     */
    SUM_VALUE("sum_value", "sumValue"),

    /**
     * ;
     * 平均值字段
     */
    AVG_VALUE("avg_value", "avgValue"),

    /**
     * 最大值字段
     */
    MAX_VALUE("max_value", "maxValue"),
    /**
     * 最小值字段
     */
    MIN_VALUE("min_value", "minValue"),


    ;

    private final String code;

    private final String msg;
}