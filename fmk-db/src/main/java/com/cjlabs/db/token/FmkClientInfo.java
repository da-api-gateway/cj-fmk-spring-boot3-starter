package com.cjlabs.db.token;

import com.cjlabs.core.time.FmkInstantUtil;
import com.cjlabs.domain.enums.ClientTypeEnum;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

/**
 * 设备信息
 */
@Getter
@Setter
public class FmkClientInfo {

    private String userAgent;

    private String ipAddress;

    private String deviceVersion;

    private ClientTypeEnum clientType;

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
    private Instant loginTime;

    /**
     * 最后活跃时间
     */
    private Instant lastActiveTime;
    /**
     * 设备语言
     */
    private String deviceLanguage;

    public FmkClientInfo() {
        Instant now = FmkInstantUtil.now();

        this.loginTime = now;
        this.lastActiveTime = now;
    }

    public FmkClientInfo(String userAgent, String ipAddress, String deviceVersion) {
        this();
        this.userAgent = userAgent;
        this.ipAddress = ipAddress;
        this.deviceVersion = deviceVersion;
    }

    /**
     * 创建 FmkClientInfo 实例（使用 ClientTypeEnum）
     *
     * @param clientType 客户端类型枚举
     * @param ipAddress  IP地址
     * @param userAgent  User Agent
     * @return FmkClientInfo 实例
     */
    public static FmkClientInfo of(ClientTypeEnum clientType, String ipAddress, String userAgent) {
        FmkClientInfo clientInfo = new FmkClientInfo();
        clientInfo.setClientType(clientType);
        clientInfo.setIpAddress(ipAddress);
        clientInfo.setUserAgent(userAgent);
        return clientInfo;
    }

    /**
     * 更新最后活跃时间
     */
    public void updateLastActiveTime() {
        this.lastActiveTime = FmkInstantUtil.now();
    }
}