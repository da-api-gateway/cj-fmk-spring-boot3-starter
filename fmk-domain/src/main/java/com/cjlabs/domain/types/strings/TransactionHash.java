package com.cjlabs.domain.types.strings;

import com.cjlabs.domain.types.base.BaseStringType;

import com.fasterxml.jackson.annotation.JsonCreator;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;

/**
 * 区块链交易哈希类型安全包装类
 */
public class TransactionHash extends BaseStringType<TransactionHash> {
    
    // 支持多种区块链交易哈希格式
    private static final Pattern ETH_TX_HASH_PATTERN = Pattern.compile("^0x[a-fA-F0-9]{64}$");
    private static final Pattern BTC_TX_HASH_PATTERN = Pattern.compile("^[a-fA-F0-9]{64}$");
    private static final Pattern GENERAL_TX_HASH_PATTERN = Pattern.compile("^[a-fA-F0-9]{64,128}$");
    
    /**
     * 从字符串创建TransactionHash，支持null值
     */
    public static TransactionHash ofNullable(String value) {
        return ofNullable(value, TransactionHash::new);
    }
    
    /**
     * 从Object值创建TransactionHash，支持JSON反序列化
     */
    @JsonCreator
    public static TransactionHash ofNullable(Object value) {
        return ofNullableObject(value, TransactionHash::new, String.class);
    }
    
    /**
     * 从字符串创建TransactionHash，不允许null或空值
     */
    public static TransactionHash of(String value) {
        if (StringUtils.isBlank(value)) {
            throw new IllegalArgumentException("TransactionHash cannot be null or empty");
        }
        return new TransactionHash(value.trim());
    }
    
    public TransactionHash(String value) {
        super(value);
        if (StringUtils.isBlank(value)) {
            throw new IllegalArgumentException("TransactionHash cannot be null or empty");
        }
        if (!isValidFormat(value.trim())) {
            throw new IllegalArgumentException("Invalid transaction hash format: " + value);
        }
    }
    
    @Override
    protected TransactionHash newInstance(String value) {
        return new TransactionHash(value);
    }
    
    /**
     * 检查哈希格式是否有效
     */
    private static boolean isValidFormat(String value) {
        return ETH_TX_HASH_PATTERN.matcher(value).matches() ||
               BTC_TX_HASH_PATTERN.matcher(value).matches() ||
               GENERAL_TX_HASH_PATTERN.matcher(value).matches();
    }
    
    /**
     * 检查是否为以太坊交易哈希
     */
    public boolean isEthereumHash() {
        return ETH_TX_HASH_PATTERN.matcher(value).matches();
    }
    
    /**
     * 检查是否为比特币交易哈希
     */
    public boolean isBitcoinHash() {
        return BTC_TX_HASH_PATTERN.matcher(value).matches() && !value.startsWith("0x");
    }
    
    /**
     * 获取哈希的简短形式（用于显示）
     */
    public String getShortForm() {
        if (value == null || value.length() <= 10) {
            return value;
        }
        
        return value.substring(0, 6) + "..." + value.substring(value.length() - 4);
    }
    
    /**
     * 获取标准化的哈希（去除0x前缀）
     */
    public String getNormalizedHash() {
        if (value == null) {
            return null;
        }
        return value.startsWith("0x") ? value.substring(2).toLowerCase() : value.toLowerCase();
    }
    
    /**
     * 获取带0x前缀的哈希
     */
    public String getPrefixedHash() {
        if (value == null) {
            return null;
        }
        return value.startsWith("0x") ? value.toLowerCase() : "0x" + value.toLowerCase();
    }
}