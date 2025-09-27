package com.cjlabs.core.types.longs;

import com.cjlabs.core.types.base.BaseLongType;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * 用户ID类型安全包装类
 */
public class UserId extends BaseLongType<UserId> {
    
    /**
     * 系统用户ID
     */
    public static final UserId SYSTEM = new UserId(0L);
    
    /**
     * 匿名用户ID
     */
    public static final UserId ANONYMOUS = new UserId(-1L);
    
    /**
     * 从Long创建UserId，支持null值
     */
    public static UserId ofNullable(Long value) {
        return ofNullable(value, UserId::new);
    }
    
    /**
     * 从Object值创建UserId，支持JSON反序列化
     */
    @JsonCreator
    public static UserId ofNullable(Object value) {
        return ofNullableObject(value, UserId::new, Long.class);
    }
    
    /**
     * 从Long创建UserId，不允许null值
     */
    public static UserId of(Long value) {
        if (value == null) {
            throw new IllegalArgumentException("UserId cannot be null");
        }
        return new UserId(value);
    }
    
    /**
     * 从int创建UserId
     */
    public static UserId of(int value) {
        return new UserId((long) value);
    }
    
    /**
     * 从字符串创建UserId
     */
    public static UserId fromString(String value) {
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException("UserId string cannot be null or empty");
        }
        try {
            return new UserId(Long.parseLong(value));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid UserId format: " + value, e);
        }
    }
    
    public UserId(Long value) {
        super(value);
        if (value == null) {
            throw new IllegalArgumentException("UserId cannot be null");
        }
    }
    
    @Override
    protected UserId newInstance(Long value) {
        return new UserId(value);
    }
    
    /**
     * 检查是否为系统用户
     */
    public boolean isSystem() {
        return value != null && value.equals(SYSTEM.value);
    }
    
    /**
     * 检查是否为匿名用户
     */
    public boolean isAnonymous() {
        return value != null && value.equals(ANONYMOUS.value);
    }
    
    /**
     * 检查是否为有效用户（非系统、非匿名）
     */
    public boolean isRegularUser() {
        return value != null && value > 0;
    }
}