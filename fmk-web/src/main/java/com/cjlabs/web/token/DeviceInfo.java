package com.cjlabs.web.token;

import com.xodo.business.common.user.enums.DeviceTypeEnum;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 设备信息
 */
@Getter
@Setter
public class DeviceInfo {
    private DeviceTypeEnum deviceType; // iphone, pc, android, web
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
    private LocalDateTime loginTime;

    /**
     * 最后活跃时间
     */
    private LocalDateTime lastActiveTime;
    /**
     * 设备语言
     */
    private String deviceLanguage;

    public DeviceInfo() {
        this.deviceType = DeviceTypeEnum.WEB; // 默认为 WEB
        this.loginTime = LocalDateTime.now();
        this.lastActiveTime = LocalDateTime.now();
    }

    public DeviceInfo(DeviceTypeEnum deviceType, String userAgent, String ipAddress, String deviceVersion) {
        this();
        this.deviceType = deviceType != null ? deviceType : DeviceTypeEnum.WEB;
        this.userAgent = userAgent;
        this.ipAddress = ipAddress;
        this.deviceVersion = deviceVersion;
    }

    /**
     * 更新最后活跃时间
     */
    public void updateLastActiveTime() {
        this.lastActiveTime = LocalDateTime.now();
    }
}