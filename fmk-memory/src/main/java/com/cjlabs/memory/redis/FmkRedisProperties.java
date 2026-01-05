package com.cjlabs.memory.redis;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Redis 配置属性
 */
@Data
@ConfigurationProperties(prefix = "fmk.redis")
public class FmkRedisProperties {
    
    /**
     * 是否启用 Redis
     */
    private boolean enabled = true;
    
    /**
     * 默认过期时间（秒）
     */
    private long defaultExpireSeconds = 3600;
    
    /**
     * 分布式锁默认超时时间（秒）
     */
    private long lockTimeoutSeconds = 30;
    
    /**
     * 分布式锁等待时间（秒）
     */
    private long lockWaitSeconds = 3;
    
    /**
     * Key 前缀
     */
    private String keyPrefix = "fmk:";
}