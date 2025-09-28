package com.cjlabs.web.types.strings;

import com.cjlabs.web.types.base.BaseStringType;
import com.fasterxml.jackson.annotation.JsonCreator;
import org.apache.commons.lang3.StringUtils;

/**
 * 区块链交易哈希类型安全包装类
 */
public class FmkTransactionHash extends BaseStringType<FmkTransactionHash> {

    /**
     * 从字符串创建TransactionHash，支持null值
     */
    public static FmkTransactionHash ofNullable(String value) {
        return ofNullable(value, FmkTransactionHash::new);
    }

    /**
     * 从Object值创建TransactionHash，支持JSON反序列化
     */
    @JsonCreator
    public static FmkTransactionHash ofNullable(Object value) {
        return ofNullableObject(value, FmkTransactionHash::new, String.class);
    }

    /**
     * 从字符串创建TransactionHash，不允许null或空值
     */
    public static FmkTransactionHash of(String value) {
        if (StringUtils.isBlank(value)) {
            throw new IllegalArgumentException("TransactionHash cannot be null or empty");
        }
        return new FmkTransactionHash(value.trim());
    }

    public FmkTransactionHash(String value) {
        super(value);
        if (StringUtils.isBlank(value)) {
            throw new IllegalArgumentException("TransactionHash cannot be null or empty");
        }
    }

    @Override
    protected FmkTransactionHash newInstance(String value) {
        return new FmkTransactionHash(value);
    }

    /**
     * 获取哈希的简短形式（用于显示）
     */
    public String getShortForm() {
        String hash = getValue();
        if (hash == null || hash.length() <= 10) {
            return hash;
        }

        return hash.substring(0, 6) + "..." + hash.substring(hash.length() - 4);
    }
}