package com.cjlabs.core.types.strings;

import com.cjlabs.core.types.base.BaseStringType;

import org.apache.commons.lang3.StringUtils;

/**
 * 区块链交易哈希类型安全包装类
 */
public class FmkTxHash extends BaseStringType<FmkTxHash> {

    /**
     * 从字符串创建TransactionHash，支持null值
     */
    public static FmkTxHash ofNullable(String value) {
        return ofNullable(value, FmkTxHash::new);
    }

    /**
     * 从Object值创建TransactionHash，支持JSON反序列化
     */
    public static FmkTxHash ofNullable(Object value) {
        return ofNullableObject(value, FmkTxHash::new, String.class);
    }

    /**
     * 从字符串创建TransactionHash，不允许null或空值
     */
    public static FmkTxHash of(String value) {
        if (StringUtils.isBlank(value)) {
            throw new IllegalArgumentException("TransactionHash cannot be null or empty");
        }
        return new FmkTxHash(value.trim());
    }

    public FmkTxHash(String value) {
        super(value);
        if (StringUtils.isBlank(value)) {
            throw new IllegalArgumentException("TransactionHash cannot be null or empty");
        }
    }

    @Override
    protected FmkTxHash newInstance(String value) {
        return new FmkTxHash(value);
    }

}