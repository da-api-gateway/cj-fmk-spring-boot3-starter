package com.cjlabs.core.types.base;

import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Objects;
import java.util.function.Function;

/**
 * 基础类型抽象类，为所有类型安全包装类提供基础功能
 */
public abstract class BaseType<T, S extends BaseType<T, S>> {

    protected final T value;

    protected BaseType(T value) {
        this.value = value;
    }

    /**
     * 获取原始值
     */
    @JsonValue
    public T getValue() {
        return value;
    }

    /**
     * 创建新实例的抽象方法
     */
    protected abstract S newInstance(T value);

    /**
     * 从可能为null的值创建实例
     */
    protected static <T, S extends BaseType<T, S>> S ofNullable(T value, Function<T, S> constructor) {
        return value == null ? null : constructor.apply(value);
    }

    /**
     * 从Object值创建实例，支持JSON反序列化
     */
    protected static <T, S extends BaseType<T, S>> S ofNullableObject(
            Object value, Function<T, S> constructor, Class<T> valueType) {
        if (value == null) {
            return null;
        }
        if (valueType.isInstance(value)) {
            return constructor.apply(valueType.cast(value));
        }
        if (value instanceof String && valueType != String.class) {
            try {
                // 尝试转换字符串到目标类型
                return constructor.apply(convertStringToType((String) value, valueType));
            } catch (Exception e) {
                throw new IllegalArgumentException("Cannot convert " + value + " to " + valueType.getSimpleName(), e);
            }
        }
        throw new IllegalArgumentException("Cannot convert " + value.getClass() + " to " + valueType.getSimpleName());
    }

    /**
     * 字符串转换为指定类型
     */
    @SuppressWarnings("unchecked")
    private static <T> T convertStringToType(String value, Class<T> targetType) {
        if (targetType == String.class) {
            return (T) value;
        } else if (targetType == Long.class) {
            return (T) Long.valueOf(value);
        } else if (targetType == Integer.class) {
            return (T) Integer.valueOf(value);
        } else if (targetType == Double.class) {
            return (T) Double.valueOf(value);
        } else if (targetType == Boolean.class) {
            return (T) Boolean.valueOf(value);
        }
        throw new IllegalArgumentException("Unsupported type conversion: String to " + targetType.getSimpleName());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BaseType)) return false;
        BaseType<?, ?> baseType = (BaseType<?, ?>) o;
        return Objects.equals(value, baseType.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}