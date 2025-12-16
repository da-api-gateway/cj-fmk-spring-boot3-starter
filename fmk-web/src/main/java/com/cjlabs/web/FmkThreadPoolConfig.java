package com.cjlabs.web;

import com.cjlabs.web.json.FmkJacksonUtil;
import com.cjlabs.web.thread.FmkThreadPoolMonitor;
import com.cjlabs.web.thread.FmkThreadPoolProperties;
import com.cjlabs.web.thread.FmkTtlThreadPoolTaskExecutor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 线程池配置类
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(FmkThreadPoolProperties.class)
public class FmkThreadPoolConfig {

    /**
     * 创建线程池执行器
     */
    @Bean
    @ConditionalOnMissingBean(name = "fmkThreadPoolTaskExecutor")
    public ThreadPoolTaskExecutor fmkThreadPoolTaskExecutor(FmkThreadPoolProperties properties) {
        log.info("Initializing FmkThreadPoolTaskExecutor with properties: {}", FmkJacksonUtil.toJson(properties));
        
        FmkTtlThreadPoolTaskExecutor executor = new FmkTtlThreadPoolTaskExecutor(
                properties.isEnableMonitoring(), 
                properties.getMonitoringPeriod());
        
        // 设置核心线程数
        executor.setCorePoolSize(properties.getCorePoolSize());
        // 设置最大线程数
        executor.setMaxPoolSize(properties.getMaxPoolSize());
        // 设置队列容量
        executor.setQueueCapacity(properties.getQueueCapacity());
        // 设置线程活跃时间（秒）
        executor.setKeepAliveSeconds(properties.getKeepAliveSeconds());
        // 设置默认线程名称前缀
        executor.setThreadNamePrefix(properties.getThreadNamePrefix());
        // 设置是否允许核心线程超时
        executor.setAllowCoreThreadTimeOut(properties.isAllowCoreThreadTimeOut());
        // 设置拒绝策略
        executor.setRejectedExecutionHandler(createRejectionPolicy(properties.getRejectionPolicy(), executor));
        // 等待所有任务结束后再关闭线程池
        executor.setWaitForTasksToCompleteOnShutdown(properties.isWaitForTasksToCompleteOnShutdown());
        // 设置线程池中任务的等待时间
        executor.setAwaitTerminationSeconds(properties.getAwaitTerminationSeconds());
        
        // 初始化线程池
        executor.initialize();
        
        // 如果启用监控，记录初始化信息
        if (properties.isEnableMonitoring()) {
            log.info("Thread pool monitoring enabled with period: {} seconds", properties.getMonitoringPeriod());
            // 初始化后立即记录一次指标
            executor.logMetrics();
        }
        
        return executor;
    }
    
    /**
     * 创建拒绝策略处理器
     */
    // FmkThreadPoolConfig.java 中的 createRejectionPolicy 方法
    private RejectedExecutionHandler createRejectionPolicy(String rejectionPolicy, FmkTtlThreadPoolTaskExecutor executor) {
        RejectedExecutionHandler handler;

        switch (rejectionPolicy.toUpperCase()) {
            case "ABORT":
                handler = new ThreadPoolExecutor.AbortPolicy();
                log.info("使用AbortPolicy拒绝策略");
                break;
            case "DISCARD":
                handler = new ThreadPoolExecutor.DiscardPolicy();
                log.info("使用DiscardPolicy拒绝策略");
                break;
            case "DISCARD_OLDEST":
                handler = new ThreadPoolExecutor.DiscardOldestPolicy();
                log.info("使用DiscardOldestPolicy拒绝策略");
                break;
            case "CALLER_RUNS":
            default:
                handler = new ThreadPoolExecutor.CallerRunsPolicy();
                log.info("使用CallerRunsPolicy拒绝策略");
                break;
        }

        // 包装拒绝处理器以进行监控
        return new FmkTtlThreadPoolTaskExecutor.MonitoredRejectedExecutionHandler(handler, executor);
    }
    
    /**
     * 创建线程池监控服务
     */
    @Bean
    @ConditionalOnMissingBean(FmkThreadPoolMonitor.class)
    public FmkThreadPoolMonitor threadPoolMonitor(@Qualifier("fmkThreadPoolTaskExecutor") ThreadPoolTaskExecutor fmkThreadPoolTaskExecutor) {
        return new FmkThreadPoolMonitor(fmkThreadPoolTaskExecutor);
    }
}