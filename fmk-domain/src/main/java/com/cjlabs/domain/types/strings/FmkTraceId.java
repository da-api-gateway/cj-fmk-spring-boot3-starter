package com.cjlabs.domain.types.strings;

import com.cjlabs.domain.types.base.BaseStringType;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonCreator.Mode;
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

    // TraceId 格式正则表达式（支持多种格式）
    private static final Pattern UUID_PATTERN = Pattern.compile(
            "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$"
    );

    private static final Pattern HEX_PATTERN = Pattern.compile("^[0-9a-fA-F]{16,32}$");

    private static final Pattern CUSTOM_PATTERN = Pattern.compile("^[a-zA-Z0-9\\-_]{16,64}$");

    private static final DateTimeFormatter TIMESTAMP_FORMATTER =
            DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");
    
    // 线程本地变量存储当前线程的TraceId
    private static final ThreadLocal<FmkTraceId> THREAD_LOCAL = new ThreadLocal<>();

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
    @JsonCreator(mode = Mode.DELEGATING)
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

    /**
     * 生成一个新的UUID格式的TraceId
     */
    public static FmkTraceId generate() {
        return new FmkTraceId(UUID.randomUUID().toString());
    }

    /**
     * 生成一个新的32位十六进制格式的TraceId（去除连字符的UUID）
     */
    public static FmkTraceId generateHex() {
        return new FmkTraceId(UUID.randomUUID().toString().replace("-", ""));
    }

    /**
     * 生成一个带时间戳前缀的TraceId
     */
    public static FmkTraceId generateWithTimestamp() {
        String timestamp = LocalDateTime.now().format(TIMESTAMP_FORMATTER);
        String randomPart = Long.toHexString(ThreadLocalRandom.current().nextLong());
        return new FmkTraceId(timestamp + "-" + randomPart);
    }

    /**
     * 生成一个自定义格式的TraceId（时间戳+随机数）
     */
    public static FmkTraceId generateCustom(String prefix) {
        if (StringUtils.isBlank(prefix)) {
            throw new IllegalArgumentException("Prefix cannot be null or empty");
        }
        String timestamp = String.valueOf(System.currentTimeMillis());
        String randomPart = Long.toHexString(ThreadLocalRandom.current().nextLong());
        return new FmkTraceId(prefix + "-" + timestamp + "-" + randomPart);
    }

    /**
     * 从现有TraceId生成子TraceId（添加后缀）
     */
    public static FmkTraceId generateChild(FmkTraceId parentTraceId) {
        if (parentTraceId == null) {
            throw new IllegalArgumentException("Parent TraceId cannot be null");
        }
        String childSuffix = Integer.toHexString(ThreadLocalRandom.current().nextInt(0xFFFF));
        return new FmkTraceId(parentTraceId.value + "-" + childSuffix);
    }
    
    /**
     * 将TraceId集合转换为字符串集合，用于批量查询
     */
    public static List<String> toStringList(Collection<FmkTraceId> traceIds) {
        if (traceIds == null || traceIds.isEmpty()) {
            return Collections.emptyList();
        }
        return traceIds.stream()
                .filter(Objects::nonNull)
                .map(FmkTraceId::getValue)
                .collect(Collectors.toList());
    }
    
    /**
     * 将字符串集合转换为TraceId集合
     */
    public static List<FmkTraceId> fromStringList(Collection<String> values) {
        if (values == null || values.isEmpty()) {
            return Collections.emptyList();
        }
        return values.stream()
                .filter(StringUtils::isNotBlank)
                .map(FmkTraceId::of)
                .collect(Collectors.toList());
    }
    
    // ==================== 线程本地存储方法 ====================
    
    /**
     * 获取当前线程的TraceId
     */
    public static FmkTraceId current() {
        FmkTraceId traceId = THREAD_LOCAL.get();
        if (traceId == null) {
            traceId = generateHex();
            THREAD_LOCAL.set(traceId);
        }
        return traceId;
    }
    
    /**
     * 设置当前线程的TraceId
     */
    public static void setCurrent(FmkTraceId traceId) {
        THREAD_LOCAL.set(traceId);
    }
    
    /**
     * 清除当前线程的TraceId
     */
    public static void clear() {
        THREAD_LOCAL.remove();
    }

    // ==================== 构造方法 ====================

    public FmkTraceId(String value) {
        super(value);
        if (StringUtils.isBlank(value)) {
            throw new IllegalArgumentException("TraceId cannot be null or empty");
        }
        if (!isValidFormat(value.trim())) {
            throw new IllegalArgumentException("Invalid TraceId format: " + value);
        }
    }

    @Override
    protected FmkTraceId newInstance(String value) {
        return new FmkTraceId(value);
    }

    // ==================== TraceId 类型判断 ====================

    /**
     * 检查是否为UUID格式的TraceId
     */
    public boolean isUUIDFormat() {
        return UUID_PATTERN.matcher(value).matches();
    }

    /**
     * 检查是否为十六进制格式的TraceId
     */
    public boolean isHexFormat() {
        return HEX_PATTERN.matcher(value).matches();
    }

    /**
     * 检查是否为自定义格式的TraceId
     */
    public boolean isCustomFormat() {
        return CUSTOM_PATTERN.matcher(value).matches();
    }

    /**
     * 检查TraceId格式是否有效
     */
    private static boolean isValidFormat(String value) {
        return UUID_PATTERN.matcher(value).matches() ||
                HEX_PATTERN.matcher(value).matches() ||
                CUSTOM_PATTERN.matcher(value).matches();
    }

    /**
     * 检查是否包含时间戳信息
     */
    public boolean hasTimestamp() {
        return value.contains("-") && value.split("-").length >= 2;
    }

    // ==================== TraceId 操作 ====================

    /**
     * 获取TraceId的简短形式（前8个字符）
     */
    public String getShortForm() {
        return value.length() >= 8 ? value.substring(0, 8) : value;
    }

    /**
     * 获取TraceId的中等长度形式（前16个字符）
     */
    public String getMediumForm() {
        return value.length() >= 16 ? value.substring(0, 16) : value;
    }

    /**
     * 获取TraceId的十六进制形式（去除连字符）
     */
    public String toHexString() {
        return value.replace("-", "").toLowerCase();
    }

    /**
     * 获取TraceId的UUID形式（如果是32位十六进制，转换为UUID格式）
     */
    public String toUUIDString() {
        if (isUUIDFormat()) {
            return value;
        }
        if (isHexFormat() && value.length() == 32) {
            return value.substring(0, 8) + "-" +
                    value.substring(8, 12) + "-" +
                    value.substring(12, 16) + "-" +
                    value.substring(16, 20) + "-" +
                    value.substring(20, 32);
        }
        return value;
    }

    /**
     * 提取时间戳部分（如果包含）
     */
    public String extractTimestamp() {
        if (!hasTimestamp()) {
            return null;
        }
        String[] parts = value.split("-");
        // 尝试解析第一部分是否为时间戳
        try {
            Long.parseLong(parts[0]);
            return parts[0];
        } catch (NumberFormatException e) {
            // 尝试解析第二部分
            if (parts.length > 1) {
                try {
                    Long.parseLong(parts[1]);
                    return parts[1];
                } catch (NumberFormatException e2) {
                    return null;
                }
            }
            return null;
        }
    }

    // ==================== 层次结构操作 ====================

    /**
     * 生成子TraceId
     */
    public FmkTraceId generateChild() {
        return generateChild(this);
    }

    /**
     * 生成兄弟TraceId（相同层级的新TraceId）
     */
    public FmkTraceId generateSibling() {
        // 如果是层次结构的TraceId，生成同级的新ID
        if (value.contains("-")) {
            String[] parts = value.split("-");
            if (parts.length >= 2) {
                String basePart = String.join("-", Arrays.copyOf(parts, parts.length - 1));
                String newSuffix = Integer.toHexString(ThreadLocalRandom.current().nextInt(0xFFFF));
                return new FmkTraceId(basePart + "-" + newSuffix);
            }
        }
        // 如果不是层次结构，生成新的TraceId
        return generate();
    }

    /**
     * 获取父TraceId（如果是层次结构）
     */
    public FmkTraceId getParent() {
        if (!value.contains("-")) {
            return null;
        }
        String[] parts = value.split("-");
        if (parts.length <= 1) {
            return null;
        }
        String parentValue = String.join("-", Arrays.copyOf(parts, parts.length - 1));
        try {
            return new FmkTraceId(parentValue);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    // ==================== 实用方法 ====================

    /**
     * 检查TraceId长度是否合适
     */
    public boolean hasValidLength() {
        return value.length() >= 16 && value.length() <= 64;
    }

    /**
     * 获取TraceId的层级深度
     */
    public int getDepth() {
        return value.split("-").length;
    }
}