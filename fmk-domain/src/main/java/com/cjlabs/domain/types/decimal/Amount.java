package com.cjlabs.domain.types.decimal;

import com.cjlabs.domain.types.base.BaseType;
import com.fasterxml.jackson.annotation.JsonCreator;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * 金额类型安全包装类
 */
public class Amount extends BaseType<BigDecimal, Amount> {
    
    /**
     * 零金额
     */
    public static final Amount ZERO = new Amount(BigDecimal.ZERO);
    
    /**
     * 从BigDecimal创建Amount，支持null值
     */
    public static Amount ofNullable(BigDecimal value) {
        return ofNullable(value, Amount::new);
    }
    
    /**
     * 从Object值创建Amount，支持JSON反序列化
     */
    @JsonCreator
    public static Amount ofNullable(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof BigDecimal) {
            return new Amount((BigDecimal) value);
        }
        if (value instanceof Number) {
            return new Amount(new BigDecimal(value.toString()));
        }
        if (value instanceof String) {
            try {
                return new Amount(new BigDecimal((String) value));
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid amount format: " + value, e);
            }
        }
        throw new IllegalArgumentException("Cannot convert " + value.getClass() + " to Amount");
    }
    
    /**
     * 从BigDecimal创建Amount，不允许null值
     */
    public static Amount of(BigDecimal value) {
        if (value == null) {
            throw new IllegalArgumentException("Amount cannot be null");
        }
        return new Amount(value);
    }
    
    /**
     * 从double创建Amount
     */
    public static Amount of(double value) {
        return new Amount(BigDecimal.valueOf(value));
    }
    
    /**
     * 从long创建Amount
     */
    public static Amount of(long value) {
        return new Amount(BigDecimal.valueOf(value));
    }
    
    /**
     * 从字符串创建Amount
     */
    public static Amount fromString(String value) {
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException("Amount string cannot be null or empty");
        }
        try {
            return new Amount(new BigDecimal(value));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid amount format: " + value, e);
        }
    }
    
    public Amount(BigDecimal value) {
        super(value);
        if (value == null) {
            throw new IllegalArgumentException("Amount cannot be null");
        }
    }
    
    @Override
    protected Amount newInstance(BigDecimal value) {
        return new Amount(value);
    }
    
    /**
     * 加法运算
     */
    public Amount add(Amount other) {
        if (other == null || other.value == null) {
            return this;
        }
        return new Amount(value.add(other.value));
    }
    
    /**
     * 减法运算
     */
    public Amount subtract(Amount other) {
        if (other == null || other.value == null) {
            return this;
        }
        return new Amount(value.subtract(other.value));
    }
    
    /**
     * 乘法运算
     */
    public Amount multiply(BigDecimal multiplier) {
        if (multiplier == null) {
            return this;
        }
        return new Amount(value.multiply(multiplier));
    }
    
    /**
     * 除法运算
     */
    public Amount divide(BigDecimal divisor, int scale, RoundingMode roundingMode) {
        if (divisor == null || divisor.compareTo(BigDecimal.ZERO) == 0) {
            throw new ArithmeticException("Division by zero");
        }
        return new Amount(value.divide(divisor, scale, roundingMode));
    }
    
    /**
     * 是否为正数
     */
    public boolean isPositive() {
        return value != null && value.compareTo(BigDecimal.ZERO) > 0;
    }
    
    /**
     * 是否为负数
     */
    public boolean isNegative() {
        return value != null && value.compareTo(BigDecimal.ZERO) < 0;
    }
    
    /**
     * 是否为零
     */
    public boolean isZero() {
        return value != null && value.compareTo(BigDecimal.ZERO) == 0;
    }
    
    /**
     * 获取绝对值
     */
    public Amount abs() {
        return new Amount(value.abs());
    }
    
    /**
     * 设置精度
     */
    public Amount setScale(int scale, RoundingMode roundingMode) {
        return new Amount(value.setScale(scale, roundingMode));
    }
    
    /**
     * 格式化为货币字符串（默认美元）
     */
    public String formatAsCurrency() {
        return formatAsCurrency(Locale.US);
    }
    
    /**
     * 格式化为货币字符串（指定区域）
     */
    public String formatAsCurrency(Locale locale) {
        NumberFormat formatter = NumberFormat.getCurrencyInstance(locale);
        return formatter.format(value);
    }
    
    /**
     * 格式化为数字字符串（指定精度）
     */
    public String format(int scale) {
        return value.setScale(scale, RoundingMode.HALF_UP).toPlainString();
    }
    
    /**
     * 比较大小
     */
    public int compareTo(Amount other) {
        if (other == null || other.value == null) {
            return value == null ? 0 : 1;
        }
        return value.compareTo(other.value);
    }
    
    /**
     * 是否大于指定金额
     */
    public boolean isGreaterThan(Amount other) {
        return compareTo(other) > 0;
    }
    
    /**
     * 是否小于指定金额
     */
    public boolean isLessThan(Amount other) {
        return compareTo(other) < 0;
    }
    
    @Override
    public String toString() {
        return value.toPlainString();
    }
}