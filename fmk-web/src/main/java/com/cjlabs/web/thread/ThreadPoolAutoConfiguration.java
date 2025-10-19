package com.cjlabs.web.thread;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.task.AsyncTaskExecutor;

/**
 * 线程池自动配置
 */
@AutoConfiguration
@EnableConfigurationProperties(ThreadPoolProperties.class)
@ConditionalOnProperty(prefix = "thread-pool", name = "enabled", havingValue = "true", matchIfMissing = true)
public class ThreadPoolAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public ThreadPoolFactory threadPoolFactory(ThreadPoolProperties properties) {
        return new DefaultThreadPoolFactory(properties);
    }

    @Bean("ioTaskExecutor")
    @Primary
    public AsyncTaskExecutor ioTaskExecutor(ThreadPoolFactory factory) {
        return factory.createThreadPool(ThreadPoolTypeEnum.IO);
    }

    @Bean("cpuTaskExecutor")
    public AsyncTaskExecutor cpuTaskExecutor(ThreadPoolFactory factory) {
        return factory.createThreadPool(ThreadPoolTypeEnum.CPU);
    }
    
    /**
     * 创建一个可动态调整的线程池
     */
    @Bean("dynamicTaskExecutor")
    @ConditionalOnProperty(prefix = "thread-pool", name = "dynamic-enabled", havingValue = "true")
    public AsyncTaskExecutor dynamicTaskExecutor(ThreadPoolFactory factory) {
        return factory.custom()
            .name("dynamic-executor")
            .corePoolSize(Runtime.getRuntime().availableProcessors())
            .maxPoolSize(Runtime.getRuntime().availableProcessors() * 4)
            .queueCapacity(1000)
            .queueType(QueueType.ARRAY)
            .build();
    }
}