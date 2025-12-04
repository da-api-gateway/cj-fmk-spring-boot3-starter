package com.cjlabs.web;

import com.cjlabs.web.token.FmkTokenProperties;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Token 模块自动配置
 * 自动扫描并加载 token 包下的所有组件
 */
@Slf4j
@Configuration
@AutoConfiguration
@ComponentScan(basePackages = {
        // 扫描整个 token 包
        "com.cjlabs.web.token",
})
@EnableConfigurationProperties(FmkTokenProperties.class)
public class FmkTokenAutoConfiguration {

    public FmkTokenAutoConfiguration() {
        log.info("FmkTokenAutoConfiguration|初始化|Token模块自动配置加载");
    }

    /**
     * Token 模块启用配置
     * 当 fmk.token.enabled=true 时启用（默认启用）
     */
    @Configuration
    @ConditionalOnProperty(name = "fmk.token.enabled", havingValue = "true", matchIfMissing = true)
    public static class TokenEnabledConfiguration {
        public TokenEnabledConfiguration() {
            log.info("FmkTokenAutoConfiguration|TokenEnabledConfiguration|Token功能已启用");
        }
    }

    /**
     * Redis 存储配置
     * 当 fmk.token.type=redis 时启用
     */
    @Configuration
    @ConditionalOnProperty(name = "fmk.token.type", havingValue = "redis")
    public static class TokenRedisConfiguration {
        public TokenRedisConfiguration() {
            log.info("FmkTokenAutoConfiguration|TokenRedisConfiguration|Token Redis存储已启用");
        }
    }

    /**
     * 内存存储配置
     * 当 fmk.token.type=memory 时启用（默认）
     */
    @Configuration
    @ConditionalOnProperty(name = "fmk.token.type", havingValue = "memory", matchIfMissing = true)
    public static class TokenMemoryConfiguration {
        public TokenMemoryConfiguration() {
            log.info("FmkTokenAutoConfiguration|TokenMemoryConfiguration|Token内存存储已启用");
        }
    }
}