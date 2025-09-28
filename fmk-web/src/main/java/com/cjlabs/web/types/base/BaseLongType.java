package com.cjlabs.web.types.base;

/**
 * Long类型的基础类型
 */
public abstract class BaseLongType<S extends BaseLongType<S>> extends BaseType<Long, S> {
    
    protected BaseLongType(Long value) {
        super(value);
    }
    
    /**
     * 加法运算
     */
    public S add(long addend) {
        return value != null ? newInstance(value + addend) : null;
    }
    
    /**
     * 减法运算
     */
    public S subtract(long subtrahend) {
        return value != null ? newInstance(value - subtrahend) : null;
    }
    
    /**
     * 乘法运算
     */
    public S multiply(long multiplier) {
        return value != null ? newInstance(value * multiplier) : null;
    }
    
    /**
     * 除法运算
     */
    public S divide(long divisor) {
        if (divisor == 0) {
            throw new ArithmeticException("Division by zero");
        }
        return value != null ? newInstance(value / divisor) : null;
    }
    
    /**
     * 是否大于指定值
     */
    public boolean isGreaterThan(long other) {
        return value != null && value > other;
    }
    
    /**
     * 是否小于指定值
     */
    public boolean isLessThan(long other) {
        return value != null && value < other;
    }
    
    /**
     * 是否为正数
     */
    public boolean isPositive() {
        return value != null && value > 0;
    }
    
    /**
     * 是否为负数
     */
    public boolean isNegative() {
        return value != null && value < 0;
    }
    
    /**
     * 是否为零
     */
    public boolean isZero() {
        return value != null && value == 0;
    }
    
    @Override
    public String toString() {
        return value != null ? value.toString() : "null";
    }
}