package com.cjlabs.web.thread;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 线程池配置属性
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "thread-pool")
public class ThreadPoolProperties {

    private int corePoolSize = 8;
    private int maxPoolSize = 16;
    private int queueCapacity = 100;
    private int keepAliveSeconds = 60;
    private String threadNamePrefix = "ttl-async-";
    private boolean allowCoreThreadTimeOut = false;
    private int awaitTerminationSeconds = 60;
    private boolean throwOnRejection = true;


    /**
     * IO密集型任务配置
     */
    private IoConfig io = new IoConfig();

    /**
     * CPU密集型任务配置
     */
    private CpuConfig cpu = new CpuConfig();

    /**
     * 定时任务配置
     */
    // private ScheduledConfig scheduled = new ScheduledConfig();

    @Getter
    @Setter
    public static class IoConfig {
        private int corePoolSize = 50;
        private int maxPoolSize = 100;
        private int queueCapacity = 500;
        private int keepAliveSeconds = 300;
    }

    @Getter
    @Setter
    public static class CpuConfig {
        private int queueCapacity = 100;
        private int keepAliveSeconds = 60;
    }

    // @Getter
    // @Setter
    // public static class ScheduledConfig {
    //     private int poolSize = 10;
    // }
}
