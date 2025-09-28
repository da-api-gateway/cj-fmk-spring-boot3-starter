package com.cjlabs.web.types.strings;

import com.cjlabs.web.types.base.BaseStringType;

import com.fasterxml.jackson.annotation.JsonCreator;
import org.apache.commons.lang3.StringUtils;

/**
 * 货币代码类型安全包装类
 */
public class FmkCurrencyCode extends BaseStringType<FmkCurrencyCode> {

    /**
     * 从字符串创建CurrencyCode，支持null值
     */
    public static FmkCurrencyCode ofNullable(String value) {
        return ofNullable(value, FmkCurrencyCode::new);
    }

    /**
     * 从Object值创建CurrencyCode，支持JSON反序列化
     */
    @JsonCreator
    public static FmkCurrencyCode ofNullable(Object value) {
        return ofNullableObject(value, FmkCurrencyCode::new, String.class);
    }

    /**
     * 从字符串创建CurrencyCode，不允许null或空值
     */
    public static FmkCurrencyCode of(String value) {
        if (StringUtils.isBlank(value)) {
            throw new IllegalArgumentException("CurrencyCode cannot be null or empty");
        }
        return new FmkCurrencyCode(value.trim().toUpperCase());
    }

    public FmkCurrencyCode(String value) {
        super(value == null ? null : value.toUpperCase());
        if (StringUtils.isBlank(value)) {
            throw new IllegalArgumentException("CurrencyCode cannot be null or empty");
        }
    }

    @Override
    protected FmkCurrencyCode newInstance(String value) {
        return new FmkCurrencyCode(value);
    }
}