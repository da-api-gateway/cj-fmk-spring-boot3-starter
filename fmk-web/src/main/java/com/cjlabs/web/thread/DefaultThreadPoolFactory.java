package com.cjlabs.web.thread;

import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.task.AsyncTaskExecutor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 默认线程池工厂实现
 */
@Slf4j
public class DefaultThreadPoolFactory implements ThreadPoolFactory {
    
    private final ThreadPoolProperties properties;
    private final Map<String, TtlThreadPool> threadPools = new ConcurrentHashMap<>();
    
    public DefaultThreadPoolFactory(ThreadPoolProperties properties) {
        this.properties = properties;
    }
    
    @Override
    public AsyncTaskExecutor createThreadPool(ThreadPoolTypeEnum type) {
        return switch (type) {
            case IO -> createIoThreadPool();
            case CPU -> createCpuThreadPool();
            case CUSTOM -> throw new IllegalArgumentException("请使用custom()方法创建自定义线程池");
        };
    }
    
    @Override
    public ThreadPoolBuilder custom() {
        return new DefaultThreadPoolBuilder();
    }
    
    private AsyncTaskExecutor createIoThreadPool() {
        ThreadPoolBuilder builder = custom()
            .name("io-task-executor")
            .corePoolSize(properties.getIo().getCorePoolSize())
            .maxPoolSize(properties.getIo().getMaxPoolSize())
            .queueCapacity(properties.getIo().getQueueCapacity())
            .keepAliveSeconds(properties.getIo().getKeepAliveSeconds())
            .threadNamePrefix(properties.getIo().getThreadNamePrefix())
            .allowCoreThreadTimeOut(properties.isAllowCoreThreadTimeOut())
            .preloadCoreThreads(properties.isPreloadCoreThreads())
            .throwOnRejection(properties.isThrowOnRejection())
            .queueType(properties.getIo().getQueueType());
            
        AsyncTaskExecutor executor = builder.build();
        registerThreadPool(executor);
        return executor;
    }
    
    private AsyncTaskExecutor createCpuThreadPool() {
        int processors = Runtime.getRuntime().availableProcessors();
        
        ThreadPoolBuilder builder = custom()
            .name("cpu-task-executor")
            .corePoolSize(processors)
            .maxPoolSize(processors * 2)
            .queueCapacity(properties.getCpu().getQueueCapacity())
            .keepAliveSeconds(properties.getCpu().getKeepAliveSeconds())
            .threadNamePrefix(properties.getCpu().getThreadNamePrefix())
            .allowCoreThreadTimeOut(properties.isAllowCoreThreadTimeOut())
            .preloadCoreThreads(properties.isPreloadCoreThreads())
            .throwOnRejection(properties.isThrowOnRejection())
            .queueType(properties.getCpu().getQueueType());
            
        AsyncTaskExecutor executor = builder.build();
        registerThreadPool(executor);
        return executor;
    }
    
    private void registerThreadPool(AsyncTaskExecutor executor) {
        if (executor instanceof TtlThreadPool ttlPool) {
            threadPools.put(ttlPool.getName(), ttlPool);
        }
    }
    
    @Override
    public Collection<TtlThreadPool> getManagedThreadPools() {
        return new ArrayList<>(threadPools.values());
    }
    
    @Override
    @PreDestroy
    public void shutdown() {
        log.info("关闭所有线程池，共{}个", threadPools.size());
        threadPools.values().forEach(pool -> {
            try {
                log.info("关闭线程池: {}", pool.getName());
                pool.shutdown();
            } catch (Exception e) {
                log.error("关闭线程池失败: {}", pool.getName(), e);
            }
        });
    }
    
    @EventListener
    public void handleContextRefresh(ContextRefreshedEvent event) {
        log.info("应用启动完成，线程池状态报告");
        threadPools.values().forEach(pool -> {
            try {
                log.info(pool.getStatus().toString());
            } catch (Exception e) {
                log.error("获取线程池状态失败: {}", pool.getName(), e);
            }
        });
    }
}