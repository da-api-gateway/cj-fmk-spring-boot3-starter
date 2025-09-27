package com.cjlabs.core.types.strings;

import com.cjlabs.core.types.base.BaseStringType;

import com.fasterxml.jackson.annotation.JsonCreator;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Pattern;

/**
 * 订单ID类型安全包装类
 */
public class OrderId extends BaseStringType<OrderId> {
    
    private static final Pattern ORDER_ID_PATTERN = Pattern.compile("^[A-Za-z0-9]{16,32}$");
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    
    /**
     * 从字符串创建OrderId，支持null值
     */
    public static OrderId ofNullable(String value) {
        return ofNullable(value, OrderId::new);
    }
    
    /**
     * 从Object值创建OrderId，支持JSON反序列化
     */
    @JsonCreator
    public static OrderId ofNullable(Object value) {
        return ofNullableObject(value, OrderId::new, String.class);
    }
    
    /**
     * 从字符串创建OrderId，不允许null或空值
     */
    public static OrderId of(String value) {
        if (StringUtils.isBlank(value)) {
            throw new IllegalArgumentException("OrderId cannot be null or empty");
        }
        return new OrderId(value.trim());
    }
    
    /**
     * 生成一个新的订单ID（基于时间戳）
     */
    public static OrderId generate() {
        String timestamp = LocalDateTime.now().format(DATE_FORMATTER);
        String random = String.format("%06d", ThreadLocalRandom.current().nextInt(1000000));
        return new OrderId(timestamp + random);
    }
    
    /**
     * 生成一个带前缀的订单ID
     */
    public static OrderId generateWithPrefix(String prefix) {
        if (StringUtils.isBlank(prefix)) {
            throw new IllegalArgumentException("Prefix cannot be null or empty");
        }
        String timestamp = LocalDateTime.now().format(DATE_FORMATTER);
        String random = String.format("%04d", ThreadLocalRandom.current().nextInt(10000));
        return new OrderId(prefix + timestamp + random);
    }
    
    public OrderId(String value) {
        super(value);
        if (StringUtils.isBlank(value)) {
            throw new IllegalArgumentException("OrderId cannot be null or empty");
        }
        if (!ORDER_ID_PATTERN.matcher(value).matches()) {
            throw new IllegalArgumentException("Invalid OrderId format: " + value);
        }
    }
    
    @Override
    protected OrderId newInstance(String value) {
        return new OrderId(value);
    }
    
    /**
     * 提取订单ID中的时间戳部分（如果包含）
     */
    public LocalDateTime extractTimestamp() {
        if (value.length() < 14) {
            return null;
        }
        
        try {
            String dateStr = value.substring(0, 14);
            return LocalDateTime.parse(dateStr, DATE_FORMATTER);
        } catch (Exception e) {
            return null;
        }
    }
    
    /**
     * 检查是否为指定前缀的订单
     */
    public boolean hasPrefix(String prefix) {
        return value != null && value.startsWith(prefix);
    }
}