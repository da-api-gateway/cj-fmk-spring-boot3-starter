package com.cjlabs.web.threadlocal;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.cjlabs.core.types.longs.FmkUserId;
import com.cjlabs.core.types.strings.FmkToken;
import com.cjlabs.core.types.strings.FmkTraceId;
import com.cjlabs.domain.enums.FmkLanguageEnum;

import lombok.extern.slf4j.Slf4j;

import java.util.Optional;
import java.util.function.Function;

/**
 * 上下文工具类，提供对线程上下文信息的访问方法
 */
@Slf4j
public class FmkContextUtil {

    private FmkContextUtil() {
        // 私有构造函数，防止实例化
    }

    private static final ThreadLocal<FmkContextInfo> CONTEXT_HOLDER = new TransmittableThreadLocal<>();

    /**
     * 获取上下文信息
     */
    public static Optional<FmkContextInfo> getContextInfo() {
        return Optional.ofNullable(CONTEXT_HOLDER.get());
    }

    /**
     * 设置上下文信息
     */
    public static void setContextInfo(FmkContextInfo fmkContextInfo) {
        CONTEXT_HOLDER.set(fmkContextInfo);
    }

    /**
     * 清除上下文信息
     */
    public static void clear() {
        CONTEXT_HOLDER.remove();
    }

    /**
     * 在当前上下文中执行操作
     */
    public static <T> Optional<T> executeWithContext(Function<FmkContextInfo, T> operation) {
        try {
            return getContextInfo().map(operation);
        } catch (Exception e) {
            log.error("FmkContextUtil|executeWithContext|执行失败|error={}", e.getMessage(), e);
            return Optional.empty();
        }
    }

    /**
     * 从上下文中获取指定属性，如果不存在则返回默认值
     */
    private static <T> T getFromContext(Function<FmkContextInfo, T> extractor, T defaultValue) {
        try {
            return getContextInfo()
                    .map(extractor)
                    .orElse(defaultValue);
        } catch (Exception e) {
            log.error("FmkContextUtil|getFromContext|获取失败|error={}", e.getMessage(), e);
            return defaultValue;
        }
    }

    /**
     * 从上下文中获取可选属性
     */
    private static <T> Optional<T> getOptionalFromContext(Function<FmkContextInfo, T> extractor) {
        try {
            return getContextInfo()
                    .map(extractor);
        } catch (Exception e) {
            log.error("FmkContextUtil|getOptionalFromContext|获取失败|error={}", e.getMessage(), e);
            return Optional.empty();
        }
    }

    /**
     * 获取当前语言设置
     */
    public static FmkLanguageEnum getCurrentLanguage() {
        return getFromContext(
                ctx -> Optional
                        .ofNullable(ctx.getLanguage())
                        .orElse(FmkLanguageEnum.EN_US),
                FmkLanguageEnum.EN_US
        );
    }

    /**
     * 获取追踪ID
     */
    public static Optional<FmkTraceId> getTraceId() {
        return getOptionalFromContext(FmkContextInfo::getTraceId);
    }

    /**
     * 获取追踪ID字符串，如果不存在则返回默认值
     */
    public static String getTraceIdString(String defaultValue) {
        return getTraceId()
                .map(FmkTraceId::getValue)
                .orElse(defaultValue);
    }

    /**
     * 获取Token
     */
    public static Optional<FmkToken> getToken() {
        return getOptionalFromContext(FmkContextInfo::getToken);
    }

    /**
     * 获取用户ID
     */
    public static Optional<FmkUserId> getUserId() {
        return getOptionalFromContext(FmkContextInfo::getUserId);
    }

    /**
     * 获取用户ID值，如果不存在则返回默认值
     */
    public static Long getUserIdValue(Long defaultValue) {
        return getUserId()
                .map(FmkUserId::getValue)
                .orElse(defaultValue);
    }

    /**
     * 获取用户信息
     */
    public static Optional<FmkUserInfo> getUserInfo() {
        return getOptionalFromContext(FmkContextInfo::getUserInfo);
    }

    /**
     * 获取客户端信息
     */
    public static Optional<ClientInfo> getClientInfo() {
        return getOptionalFromContext(FmkContextInfo::getClientInfo);
    }

    /**
     * 获取请求URI
     */
    public static Optional<String> getRequestUri() {
        return getOptionalFromContext(FmkContextInfo::getRequestUri);
    }

    /**
     * 获取请求头
     */
    public static Optional<String> getHeader(String name) {
        return getOptionalFromContext(ctx -> ctx.getHeader(name));
    }

    /**
     * 设置上下文属性
     */
    public static void setAttribute(String key, Object value) {
        executeWithContext(ctx -> {
            ctx.setAttribute(key, value);
            return null;
        });
    }

    /**
     * 获取上下文属性
     */
    public static <T> Optional<T> getAttribute(String key, Class<T> type) {
        return getOptionalFromContext(ctx -> ctx.getAttribute(key, type));
    }
}