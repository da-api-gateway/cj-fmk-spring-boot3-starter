package com.cjlabs.core.types.strings;

import com.cjlabs.core.id.FmkSnowflakeIdGenerator;
import com.cjlabs.core.types.base.BaseStringType;
import org.apache.commons.lang3.StringUtils;

/**
 * 链路追踪 SpanId 类型安全包装类
 * 用于标识单个操作或请求的唯一 Span
 */
public class FmkSpanId extends BaseStringType<FmkSpanId> {

    private static final String SPAN_PREFIX = "SPAN_";

    // ==================== 静态工厂方法 ====================

    /**
     * 从字符串创建 SpanId，支持 null 值
     */
    public static FmkSpanId ofNullable(String value) {
        return ofNullable(value, FmkSpanId::new);
    }

    /**
     * 从 Object 值创建 SpanId，支持 JSON 反序列化
     */
    public static FmkSpanId ofNullable(Object value) {
        return ofNullableObject(value, FmkSpanId::new, String.class);
    }

    /**
     * 生成一个新的 SpanId（使用 Snowflake 或 UUID）
     */
    public static FmkSpanId generate() {
        return new FmkSpanId(SPAN_PREFIX + FmkSnowflakeIdGenerator.nextId());
    }

    // ==================== 构造方法 ====================

    public FmkSpanId(String value) {
        super(value);
        if (StringUtils.isBlank(value)) {
            throw new IllegalArgumentException("SpanId cannot be null or empty");
        }
        if (!value.startsWith(SPAN_PREFIX)) {
            throw new IllegalArgumentException("SpanId must start with " + SPAN_PREFIX);
        }
    }

    @Override
    protected FmkSpanId newInstance(String value) {
        return new FmkSpanId(value);
    }

    // ==================== SpanId 操作 ====================

    /**
     * 获取不带前缀的 SpanId 值
     */
    public String getSpanWithoutPrefix() {
        return getValue().substring(SPAN_PREFIX.length());
    }
}
