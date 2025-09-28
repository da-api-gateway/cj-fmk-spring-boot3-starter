package com.cjlabs.web.types.longs;

import com.cjlabs.domain.common.FmkConstant;
import com.cjlabs.web.types.base.BaseLongType;
import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * 用户ID类型安全包装类
 */
public class FmkUserId extends BaseLongType<FmkUserId> {

    /**
     * 系统用户ID
     */
    public static final FmkUserId SYSTEM = new FmkUserId(FmkConstant.SYSTEM_USER_ID);

    /**
     * 从Long创建UserId，支持null值
     */
    public static FmkUserId ofNullable(Long value) {
        return ofNullable(value, FmkUserId::new);
    }

    /**
     * 从Object值创建UserId，支持JSON反序列化
     */
    @JsonCreator
    public static FmkUserId ofNullable(Object value) {
        return ofNullableObject(value, FmkUserId::new, Long.class);
    }

    /**
     * 从Long创建UserId，不允许null值
     */
    public static FmkUserId of(Long value) {
        if (value == null) {
            throw new IllegalArgumentException("UserId cannot be null");
        }
        return new FmkUserId(value);
    }

    /**
     * 从int创建UserId
     */
    public static FmkUserId of(int value) {
        return new FmkUserId((long) value);
    }

    /**
     * 从字符串创建UserId
     */
    public static FmkUserId fromString(String value) {
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException("UserId string cannot be null or empty");
        }
        try {
            return new FmkUserId(Long.parseLong(value));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid UserId format: " + value, e);
        }
    }

    public FmkUserId(Long value) {
        super(value);
        if (value == null) {
            throw new IllegalArgumentException("UserId cannot be null");
        }
    }

    @Override
    protected FmkUserId newInstance(Long value) {
        return new FmkUserId(value);
    }

    /**
     * 检查是否为系统用户
     */
    public boolean isSystem() {
        return value != null && value.equals(SYSTEM.value);
    }

    /**
     * 检查是否为有效用户（非系统、非匿名）
     */
    public boolean isRegularUser() {
        return value != null && value > 0;
    }
}