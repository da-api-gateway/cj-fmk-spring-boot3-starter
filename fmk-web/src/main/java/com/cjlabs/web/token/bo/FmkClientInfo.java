package com.cjlabs.web.token.bo;

import com.cjlabs.domain.enums.ClientTypeEnum;
import lombok.Data;

import java.time.Instant;

/**
 * 客户端信息
 */
@Data
public class FmkClientInfo {
    /**
     * 客户端类型
     */
    private ClientTypeEnum clientType;

    /**
     * IP 地址
     */
    private String ipAddress;

    /**
     * User-Agent
     */
    private String userAgent;

    /**
     * 设备版本
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