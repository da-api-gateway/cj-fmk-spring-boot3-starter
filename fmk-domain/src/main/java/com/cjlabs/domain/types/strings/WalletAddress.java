package com.cjlabs.domain.types.strings;

import com.cjlabs.domain.types.base.BaseStringType;

import com.fasterxml.jackson.annotation.JsonCreator;
import org.apache.commons.lang3.StringUtils;

/**
 * 钱包地址类型安全包装类
 */
public class WalletAddress extends BaseStringType<WalletAddress> {

    /**
     * 从字符串创建WalletAddress，支持null值
     */
    public static WalletAddress ofNullable(String value) {
        return ofNullable(value, WalletAddress::new);
    }

    /**
     * 从Object值创建WalletAddress，支持JSON反序列化
     */
    @JsonCreator
    public static WalletAddress ofNullable(Object value) {
        return ofNullableObject(value, WalletAddress::new, String.class);
    }

    /**
     * 从字符串创建WalletAddress，不允许null或空值
     */
    public static WalletAddress of(String value) {
        if (StringUtils.isBlank(value)) {
            throw new IllegalArgumentException("WalletAddress cannot be null or empty");
        }
        return new WalletAddress(value.trim());
    }

    public WalletAddress(String value) {
        super(value);
        if (StringUtils.isBlank(value)) {
            throw new IllegalArgumentException("WalletAddress cannot be null or empty");
        }
    }

    @Override
    protected WalletAddress newInstance(String value) {
        return new WalletAddress(value);
    }

    /**
     * 获取地址的简短形式（用于显示）
     */
    public String getShortForm() {
        if (value == null || value.length() <= 10) {
            return value;
        }

        return value.substring(0, 6) + "..." + value.substring(value.length() - 4);
    }
}