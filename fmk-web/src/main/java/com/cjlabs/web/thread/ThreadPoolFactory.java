package com.cjlabs.web.thread;

import org.springframework.core.task.AsyncTaskExecutor;
import java.util.Collection;

/**
 * 线程池工厂接口 - 采用工厂模式
 */
public interface ThreadPoolFactory {
    
    /**
     * 创建指定类型的线程池
     * 
     * @param type 线程池类型
     * @return 线程池实例
     */
    AsyncTaskExecutor createThreadPool(ThreadPoolTypeEnum type);
    
    /**
     * 获取自定义线程池构建器
     * 
     * @return 线程池构建器
     */
    ThreadPoolBuilder custom();
    
    /**
     * 关闭所有由此工厂创建的线程池
     */
    void shutdown();
    
    /**
     * 获取所有已创建的线程池
     * 
     * @return 线程池集合
     */
    Collection<TtlThreadPool> getManagedThreadPools();
}