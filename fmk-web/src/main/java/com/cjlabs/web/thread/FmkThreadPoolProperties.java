package com.cjlabs.web.thread;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "fmk.thread-pool")
public class FmkThreadPoolProperties {
    /**
     * 线程池名称前缀
     */
    private String threadNamePrefix = "fmk-async-";

    /**
     * 核心线程数
     */
    private int corePoolSize = 10;

    /**
     * 最大线程数
     */
    private int maxPoolSize = 50;

    /**
     * 队列容量
     */
    private int queueCapacity = 1000;

    /**
     * 线程池维护线程所允许的空闲时间(秒)
     */
    private int keepAliveSeconds = 60;

    /**
     * 是否允许核心线程超时
     */
    private boolean allowCoreThreadTimeOut = false;

    /**
     * 拒绝策略类型: ABORT, CALLER_RUNS, DISCARD, DISCARD_OLDEST
     */
    private String rejectionPolicy = "CALLER_RUNS";

    /**
     * 是否等待所有任务完成后再关闭线程池
     */
    private boolean waitForTasksToCompleteOnShutdown = true;

    /**
     * 线程池关闭的时候等待所有任务完成的最长时间(秒)
     */
    private int awaitTerminationSeconds = 60;

    /**
     * 是否启用监控
     */
    private boolean enableMonitoring = true;

    /**
     * 监控统计间隔(秒)
     */
    private int monitoringPeriod = 10;
}