package com.cjlabs.web.threadlocal;

import com.cjlabs.core.types.longs.FmkUserId;
import com.cjlabs.core.types.strings.FmkSpanId;
import com.cjlabs.core.types.strings.FmkToken;
import com.cjlabs.core.types.strings.FmkTraceId;
import com.cjlabs.domain.enums.FmkLanguageEnum;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Getter
@Setter
@ToString(onlyExplicitlyIncluded = true)
public class FmkContextInfo {

    /**
     * 追踪ID
     */
    @ToString.Include
    private FmkTraceId traceId;

    /**
     * 追踪ID
     */
    @ToString.Include
    private FmkSpanId spanId;

    /**
     * 用户token
     */
    private FmkToken token;

    /**
     * 用户信息
     */
    private FmkUserInfo userInfo;

    /**
     * 语言代码
     */
    private FmkLanguageEnum language = FmkLanguageEnum.EN_US;

    /**
     * 请求时间
     */
    private LocalDateTime requestTime;

    /**
     * 请求URI
     */
    @ToString.Include
    private String requestUri;

    /**
     * 客户端信息
     */
    private ClientInfo clientInfo = new ClientInfo();

    /**
     * 请求头信息
     */
    private Map<String, String> headers = new ConcurrentHashMap<>();

    /**
     * 自定义属性
     */
    private Map<String, Object> attributes = new ConcurrentHashMap<>();

    /**
     * 创建一个基础的上下文信息实例
     */
    public static FmkContextInfo createBasic(FmkTraceId traceId, String requestUri) {
        FmkContextInfo info = new FmkContextInfo();
        info.traceId = traceId;
        info.requestTime = LocalDateTime.now();
        info.requestUri = requestUri;
        return info;
    }

    /**
     * 获取用户ID
     */
    @ToString.Include(name = "userId")
    public FmkUserId getUserId() {
        return userInfo != null ? userInfo.getUserId() : null;
    }

    /**
     * 设置用户ID和用户信息
     */
    public void setUserId(FmkUserId userId) {
        if (userId == null) {
            this.userInfo = null;
            return;
        }

        if (this.userInfo == null) {
            this.userInfo = new FmkUserInfo();
        }
        this.userInfo.setUserId(userId);
    }

    /**
     * 设置用户信息和用户ID
     */
    public FmkUserInfo setUserInfoAndUserId(FmkUserId inputUserId) {
        if (inputUserId == null) {
            this.userInfo = null;
            return null;
        }

        FmkUserInfo userInfo = new FmkUserInfo();
        userInfo.setUserId(inputUserId);
        this.userInfo = userInfo;
        return userInfo;
    }

    /**
     * 获取属性
     */
    public Object getAttribute(String key) {
        return attributes.get(key);
    }

    /**
     * 获取属性并转换为指定类型
     */
    @SuppressWarnings("unchecked")
    public <T> T getAttribute(String key, Class<T> type) {
        Object value = attributes.get(key);
        if (type.isInstance(value)) {
            return (T) value;
        }
        return null;
    }

    /**
     * 获取属性并包装为Optional
     */
    public <T> Optional<T> getAttributeOptional(String key, Class<T> type) {
        return Optional.ofNullable(getAttribute(key, type));
    }

    /**
     * 设置属性
     */
    public void setAttribute(String key, Object value) {
        attributes.put(key, value);
    }

    /**
     * 移除属性
     */
    public void removeAttribute(String key) {
        attributes.remove(key);
    }

    /**
     * 检查是否有用户信息
     */
    public boolean hasUserInfo() {
        return userInfo != null && userInfo.getUserId() != null;
    }

    /**
     * 获取请求头
     */
    public String getHeader(String name) {
        return headers.get(name);
    }

    /**
     * 设置请求头
     */
    public void setHeader(String name, String value) {
        headers.put(name, value);
    }
}