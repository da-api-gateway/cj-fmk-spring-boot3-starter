package com.cjlabs.core.types.strings;

import com.cjlabs.core.types.base.BaseStringType;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 令牌类型安全包装类
 * 提供系统中身份验证和授权的令牌管理
 */
public class FmkToken extends BaseStringType<FmkToken> {

    private static final String TOKEN_PREFIX = "TOKEN_";
    
    // Token 格式正则表达式
    private static final Pattern TOKEN_PATTERN = Pattern.compile(
            "^TOKEN_[a-zA-Z0-9\\-_]{16,64}$"
    );
    
    private static final DateTimeFormatter TIMESTAMP_FORMATTER =
            DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");
    
    // 线程本地变量存储当前线程的Token
    private static final ThreadLocal<FmkToken> THREAD_LOCAL = new ThreadLocal<>();

    // ==================== 静态工厂方法 ====================

    /**
     * 从字符串创建Token，支持null值
     */
    public static FmkToken ofNullable(String value) {
        return ofNullable(value, FmkToken::new);
    }

    /**
     * 从Object值创建Token，支持JSON反序列化
     */
    public static FmkToken ofNullable(Object value) {
        return ofNullableObject(value, FmkToken::new, String.class);
    }

    /**
     * 从字符串创建Token，不允许null或空值
     */
    public static FmkToken of(String value) {
        if (StringUtils.isBlank(value)) {
            throw new IllegalArgumentException("Token cannot be null or empty");
        }
        return new FmkToken(value.trim());
    }

    /**
     * 生成一个新的基本Token
     */
    public static FmkToken generate() {
        return new FmkToken(TOKEN_PREFIX + UUID.randomUUID().toString().replace("-", ""));
    }

    /**
     * 生成一个带时间戳的Token
     */
    public static FmkToken generateWithTimestamp() {
        String timestamp = LocalDateTime.now().format(TIMESTAMP_FORMATTER);
        String randomPart = UUID.randomUUID().toString().replace("-", "");
        return new FmkToken(TOKEN_PREFIX + timestamp + "-" + randomPart);
    }

    /**
     * 生成一个自定义Token
     */
    public static FmkToken generateCustom(String suffix) {
        if (StringUtils.isBlank(suffix)) {
            throw new IllegalArgumentException("Suffix cannot be null or empty");
        }
        String randomPart = UUID.randomUUID().toString().replace("-", "").substring(0, 16);
        return new FmkToken(TOKEN_PREFIX + randomPart + "-" + suffix);
    }

    /**
     * 将Token集合转换为字符串集合
     */
    public static List<String> toStringList(Collection<FmkToken> tokens) {
        if (tokens == null || tokens.isEmpty()) {
            return Collections.emptyList();
        }
        return tokens.stream()
                .filter(Objects::nonNull)
                .map(FmkToken::getValue)
                .collect(Collectors.toList());
    }
    
    /**
     * 将字符串集合转换为Token集合
     */
    public static List<FmkToken> fromStringList(Collection<String> values) {
        if (values == null || values.isEmpty()) {
            return Collections.emptyList();
        }
        return values.stream()
                .filter(StringUtils::isNotBlank)
                .map(FmkToken::of)
                .collect(Collectors.toList());
    }
    
    // ==================== 线程本地存储方法 ====================
    
    /**
     * 获取当前线程的Token
     */
    public static FmkToken current() {
        FmkToken token = THREAD_LOCAL.get();
        if (token == null) {
            token = generate();
            THREAD_LOCAL.set(token);
        }
        return token;
    }
    
    /**
     * 设置当前线程的Token
     */
    public static void setCurrent(FmkToken token) {
        THREAD_LOCAL.set(token);
    }
    
    /**
     * 清除当前线程的Token
     */
    public static void clear() {
        THREAD_LOCAL.remove();
    }

    // ==================== 构造方法 ====================

    public FmkToken(String value) {
        super(value);
        if (StringUtils.isBlank(value)) {
            throw new IllegalArgumentException("Token cannot be null or empty");
        }
        if (!isValidFormat(value.trim())) {
            throw new IllegalArgumentException("Invalid Token format: " + value);
        }
    }

    @Override
    protected FmkToken newInstance(String value) {
        return new FmkToken(value);
    }

    // ==================== Token 类型判断 ====================

    /**
     * 检查Token格式是否有效
     */
    private static boolean isValidFormat(String value) {
        return TOKEN_PATTERN.matcher(value).matches();
    }

    /**
     * 检查是否包含时间戳信息
     */
    public boolean hasTimestamp() {
        return value.contains("-") && value.split("-").length >= 2;
    }

    // ==================== Token 操作 ====================

    /**
     * 获取不带前缀的Token值
     */
    public String getTokenWithoutPrefix() {
        return value.substring(TOKEN_PREFIX.length());
    }

    /**
     * 获取Token的简短形式（前8个字符，包含前缀）
     */
    public String getShortForm() {
        return value.length() > TOKEN_PREFIX.length() + 8 ? 
               value.substring(0, TOKEN_PREFIX.length() + 8) : value;
    }

    /**
     * 提取时间戳部分（如果包含）
     */
    public String extractTimestamp() {
        if (!hasTimestamp()) {
            return null;
        }
        String tokenWithoutPrefix = getTokenWithoutPrefix();
        String[] parts = tokenWithoutPrefix.split("-");
        // 尝试解析第一部分是否为时间戳
        try {
            Long.parseLong(parts[0]);
            return parts[0];
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * 检查是否为临时Token
     */
    public boolean isTemporary() {
        return value.contains("TEMP");
    }

    /**
     * 生成临时Token
     */
    public FmkToken toTemporary() {
        if (isTemporary()) {
            return this;
        }
        return new FmkToken(TOKEN_PREFIX + "TEMP-" + getTokenWithoutPrefix());
    }

    /**
     * 检查Token是否已过期（仅当Token包含时间戳时有效）
     */
    public boolean isExpired(long expirationMillis) {
        String timestamp = extractTimestamp();
        if (timestamp == null) {
            return false;
        }
        try {
            LocalDateTime tokenTime = LocalDateTime.parse(timestamp, TIMESTAMP_FORMATTER);
            LocalDateTime expirationTime = tokenTime.plusNanos(expirationMillis * 1000000);
            return LocalDateTime.now().isAfter(expirationTime);
        } catch (Exception e) {
            return false;
        }
    }
}