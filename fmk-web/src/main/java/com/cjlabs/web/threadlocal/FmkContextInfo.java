package com.cjlabs.web.threadlocal;

import com.cjlabs.core.types.longs.FmkUserId;
import com.cjlabs.core.types.strings.FmkToken;
import com.cjlabs.core.types.strings.FmkTraceId;
import com.cjlabs.domain.enums.FmkLanguageEnum;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Getter
@Setter
public class FmkContextInfo {

    /**
     * 追踪ID
     */
    private FmkTraceId traceId;

    /**
     * 用户token
     */
    private FmkToken token;

    /**
     * 用户ID
     */
    private FmkUserInfo userInfo;

    /**
     * 用户名
     */
    private FmkUserId userId;

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
     * 获取属性
     */
    public Object getAttribute(String key) {
        return attributes.get(key);
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


    public FmkUserInfo setUserInfoAndUserId(FmkUserId inputUserId) {
        FmkUserInfo userInfo = new FmkUserInfo();
        userInfo.setUserId(inputUserId);
        this.userInfo = userInfo;
        this.userId = inputUserId;
        return userInfo;
    }
}
