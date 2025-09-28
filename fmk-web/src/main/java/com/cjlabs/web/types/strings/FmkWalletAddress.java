package com.cjlabs.web.types.strings;

import com.cjlabs.web.types.base.BaseStringType;
import com.fasterxml.jackson.annotation.JsonCreator;
import org.apache.commons.lang3.StringUtils;

/**
 * 钱包地址类型安全包装类
 */
public class FmkWalletAddress extends BaseStringType<FmkWalletAddress> {

    /**
     * 从字符串创建WalletAddress，支持null值
     */
    public static FmkWalletAddress ofNullable(String value) {
        return ofNullable(value, FmkWalletAddress::new);
    }

    /**
     * 从Object值创建WalletAddress，支持JSON反序列化
     */
    @JsonCreator
    public static FmkWalletAddress ofNullable(Object value) {
        return ofNullableObject(value, FmkWalletAddress::new, String.class);
    }

    /**
     * 从字符串创建WalletAddress，不允许null或空值
     */
    public static FmkWalletAddress of(String value) {
        if (StringUtils.isBlank(value)) {
            throw new IllegalArgumentException("WalletAddress cannot be null or empty");
        }
        return new FmkWalletAddress(value.trim());
    }

    public FmkWalletAddress(String value) {
        super(value);
        if (StringUtils.isBlank(value)) {
            throw new IllegalArgumentException("WalletAddress cannot be null or empty");
        }
    }

    @Override
    protected FmkWalletAddress newInstance(String value) {
        return new FmkWalletAddress(value);
    }

    /**
     * 获取地址的简短形式（用于显示）
     */
    public String getShortForm() {
        String address = getValue();
        if (address == null || address.length() <= 10) {
            return address;
        }

        return address.substring(0, 6) + "..." + address.substring(address.length() - 4);
    }
}