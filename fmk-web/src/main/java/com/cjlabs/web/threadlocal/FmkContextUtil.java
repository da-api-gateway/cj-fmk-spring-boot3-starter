package com.cjlabs.web.threadlocal;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.cjlabs.core.types.longs.FmkUserId;
import com.cjlabs.core.types.strings.FmkToken;
import com.cjlabs.core.types.strings.FmkTraceId;
import com.cjlabs.domain.enums.FmkLanguageEnum;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

@Slf4j
public class FmkContextUtil {

    private FmkContextUtil() {
        // 私有构造函数，防止实例化
    }

    private static final ThreadLocal<FmkContextInfo> CONTEXT_HOLDER = new TransmittableThreadLocal<>();

    public static Optional<FmkContextInfo> getContextInfo() {
        return Optional.ofNullable(CONTEXT_HOLDER.get());
    }

    public static void setContextInfo(FmkContextInfo fmkContextInfo) {
        CONTEXT_HOLDER.set(fmkContextInfo);
    }

    public static void clear() {
        CONTEXT_HOLDER.remove();
    }

    /**
     * 通用方法：从上下文中获取指定属性
     *
     * @param extractor    属性提取函数
     * @param defaultValue 默认值
     * @param <T>          属性类型
     * @return 属性值或默认值
     */
    private static <T> T getFromContext(Function<FmkContextInfo, T> extractor, T defaultValue) {
        try {
            return getContextInfo()
                    .map(extractor)
                    .orElse(defaultValue);
        } catch (Exception e) {
            log.error("FmkContextUtil|getFromContext|e={}", e.getMessage(), e);
            return defaultValue;
        }
    }


    /**
     * 通用方法：从上下文中获取可选属性
     *
     * @param extractor 属性提取函数
     * @param <T>       属性类型
     * @return 包含属性的Optional
     */
    private static <T> Optional<T> getOptionalFromContext(Function<FmkContextInfo, T> extractor) {
        try {
            return getContextInfo()
                    .map(extractor);
        } catch (Exception e) {
            log.error("FmkContextUtil|getOptionalFromContext|e={}", e.getMessage(), e);
            return Optional.empty();
        }
    }

    public static FmkLanguageEnum getCurrentLanguageCode() {
        return getFromContext(
                ctx -> Optional
                        .ofNullable(ctx.getLanguage())
                        .orElse(FmkLanguageEnum.EN_US),
                FmkLanguageEnum.EN_US
        );
    }

    public static Optional<FmkTraceId> getTraceId() {
        return getOptionalFromContext(
                FmkContextInfo::getTraceId
        );
    }

    public static Optional<FmkToken> getToken() {
        return getOptionalFromContext(
                FmkContextInfo::getToken
        );
    }

    public static Optional<FmkUserId> getUserId() {
        return getOptionalFromContext(
                FmkContextInfo::getUserId
        );
    }

    public static Optional<FmkUserInfo> getUserInfo() {
        return getOptionalFromContext(
                FmkContextInfo::getUserInfo
        );
    }

    public static Optional<ClientInfo> getClientInfo() {
        return getOptionalFromContext(
                FmkContextInfo::getClientInfo
        );
    }

}
