package com.cjlabs.web.thread;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.task.AsyncTaskExecutor;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

/**
 * 线程池工具类
 */
@Slf4j
public class ThreadPoolUtil {

    /**
     * 在指定线程池中异步执行任务，并自动选择备用线程池（如果主线程池接近饱和）
     * 
     * @param primary 主线程池
     * @param fallback 备用线程池
     * @param task 任务
     * @param saturationThreshold 饱和阈值(0-1)
     * @param <T> 返回类型
     * @return CompletableFuture
     */
    public static <T> CompletableFuture<T> runWithFallback(
            TtlThreadPool primary, 
            TtlThreadPool fallback,
            Supplier<T> task,
            double saturationThreshold) {
        
        AsyncTaskExecutor executor = primary;
        
        // 检查主线程池是否接近饱和
        if (primary.isNearSaturation(saturationThreshold)) {
            log.warn("主线程池[{}]接近饱和，切换到备用线程池[{}]", 
                    primary.getName(), fallback.getName());
            executor = fallback;
        }
        
        return CompletableFuture.supplyAsync(task, executor);
    }
    
    /**
     * 获取最适合的线程池类型
     * 
     * @param isIoBound 是否是IO密集型任务
     * @return 线程池类型
     */
    public static ThreadPoolTypeEnum getOptimalPoolType(boolean isIoBound) {
        return isIoBound ? ThreadPoolTypeEnum.IO : ThreadPoolTypeEnum.CPU;
    }
}