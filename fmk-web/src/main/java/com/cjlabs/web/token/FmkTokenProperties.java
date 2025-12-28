package com.cjlabs.web.token;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Token 模块配置属性
 *
 * 配置示例：
 * <pre>
 * fmk:
 *   token:
 *     enabled: true          # 是否启用 Token 功能
 *     type: memory           # 存储类型：memory（内存）或 redis（Redis）
 *     memory:
 *       max-size: 10000      # 内存最大缓存数量
 *       expire-hours: 24     # 过期时间（小时）
 *     redis:
 *       key-prefix: "fmk:token:"  # Redis Key 前缀
 *       expire-hours: 12          # 过期时间（小时）
 * </pre>
 */
@Data
@ConfigurationProperties(prefix = "fmk.token")
public class FmkTokenProperties {

    /**
     * 是否启用 Token 模块
     */
    private boolean enabled = true;

    /**
     * Token 存储方式：memory（内存）/ redis（Redis）
     * <p>
     * TokenSaveTypeEnum.MEMORY
     */
    private String type = "memory";

    /**
     * Redis 配置（当 save=redis 时生效）
     */
    private RedisConfig redis = new RedisConfig();

    /**
     * 内存配置（当 save=memory 时生效）
     */
    private MemoryConfig memory = new MemoryConfig();

    /**
     * Redis 配置类
     */
    @Data
    public static class RedisConfig {
        /**
         * Redis Key 前缀
         */
        private String keyPrefix = "fmk:token:";

        /**
         * TTL 时间（小时）
         */
        private int expireHours = 12;
    }

    /**
     * 内存配置类
     */
    @Data
    public static class MemoryConfig {
        /**
         * 最大缓存数量
         * <p>
         * 超过这个数量，建议换redis
         */
        private int maxSize = 10000;

        /**
         * 过期时间（小时）
         */
        private int expireHours = 12;
    }


}