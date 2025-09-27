package com.cjlabs.web.thread;

import com.alibaba.ttl.threadpool.TtlExecutors;
import com.xodo.fmk.json.FmkJacksonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.annotation.PreDestroy;
import java.util.concurrent.Executor;

@Slf4j
@Configuration
public class ThreadPoolConfig {
    @Autowired
    private ThreadPoolProperties properties;
    // @Autowired
    // private MeterRegistry meterRegistry;

    /**
     * 专用于IO密集型任务的线程池
     */
    @Bean("ioTaskExecutor")
    @Primary
    public AsyncTaskExecutor ioTaskExecutor() {
        log.info("ThreadPoolConfig|ioTaskExecutor|properties={}", FmkJacksonUtil.toJson(properties));
        return createTtlTaskExecutor(
                properties.getIo().getCorePoolSize(),
                properties.getIo().getMaxPoolSize(),
                properties.getIo().getQueueCapacity(),
                properties.getIo().getKeepAliveSeconds(),
                "io-task-",
                "io-task-executor"
        );

    }

    /**
     * 专用于CPU密集型任务的线程池
     */
    @Bean("cpuTaskExecutor")
    public AsyncTaskExecutor cpuTaskExecutor() {
        log.info("ThreadPoolConfig|cpuTaskExecutor|properties={}", FmkJacksonUtil.toJson(properties));
        int processors = Runtime.getRuntime().availableProcessors();
        return createTtlTaskExecutor(
                Math.max(1, processors),
                Math.max(2, processors * 2),
                properties.getCpu().getQueueCapacity(),
                properties.getCpu().getKeepAliveSeconds(),
                "cpu-task-",
                "cpu-task-executor"
        );
    }

    /**
     * 创建TTL支持的任务执行器
     */
    private AsyncTaskExecutor createTtlTaskExecutor(int corePoolSize,
                                                    int maxPoolSize,
                                                    int queueCapacity,
                                                    int keepAliveSeconds,
                                                    String threadNamePrefix,
                                                    String executorName) {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        // 基本参数配置
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setKeepAliveSeconds(keepAliveSeconds);
        executor.setThreadNamePrefix(threadNamePrefix);

        // 允许核心线程超时
        executor.setAllowCoreThreadTimeOut(properties.isAllowCoreThreadTimeOut());
        // 优雅关闭配置
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(properties.getAwaitTerminationSeconds());

        // 自定义拒绝策略
        executor.setRejectedExecutionHandler(new MonitoredRejectedExecutionHandler(executorName));

        // 自定义线程工厂
        executor.setThreadFactory(new CustomThreadFactory(threadNamePrefix, executorName));

        // 初始化线程池
        executor.initialize();

        // 添加监控
        // if (meterRegistry != null) {
        //     bindExecutorMetrics(executor.getThreadPoolExecutor(), executorName);
        // }

        // 使用TTL包装
        Executor ttlExecutor = TtlExecutors.getTtlExecutor(executor.getThreadPoolExecutor());
        return new TtlAsyncTaskExecutor(ttlExecutor, executor);
    }

    // /**
    //  * 绑定线程池监控指标
    //  */
    // private void bindExecutorMetrics(ThreadPoolExecutor executor, String executorName) {
    // if (meterRegistry == null) {
    //     log.info("ThreadPoolConfig|MeterRegistry未配置，跳过监控指标绑定|executor={}", executorName);
    //     return;
    // }
    //     try {
    //         ExecutorServiceMetrics.monitor(meterRegistry, executor, executorName);
    //         log.info("线程池监控指标绑定成功: {}", executorName);
    //     } catch (Exception e) {
    //         log.warn("线程池监控指标绑定失败: {}, 错误: {}", executorName, e.getMessage());
    //     }
    // }

    /**
     * 参数校验方法
     */
    private int validateAndGetCorePoolSize(int corePoolSize) {
        if (corePoolSize <= 0) {
            log.warn("ThreadPoolConfig|核心线程数无效，使用默认值|input={}|default=1", corePoolSize);
            return 1;
        }
        return corePoolSize;
    }

    private int validateAndGetMaxPoolSize(int maxPoolSize, int corePoolSize) {
        if (maxPoolSize < corePoolSize) {
            int defaultMax = Math.max(corePoolSize * 2, 2);
            log.warn("ThreadPoolConfig|最大线程数小于核心线程数，使用默认值|input={}|core={}|default={}",
                    maxPoolSize, corePoolSize, defaultMax);
            return defaultMax;
        }
        return maxPoolSize;
    }

    private int validateAndGetQueueCapacity(int queueCapacity) {
        if (queueCapacity < 0) {
            log.warn("ThreadPoolConfig|队列容量无效，使用默认值|input={}|default=100", queueCapacity);
            return 100;
        }
        return queueCapacity;
    }

    private int validateAndGetKeepAliveSeconds(int keepAliveSeconds) {
        if (keepAliveSeconds < 0) {
            log.warn("ThreadPoolConfig|线程存活时间无效，使用默认值|input={}|default=60", keepAliveSeconds);
            return 60;
        }
        return keepAliveSeconds;
    }


    private String validateAndGetThreadNamePrefix(String threadNamePrefix) {
        if (StringUtils.isNotBlank(threadNamePrefix)) {
            String defaultPrefix = "task-";
            log.warn("ThreadPoolConfig|线程名前缀为空，使用默认值|input={}|default={}", threadNamePrefix, defaultPrefix);
            return defaultPrefix;
        }
        return threadNamePrefix;
    }


    /**
     * 应用关闭时的清理工作
     */
    @PreDestroy
    public void destroy() {
        log.info("开始关闭线程池...");
        // Spring会自动处理Bean的销毁，这里可以添加额外的清理逻辑
    }

    /**
     * 线程池健康检查
     */
    @EventListener
    public void handleContextRefresh(ContextRefreshedEvent event) {
        log.info("应用启动完成，开始线程池健康检查");
        // 可以添加线程池状态检查逻辑
    }

}

