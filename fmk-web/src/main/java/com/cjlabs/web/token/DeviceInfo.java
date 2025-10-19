package com.cjlabs.web.token;

import lombok.Getter;
import lombok.Setter;

/**
 * 设备信息
 */
@Getter
@Setter
public class DeviceInfo {
    private String userAgent;
    private String ipAddress;
    private String deviceVersion;
    /**
     * 操作系统信息
     */
    private String operatingSystem;

    /**
     * 浏览器信息
     */
    private String browser;

    /**
     * 浏览器版本
     */
    private String browserVersion;

    /**
     * 登录时间
     */
    private Long loginTime;

    /**
     * 最后活跃时间
     */
    private Long lastActiveTime;
    /**
     * 设备语言
     */
    private String deviceLanguage;

    public DeviceInfo() {
        long now = System.currentTimeMillis();

        this.loginTime = now;
        this.lastActiveTime = now;
    }

    public DeviceInfo(String userAgent, String ipAddress, String deviceVersion) {
        this();
        this.userAgent = userAgent;
        this.ipAddress = ipAddress;
        this.deviceVersion = deviceVersion;
    }

    /**
     * 更新最后活跃时间
     */
    public void updateLastActiveTime() {
        long now = System.currentTimeMillis();

        this.lastActiveTime = now;
    }
}