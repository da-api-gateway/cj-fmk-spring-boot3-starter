package com.cjlabs.web.types.base;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * BigDecimal类型的基础类型
 */
public abstract class BaseBigDecimalType<S extends BaseBigDecimalType<S>> extends BaseType<BigDecimal, S> {
    
    protected BaseBigDecimalType(BigDecimal value) {
        super(value);
    }
    
    /**
     * 加法运算
     */
    public S add(BigDecimal addend) {
        return getValue() != null && addend != null ? newInstance(getValue().add(addend)) : null;
    }
    
    /**
     * 加法运算（使用另一个类型安全的BigDecimal）
     */
    public S add(BaseBigDecimalType<?> addend) {
        return getValue() != null && addend != null && addend.getValue() != null ? 
               newInstance(getValue().add(addend.getValue())) : null;
    }
    
    /**
     * 减法运算
     */
    public S subtract(BigDecimal subtrahend) {
        return getValue() != null && subtrahend != null ? newInstance(getValue().subtract(subtrahend)) : null;
    }
    
    /**
     * 减法运算（使用另一个类型安全的BigDecimal）
     */
    public S subtract(BaseBigDecimalType<?> subtrahend) {
        return getValue() != null && subtrahend != null && subtrahend.getValue() != null ? 
               newInstance(getValue().subtract(subtrahend.getValue())) : null;
    }
    
    /**
     * 乘法运算
     */
    public S multiply(BigDecimal multiplier) {
        return getValue() != null && multiplier != null ? newInstance(getValue().multiply(multiplier)) : null;
    }
    
    /**
     * 乘法运算（使用另一个类型安全的BigDecimal）
     */
    public S multiply(BaseBigDecimalType<?> multiplier) {
        return getValue() != null && multiplier != null && multiplier.getValue() != null ? 
               newInstance(getValue().multiply(multiplier.getValue())) : null;
    }
    
    /**
     * 除法运算（使用默认精度和舍入模式）
     */
    public S divide(BigDecimal divisor) {
        if (getValue() == null || divisor == null || divisor.compareTo(BigDecimal.ZERO) == 0) {
            throw new ArithmeticException("Division by zero or null values");
        }
        return newInstance(getValue().divide(divisor, 8, RoundingMode.HALF_UP));
    }
    
    /**
     * 除法运算（使用指定精度和舍入模式）
     */
    public S divide(BigDecimal divisor, int scale, RoundingMode roundingMode) {
        if (getValue() == null || divisor == null || divisor.compareTo(BigDecimal.ZERO) == 0) {
            throw new ArithmeticException("Division by zero or null values");
        }
        return newInstance(getValue().divide(divisor, scale, roundingMode));
    }
    
    /**
     * 除法运算（使用另一个类型安全的BigDecimal）
     */
    public S divide(BaseBigDecimalType<?> divisor) {
        if (getValue() == null || divisor == null || divisor.getValue() == null || 
            divisor.getValue().compareTo(BigDecimal.ZERO) == 0) {
            throw new ArithmeticException("Division by zero or null values");
        }
        return newInstance(getValue().divide(divisor.getValue(), 8, RoundingMode.HALF_UP));
    }
    
    /**
     * 设置精度（四舍五入）
     */
    public S setScale(int scale) {
        return getValue() != null ? newInstance(getValue().setScale(scale, RoundingMode.HALF_UP)) : null;
    }
    
    /**
     * 设置精度（指定舍入模式）
     */
    public S setScale(int scale, RoundingMode roundingMode) {
        return getValue() != null ? newInstance(getValue().setScale(scale, roundingMode)) : null;
    }
    
    /**
     * 取绝对值
     */
    public S abs() {
        return getValue() != null ? newInstance(getValue().abs()) : null;
    }
    
    /**
     * 取反值
     */
    public S negate() {
        return getValue() != null ? newInstance(getValue().negate()) : null;
    }
    
    /**
     * 是否大于指定值
     */
    public boolean isGreaterThan(BigDecimal other) {
        return getValue() != null && other != null && getValue().compareTo(other) > 0;
    }
    
    /**
     * 是否大于指定值（使用另一个类型安全的BigDecimal）
     */
    public boolean isGreaterThan(BaseBigDecimalType<?> other) {
        return getValue() != null && other != null && other.getValue() != null && 
               getValue().compareTo(other.getValue()) > 0;
    }
    
    /**
     * 是否大于或等于指定值
     */
    public boolean isGreaterThanOrEqual(BigDecimal other) {
        return getValue() != null && other != null && getValue().compareTo(other) >= 0;
    }
    
    /**
     * 是否小于指定值
     */
    public boolean isLessThan(BigDecimal other) {
        return getValue() != null && other != null && getValue().compareTo(other) < 0;
    }
    
    /**
     * 是否小于指定值（使用另一个类型安全的BigDecimal）
     */
    public boolean isLessThan(BaseBigDecimalType<?> other) {
        return getValue() != null && other != null && other.getValue() != null && 
               getValue().compareTo(other.getValue()) < 0;
    }
    
    /**
     * 是否小于或等于指定值
     */
    public boolean isLessThanOrEqual(BigDecimal other) {
        return getValue() != null && other != null && getValue().compareTo(other) <= 0;
    }
    
    /**
     * 是否等于指定值
     */
    public boolean isEqual(BigDecimal other) {
        return getValue() != null && other != null && getValue().compareTo(other) == 0;
    }
    
    /**
     * 是否为正数
     */
    public boolean isPositive() {
        return getValue() != null && getValue().compareTo(BigDecimal.ZERO) > 0;
    }
    
    /**
     * 是否为负数
     */
    public boolean isNegative() {
        return getValue() != null && getValue().compareTo(BigDecimal.ZERO) < 0;
    }
    
    /**
     * 是否为零
     */
    public boolean isZero() {
        return getValue() != null && getValue().compareTo(BigDecimal.ZERO) == 0;
    }
    
    /**
     * 获取最大值
     */
    public S max(BigDecimal other) {
        if (getValue() == null) return null;
        if (other == null) return newInstance(getValue());
        return newInstance(getValue().max(other));
    }
    
    /**
     * 获取最小值
     */
    public S min(BigDecimal other) {
        if (getValue() == null) return null;
        if (other == null) return newInstance(getValue());
        return newInstance(getValue().min(other));
    }
    
    /**
     * 移动小数点位置
     */
    public S movePointLeft(int n) {
        return getValue() != null ? newInstance(getValue().movePointLeft(n)) : null;
    }
    
    /**
     * 移动小数点位置
     */
    public S movePointRight(int n) {
        return getValue() != null ? newInstance(getValue().movePointRight(n)) : null;
    }
    
    /**
     * 转换为字符串（使用纯数字表示，不使用科学计数法）
     */
    public String toPlainString() {
        return getValue() != null ? getValue().toPlainString() : "null";
    }
    
    @Override
    public String toString() {
        return getValue() != null ? getValue().toString() : "null";
    }
}