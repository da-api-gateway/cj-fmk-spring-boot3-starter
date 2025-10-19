package com.cjlabs.web.thread;

import org.springframework.core.task.AsyncTaskExecutor;

/**
 * 线程池构建器接口 - 采用流式API和构建者模式
 */
public interface ThreadPoolBuilder {
    
    /**
     * 设置核心线程数
     */
    ThreadPoolBuilder corePoolSize(int corePoolSize);
    
    /**
     * 设置最大线程数
     */
    ThreadPoolBuilder maxPoolSize(int maxPoolSize);
    
    /**
     * 设置队列容量
     */
    ThreadPoolBuilder queueCapacity(int queueCapacity);
    
    /**
     * 设置线程存活时间(秒)
     */
    ThreadPoolBuilder keepAliveSeconds(int keepAliveSeconds);
    
    /**
     * 设置线程名前缀
     */
    ThreadPoolBuilder threadNamePrefix(String threadNamePrefix);
    
    /**
     * 设置是否允许核心线程超时
     */
    ThreadPoolBuilder allowCoreThreadTimeOut(boolean allowCoreThreadTimeOut);
    
    /**
     * 设置是否预热核心线程
     */
    ThreadPoolBuilder preloadCoreThreads(boolean preloadCoreThreads);
    
    /**
     * 设置拒绝策略是否抛出异常
     */
    ThreadPoolBuilder throwOnRejection(boolean throwOnRejection);
    
    /**
     * 设置线程池名称
     */
    ThreadPoolBuilder name(String name);
    
    /**
     * 设置队列类型
     */
    ThreadPoolBuilder queueType(QueueType queueType);
    
    /**
     * 构建线程池
     */
    AsyncTaskExecutor build();
}