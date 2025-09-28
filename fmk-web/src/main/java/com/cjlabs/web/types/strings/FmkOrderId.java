package com.cjlabs.web.types.strings;

import com.cjlabs.web.types.base.BaseStringType;

import com.fasterxml.jackson.annotation.JsonCreator;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Pattern;

/**
 * 订单ID类型安全包装类
 */
public class FmkOrderId extends BaseStringType<FmkOrderId> {

    private static final String ORDER_PREFIX = "ORDER_";
    private static final Pattern ORDER_ID_PATTERN = Pattern.compile("^ORDER_[A-Za-z0-9]{16,32}$");
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    /**
     * 从字符串创建OrderId，支持null值
     */
    public static FmkOrderId ofNullable(String value) {
        return ofNullable(value, FmkOrderId::new);
    }

    /**
     * 从Object值创建OrderId，支持JSON反序列化
     */
    @JsonCreator
    public static FmkOrderId ofNullable(Object value) {
        return ofNullableObject(value, FmkOrderId::new, String.class);
    }

    /**
     * 从字符串创建OrderId，不允许null或空值
     */
    public static FmkOrderId of(String value) {
        if (StringUtils.isBlank(value)) {
            throw new IllegalArgumentException("OrderId cannot be null or empty");
        }

        String trimmedValue = value.trim();
        if (!trimmedValue.startsWith(ORDER_PREFIX)) {
            trimmedValue = ORDER_PREFIX + trimmedValue;
        }

        return new FmkOrderId(trimmedValue);
    }

    /**
     * 生成一个新的订单ID（基于时间戳）
     */
    public static FmkOrderId generate() {
        String timestamp = LocalDateTime.now().format(DATE_FORMATTER);
        String random = String.format("%06d", ThreadLocalRandom.current().nextInt(1000000));
        return new FmkOrderId(ORDER_PREFIX + timestamp + random);
    }

    /**
     * 生成一个带前缀的订单ID
     * @deprecated 使用 generate() 方法代替，所有订单ID现在必须以ORDER_开头
     */
    @Deprecated
    public static FmkOrderId generateWithPrefix(String prefix) {
        if (StringUtils.isBlank(prefix)) {
            throw new IllegalArgumentException("Prefix cannot be null or empty");
        }
        if (!prefix.equals(ORDER_PREFIX)) {
            throw new IllegalArgumentException("OrderId must start with " + ORDER_PREFIX);
        }
        String timestamp = LocalDateTime.now().format(DATE_FORMATTER);
        String random = String.format("%04d", ThreadLocalRandom.current().nextInt(10000));
        return new FmkOrderId(prefix + timestamp + random);
    }

    public FmkOrderId(String value) {
        super(value);
        if (StringUtils.isBlank(value)) {
            throw new IllegalArgumentException("OrderId cannot be null or empty");
        }
        if (!value.startsWith(ORDER_PREFIX)) {
            throw new IllegalArgumentException("OrderId must start with " + ORDER_PREFIX);
        }
        if (!ORDER_ID_PATTERN.matcher(value).matches()) {
            throw new IllegalArgumentException("Invalid OrderId format: " + value);
        }
    }

    @Override
    protected FmkOrderId newInstance(String value) {
        return new FmkOrderId(value);
    }

    /**
     * 提取订单ID中的时间戳部分（如果包含）
     */
    public LocalDateTime extractTimestamp() {
        if (value.length() < ORDER_PREFIX.length() + 14) {
            return null;
        }

        try {
            String dateStr = value.substring(ORDER_PREFIX.length(), ORDER_PREFIX.length() + 14);
            return LocalDateTime.parse(dateStr, DATE_FORMATTER);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 检查是否为指定前缀的订单
     * @deprecated 所有订单ID现在必须以ORDER_开头
     */
    @Deprecated
    public boolean hasPrefix(String prefix) {
        return value != null && value.startsWith(prefix);
    }
}