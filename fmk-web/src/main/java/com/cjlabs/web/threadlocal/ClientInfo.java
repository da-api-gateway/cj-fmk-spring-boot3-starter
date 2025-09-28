package com.cjlabs.web.threadlocal;

import lombok.Getter;
import lombok.Setter;

/**
 * 客户端信息
 */
@Getter
@Setter
public class ClientInfo {
    /**
     * IP地址
     */
    private String ipAddress;

    /**
     * 用户代理
     */
    private String userAgent;

    /**
     * 设备类型
     */
    // private DeviceTypeEnum deviceType;

    /**
     * 应用版本
     */
    private String deviceVersion;

    /**
     * 操作系统
     */
    private String operatingSystem;

    /**
     * 浏览器
     */
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


    private String referrer;

}