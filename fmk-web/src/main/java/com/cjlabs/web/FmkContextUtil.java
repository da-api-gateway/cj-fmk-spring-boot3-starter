package com.cjlabs.web;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.xodo.business.common.user.enums.DeviceTypeEnum;
import com.xodo.fmk.common.LanguageEnum;
import com.xodo.fmk.core.ClientInfo;
import com.xodo.fmk.core.FmkContextInfo;
import com.xodo.fmk.core.FmkUserInfo;
import com.xodo.fmk.jdk.basetype.type.FmkToken;
import com.xodo.fmk.jdk.basetype.type.FmkTraceId;
import com.xodo.fmk.jdk.basetype.type.FmkUserId;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.Optional;

@Slf4j
public class FmkContextUtil {

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

    public static LanguageEnum getCurrentLanguageCode() {
        try {
            Optional<FmkContextInfo> contextInfoOptional = FmkContextUtil.getContextInfo();
            if (contextInfoOptional.isPresent()) {
                FmkContextInfo fmkContextInfo = contextInfoOptional.get();
                return Optional.ofNullable(fmkContextInfo.getLanguage()).orElse(LanguageEnum.EN);
            }
        } catch (Exception e) {
            log.error("FmkContextUtil|getCurrentLanguageCode|e={}", e.getMessage(), e);
        }
        return LanguageEnum.EN;
    }

    public static Optional<FmkTraceId> getTraceId() {
        try {
            Optional<FmkContextInfo> contextInfoOptional = FmkContextUtil.getContextInfo();
            if (contextInfoOptional.isPresent()) {
                FmkContextInfo fmkContextInfo = contextInfoOptional.get();
                return Optional.ofNullable(fmkContextInfo.getTraceId());
            }
        } catch (Exception e) {
            log.info("FmkContextUtil|getTraceId|e={}", e.getMessage(), e);
        }
        return Optional.empty();
    }

    /**
     * 🔥 新增：获取当前用户ID
     */
    public static Optional<FmkToken> getToken() {
        try {
            Optional<FmkContextInfo> contextInfoOptional = getContextInfo();
            if (contextInfoOptional.isPresent()) {
                FmkContextInfo contextInfo = contextInfoOptional.get();
                return Optional.ofNullable(contextInfo.getToken());
            }
        } catch (Exception e) {
            log.warn("FmkContextUtil|getToken|获取用户token失败: {}", e.getMessage(), e);
        }
        return Optional.empty();
    }

    /**
     * 🔥 新增：获取当前用户ID
     */
    public static Optional<FmkUserId> getUserId() {
        try {
            Optional<FmkContextInfo> contextInfoOptional = getContextInfo();
            if (contextInfoOptional.isPresent()) {
                FmkContextInfo contextInfo = contextInfoOptional.get();
                return Optional.ofNullable(contextInfo.getUserId());
            }
        } catch (Exception e) {
            log.warn("FmkContextUtil|getUserId|获取用户ID失败: {}", e.getMessage(), e);
        }
        return Optional.empty();
    }

    /**
     * 🔥 新增：获取当前用户ID
     */
    public static Optional<FmkUserInfo> getUserInfo() {
        try {
            Optional<FmkContextInfo> contextInfoOptional = getContextInfo();
            if (contextInfoOptional.isPresent()) {
                FmkContextInfo contextInfo = contextInfoOptional.get();
                return Optional.ofNullable(contextInfo.getUserInfo());
            }
        } catch (Exception e) {
            log.warn("FmkContextUtil|getUserId|获取用户ID失败: {}", e.getMessage(), e);
        }
        return Optional.empty();
    }

    public static Optional<ClientInfo> getClientInfo() {
        try {
            Optional<FmkContextInfo> contextInfoOptional = FmkContextUtil.getContextInfo();
            if (contextInfoOptional.isPresent()) {
                FmkContextInfo fmkContextInfo = contextInfoOptional.get();
                return Optional.ofNullable(fmkContextInfo.getClientInfo());
            }
        } catch (Exception e) {
            log.info("FmkContextUtil|getCurrentLanguageCode|e={}", e.getMessage(), e);
        }
        return Optional.empty();
    }

    public static DeviceTypeEnum getDeviceType() {
        DeviceTypeEnum result = DeviceTypeEnum.WEB;
        try {
            Optional<FmkContextInfo> contextInfoOptional = FmkContextUtil.getContextInfo();
            if (contextInfoOptional.isPresent()) {
                FmkContextInfo fmkContextInfo = contextInfoOptional.get();
                ClientInfo clientInfo = fmkContextInfo.getClientInfo();
                if (Objects.isNull(clientInfo)) {
                    return result;
                }
                DeviceTypeEnum deviceTypeEnum = clientInfo.getDeviceType();
                if (Objects.isNull(deviceTypeEnum)) {
                    return result;
                }
                return deviceTypeEnum;
            }
        } catch (Exception e) {
            log.info("FmkContextUtil|getCurrentLanguageCode|e={}", e.getMessage(), e);
        }
        return result;
    }
}
