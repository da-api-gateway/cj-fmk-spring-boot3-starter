package com.cjlabs.core.types.decimal;

import com.cjlabs.core.types.base.BaseType;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * 金额类型安全包装类
 */
public class FmkAmount extends BaseType<BigDecimal, FmkAmount> {

    /**
     * 零金额
     */
    public static final FmkAmount ZERO = new FmkAmount(BigDecimal.ZERO);

    /**
     * 从BigDecimal创建Amount，支持null值
     */
    public static FmkAmount ofNullable(BigDecimal value) {
        return ofNullable(value, FmkAmount::new);
    }

    /**
     * 从Object值创建Amount，支持JSON反序列化
     */
    public static FmkAmount ofNullable(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof BigDecimal) {
            return new FmkAmount((BigDecimal) value);
        }
        if (value instanceof Number) {
            return new FmkAmount(new BigDecimal(value.toString()));
        }
        if (value instanceof String) {
            try {
                return new FmkAmount(new BigDecimal((String) value));
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid amount format: " + value, e);
            }
        }
        throw new IllegalArgumentException("Cannot convert " + value.getClass() + " to Amount");
    }

    /**
     * 从BigDecimal创建Amount，不允许null值
     */
    public static FmkAmount of(BigDecimal value) {
        if (value == null) {
            throw new IllegalArgumentException("Amount cannot be null");
        }
        return new FmkAmount(value);
    }

    /**
     * 从double创建Amount
     */
    public static FmkAmount of(double value) {
        return new FmkAmount(BigDecimal.valueOf(value));
    }

    /**
     * 从long创建Amount
     */
    public static FmkAmount of(long value) {
        return new FmkAmount(BigDecimal.valueOf(value));
    }

    /**
     * 从字符串创建Amount
     */
    public static FmkAmount fromString(String value) {
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException("Amount string cannot be null or empty");
        }
        try {
            return new FmkAmount(new BigDecimal(value));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid amount format: " + value, e);
        }
    }

    public FmkAmount(BigDecimal value) {
        super(value);
        if (value == null) {
            throw new IllegalArgumentException("Amount cannot be null");
        }
    }

    @Override
    protected FmkAmount newInstance(BigDecimal value) {
        return new FmkAmount(value);
    }

    /**
     * 加法运算
     */
    public FmkAmount add(FmkAmount other) {
        if (other == null || other.value == null) {
            return this;
        }
        return new FmkAmount(value.add(other.value));
    }

    /**
     * 减法运算
     */
    public FmkAmount subtract(FmkAmount other) {
        if (other == null || other.value == null) {
            return this;
        }
        return new FmkAmount(value.subtract(other.value));
    }

    /**
     * 乘法运算
     */
    public FmkAmount multiply(BigDecimal multiplier) {
        if (multiplier == null) {
            return this;
        }
        return new FmkAmount(value.multiply(multiplier));
    }

    /**
     * 除法运算
     */
    public FmkAmount divide(BigDecimal divisor, int scale, RoundingMode roundingMode) {
        if (divisor == null || divisor.compareTo(BigDecimal.ZERO) == 0) {
            throw new ArithmeticException("Division by zero");
        }
        return new FmkAmount(value.divide(divisor, scale, roundingMode));
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
    public FmkAmount abs() {
        return new FmkAmount(value.abs());
    }

    /**
     * 设置精度
     */
    public FmkAmount setScale(int scale, RoundingMode roundingMode) {
        return new FmkAmount(value.setScale(scale, roundingMode));
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
    public int compareTo(FmkAmount other) {
        if (other == null || other.value == null) {
            return value == null ? 0 : 1;
        }
        return value.compareTo(other.value);
    }

    /**
     * 是否大于指定金额
     */
    public boolean isGreaterThan(FmkAmount other) {
        return compareTo(other) > 0;
    }

    /**
     * 是否小于指定金额
     */
    public boolean isLessThan(FmkAmount other) {
        return compareTo(other) < 0;
    }

    @Override
    public String toString() {
        return value.toPlainString();
    }
}