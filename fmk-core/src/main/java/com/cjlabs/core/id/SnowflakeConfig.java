package com.cjlabs.core.id;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 雪花算法配置类（无机器ID版本）
 */
@Slf4j
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "snowflake")
public class SnowflakeConfig {

    /**
     * 时钟回拨容忍时间（毫秒）
     */
    private Long clockBackwardToleranceMs = 5L;

    /**
     * 创建雪花算法ID生成器Bean
     *
     * @return SnowflakeIdGenerator实例
     */
    @Bean
    public SnowflakeIdGenerator snowflakeIdGenerator() {
        SnowflakeIdGenerator generator = new SnowflakeIdGenerator(clockBackwardToleranceMs);

        log.info("SnowflakeConfig|snowflakeIdGenerator|雪花算法配置={}", generator.getConfigInfo());
        log.info("SnowflakeConfig|snowflakeIdGenerator|雪花算法性能={}", generator.getPerformanceInfo());
        return generator;
    }

}