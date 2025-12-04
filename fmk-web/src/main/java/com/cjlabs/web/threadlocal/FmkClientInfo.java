package com.cjlabs.web.threadlocal;

import com.cjlabs.domain.enums.ClientTypeEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.Instant;

/**
 * 客户端信息，包含请求来源的设备、浏览器和网络信息
 */
@Getter
@Setter
@ToString(onlyExplicitlyIncluded = true)
public class FmkClientInfo {
    /**
     * IP地址
     */
    @ToString.Include
    private String ipAddress;

    /**
     * 用户代理
     */
    private String userAgent;

    /**
     * 设备类型
     */
    private ClientTypeEnum clientType;

    /**
     * 应用版本
     */
    private String deviceVersion;

    /**
     * 操作系统
     */
    @ToString.Include
    private String operatingSystem;

    /**
     * 浏览器
     */
    @ToString.Include
    private String browser;

    /**
     * 浏览器版本
     */
    private String browserVersion;

    /**
     * 屏幕分辨率
     */
    private String screenResolution;

    /**
     * 渠道来源
     */
    private String channel;

    /**
     * 引用页
     */
    private String referrer;

    /**
     * 最后活跃时间
     */
    private Instant lastActiveTime;

    /**
     * 更新最后活跃时间
     */
    public void updateLastActiveTime() {
        this.lastActiveTime = Instant.now();
    }
}