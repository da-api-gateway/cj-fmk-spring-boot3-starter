package com.cjlabs.core.types.strings;

import com.cjlabs.core.types.base.BaseStringType;
import com.fasterxml.jackson.annotation.JsonCreator;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 货币代码类型安全包装类
 */
public class CurrencyCode extends BaseStringType<CurrencyCode> {
    
    // 常用法定货币代码
    private static final Set<String> FIAT_CURRENCIES = new HashSet<>(Arrays.asList(
            "USD", "EUR", "CNY", "JPY", "GBP", "AUD", "CAD", "CHF", "HKD", "SGD"
    ));
    
    // 常用加密货币代码
    private static final Set<String> CRYPTO_CURRENCIES = new HashSet<>(Arrays.asList(
            "BTC", "ETH", "USDT", "USDC", "BNB", "XRP", "ADA", "SOL", "DOT", "DOGE"
    ));
    
    /**
     * 从字符串创建CurrencyCode，支持null值
     */
    public static CurrencyCode ofNullable(String value) {
        return ofNullable(value, CurrencyCode::new);
    }
    
    /**
     * 从Object值创建CurrencyCode，支持JSON反序列化
     */
    @JsonCreator
    public static CurrencyCode ofNullable(Object value) {
        return ofNullableObject(value, CurrencyCode::new, String.class);
    }
    
    /**
     * 从字符串创建CurrencyCode，不允许null或空值
     */
    public static CurrencyCode of(String value) {
        if (StringUtils.isBlank(value)) {
            throw new IllegalArgumentException("CurrencyCode cannot be null or empty");
        }
        return new CurrencyCode(value.trim().toUpperCase());
    }
    
    // 常用货币常量
    public static final CurrencyCode USD = new CurrencyCode("USD");
    public static final CurrencyCode EUR = new CurrencyCode("EUR");
    public static final CurrencyCode CNY = new CurrencyCode("CNY");
    public static final CurrencyCode BTC = new CurrencyCode("BTC");
    public static final CurrencyCode ETH = new CurrencyCode("ETH");
    public static final CurrencyCode USDT = new CurrencyCode("USDT");
    
    public CurrencyCode(String value) {
        super(value);
        if (StringUtils.isBlank(value)) {
            throw new IllegalArgumentException("CurrencyCode cannot be null or empty");
        }
        // 统一转为大写
        this.value = value.toUpperCase();
    }
    
    @Override
    protected CurrencyCode newInstance(String value) {
        return new CurrencyCode(value);
    }
    
    /**
     * 检查是否为法定货币
     */
    public boolean isFiat() {
        return FIAT_CURRENCIES.contains(value);
    }
    
    /**
     * 检查是否为加密货币
     */
    public boolean isCrypto() {
        return CRYPTO_CURRENCIES.contains(value);
    }
    
    /**
     * 检查是否为稳定币
     */
    public boolean isStablecoin() {
        return "USDT".equals(value) || "USDC".equals(value) || "DAI".equals(value) || "BUSD".equals(value);
    }
    
    /**
     * 获取货币类型
     */
    public String getType() {
        if (isFiat()) {
            return "FIAT";
        } else if (isCrypto()) {
            return "CRYPTO";
        } else {
            return "OTHER";
        }
    }
    
    /**
     * 获取货币符号
     */
    public String getSymbol() {
        switch (value) {
            case "USD": return "$";
            case "EUR": return "€";
            case "GBP": return "£";
            case "JPY": return "¥";
            case "CNY": return "¥";
            case "BTC": return "₿";
            case "ETH": return "Ξ";
            default: return value;
        }
    }
}