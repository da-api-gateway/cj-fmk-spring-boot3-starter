package com.cjlabs.core.types.strings;

import com.cjlabs.core.id.FmkSnowflakeIdGenerator;
import com.cjlabs.core.types.base.BaseStringType;

import org.apache.commons.lang3.StringUtils;

/**
 * 链路追踪ID类型安全包装类
 * 提供分布式系统中请求链路追踪的ID管理
 */
public class FmkTraceId extends BaseStringType<FmkTraceId> {

    private static final String TRACE_PREFIX = "TRACE_";

    // ==================== 静态工厂方法 ====================

    /**
     * 从字符串创建TraceId，支持null值
     */
    public static FmkTraceId ofNullable(String value) {
        return ofNullable(value, FmkTraceId::new);
    }

    /**
     * 从Object值创建TraceId，支持JSON反序列化
     */
    public static FmkTraceId ofNullable(Object value) {
        return ofNullableObject(value, FmkTraceId::new, String.class);
    }

    /**
     * 生成一个新的UUID格式的TraceId
     */
    public static FmkTraceId generate() {
        return new FmkTraceId(TRACE_PREFIX + FmkSnowflakeIdGenerator.nextId());
    }

    // ==================== 构造方法 ====================

    public FmkTraceId(String value) {
        super(value);
        if (StringUtils.isBlank(value)) {
            throw new IllegalArgumentException("TraceId cannot be null or empty");
        }
        if (!value.startsWith(TRACE_PREFIX)) {
            throw new IllegalArgumentException("TraceId must start with " + TRACE_PREFIX);
        }
    }

    @Override
    protected FmkTraceId newInstance(String value) {
        return new FmkTraceId(value);
    }

    // ==================== TraceId 操作 ====================

    /**
     * 获取不带前缀的TraceId值
     */
    public String getTraceWithoutPrefix() {
        return getValue().substring(TRACE_PREFIX.length());
    }
}