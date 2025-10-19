package com.cjlabs.web.thread;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 默认线程池构建器实现
 */
@Slf4j
public class DefaultThreadPoolBuilder implements ThreadPoolBuilder {
    private int corePoolSize = 1;
    private int maxPoolSize = 1;
    private int queueCapacity = 100;
    private int keepAliveSeconds = 60;
    private String threadNamePrefix = "task-";
    private boolean allowCoreThreadTimeOut = false;
    private boolean preloadCoreThreads = false;
    private boolean throwOnRejection = true;
    private String name = "custom-pool";
    private QueueType queueType = QueueType.LINKED;
    
    @Override
    public ThreadPoolBuilder corePoolSize(int corePoolSize) {
        this.corePoolSize = Math.max(1, corePoolSize);
        return this;
    }
    
    @Override
    public ThreadPoolBuilder maxPoolSize(int maxPoolSize) {
        this.maxPoolSize = Math.max(this.corePoolSize, maxPoolSize);
        return this;
    }
    
    @Override
    public ThreadPoolBuilder queueCapacity(int queueCapacity) {
        this.queueCapacity = Math.max(0, queueCapacity);
        return this;
    }
    
    @Override
    public ThreadPoolBuilder keepAliveSeconds(int keepAliveSeconds) {
        this.keepAliveSeconds = Math.max(1, keepAliveSeconds);
        return this;
    }
    
    @Override
    public ThreadPoolBuilder threadNamePrefix(String threadNamePrefix) {
        this.threadNamePrefix = threadNamePrefix;
        return this;
    }
    
    @Override
    public ThreadPoolBuilder allowCoreThreadTimeOut(boolean allowCoreThreadTimeOut) {
        this.allowCoreThreadTimeOut = allowCoreThreadTimeOut;
        return this;
    }
    
    @Override
    public ThreadPoolBuilder preloadCoreThreads(boolean preloadCoreThreads) {
        this.preloadCoreThreads = preloadCoreThreads;
        return this;
    }
    
    @Override
    public ThreadPoolBuilder throwOnRejection(boolean throwOnRejection) {
        this.throwOnRejection = throwOnRejection;
        return this;
    }
    
    @Override
    public ThreadPoolBuilder name(String name) {
        this.name = name;
        return this;
    }
    
    @Override
    public ThreadPoolBuilder queueType(QueueType queueType) {
        this.queueType = queueType;
        return this;
    }

    @Override
    public AsyncTaskExecutor build() {
        log.info("创建线程池: {}", name);

        // 创建Spring线程池任务执行器
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setKeepAliveSeconds(keepAliveSeconds);
        executor.setThreadNamePrefix(threadNamePrefix);
        executor.setAllowCoreThreadTimeOut(allowCoreThreadTimeOut);
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(60);

        // 创建自定义线程工厂
        CustomThreadFactory threadFactory = new CustomThreadFactory(threadNamePrefix, name);
        executor.setThreadFactory(threadFactory);

        // 创建拒绝策略
        RejectionHandler rejectionHandler = new RejectionHandler(name, throwOnRejection);
        executor.setRejectedExecutionHandler(rejectionHandler);

        // 根据队列类型设置队列
        switch (queueType) {
            case ARRAY:
                // 对于ArrayBlockingQueue，需要直接设置队列容量
                executor.setQueueCapacity(queueCapacity);
                break;
            case SYNCHRONOUS:
                // 对于SynchronousQueue，设置队列容量为0
                executor.setQueueCapacity(0);
                break;
            case PRIORITY:
                // 对于PriorityBlockingQueue，需要在初始化后替换队列
                executor.setQueueCapacity(queueCapacity);
                // 注意：这种方式不支持直接替换为PriorityBlockingQueue
                // 如果确实需要PriorityBlockingQueue，可能需要自定义ThreadPoolExecutor
                log.warn("PriorityBlockingQueue不被ThreadPoolTaskExecutor直接支持，使用默认队列");
                break;
            default:
                // LinkedBlockingQueue是默认队列类型
                executor.setQueueCapacity(queueCapacity);
                break;
        }

        // 初始化线程池
        executor.initialize();

        // 预热核心线程
        if (preloadCoreThreads) {
            executor.getThreadPoolExecutor().prestartAllCoreThreads();
        }

        // 创建TTL线程池
        return new TtlThreadPool(executor, name);
    }
}