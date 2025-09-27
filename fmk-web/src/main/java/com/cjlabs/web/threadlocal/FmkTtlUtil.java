package com.cjlabs.web.threadlocal;

import com.alibaba.ttl.TtlCallable;
import com.alibaba.ttl.TtlRunnable;
import com.alibaba.ttl.threadpool.TtlExecutors;

import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;

public class FmkTtlUtil {

    // ==================== 线程池包装 ====================

    /**
     * 包装Executor，使其支持TTL传递
     */
    public static Executor wrapExecutor(Executor executor) {
        return TtlExecutors.getTtlExecutor(executor);
    }

    /**
     * 包装ExecutorService，使其支持TTL传递
     */
    public static ExecutorService wrapExecutorService(ExecutorService executorService) {
        return TtlExecutors.getTtlExecutorService(executorService);
    }

    /**
     * 包装ScheduledExecutorService，使其支持TTL传递
     */
    public static ScheduledExecutorService wrapScheduledExecutorService(ScheduledExecutorService scheduledExecutorService) {
        return TtlExecutors.getTtlScheduledExecutorService(scheduledExecutorService);
    }


    // ==================== 任务包装 ====================

    /**
     * 包装Runnable，使其支持TTL传递
     */
    public static Runnable wrapRunnable(Runnable runnable) {
        return TtlRunnable.get(runnable);
    }

    /**
     * 包装Callable，使其支持TTL传递
     */
    public static <T> Callable<T> wrapCallable(Callable<T> callable) {
        return TtlCallable.get(callable);
    }


}
