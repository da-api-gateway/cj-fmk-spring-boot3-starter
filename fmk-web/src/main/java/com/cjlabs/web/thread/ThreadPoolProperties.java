package com.cjlabs.web.thread;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 线程池配置属性 - 增强版
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "thread-pool")
public class ThreadPoolProperties {
    private boolean enabled = true;
    private boolean allowCoreThreadTimeOut = false;
    private boolean preloadCoreThreads = false;
    private boolean throwOnRejection = true;
    private boolean dynamicEnabled = false;
    private boolean healthCheckEnabled = true;
    private long healthCheckIntervalMs = 60000;
    
    private IoConfig io = new IoConfig();
    private CpuConfig cpu = new CpuConfig();

    @Getter
    @Setter
    public static class IoConfig {
        private int corePoolSize = 50;
        private int maxPoolSize = 100;
        private int queueCapacity = 500;
        private int keepAliveSeconds = 300;
        private String threadNamePrefix = "io-task-";
        private QueueType queueType = QueueType.LINKED;
    }

    @Getter
    @Setter
    public static class CpuConfig {
        private int queueCapacity = 100;
        private int keepAliveSeconds = 60;
        private String threadNamePrefix = "cpu-task-";
        private QueueType queueType = QueueType.LINKED;
    }
}