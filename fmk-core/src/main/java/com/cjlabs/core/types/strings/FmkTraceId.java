package com.cjlabs.core.types.strings;

import com.cjlabs.core.types.base.BaseStringType;

import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 链路追踪ID类型安全包装类
 * 提供分布式系统中请求链路追踪的ID管理
 */
public class FmkTraceId extends BaseStringType<FmkTraceId> {

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
     * 从字符串创建TraceId，不允许null或空值
     */
    public static FmkTraceId of(String value) {
        if (StringUtils.isBlank(value)) {
            throw new IllegalArgumentException("TraceId cannot be null or empty");
        }
        return new FmkTraceId(value.trim());
    }

    // ==================== 构造方法 ====================

    public FmkTraceId(String value) {
        super(value);
        if (StringUtils.isBlank(value)) {
            throw new IllegalArgumentException("TraceId cannot be null or empty");
        }
    }

    @Override
    protected FmkTraceId newInstance(String value) {
        return new FmkTraceId(value);
    }

    // ==================== TraceId 类型判断 ====================

}