package com.cjlabs.web.thread;

import com.cjlabs.web.threadlocal.FmkTtlUtil;

import com.alibaba.ttl.threadpool.TtlExecutors;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * TTL线程池包装类 - 采用装饰器模式
 */
@Slf4j
public class TtlThreadPool implements AsyncTaskExecutor {

    private final Executor ttlExecutor;
    private final ThreadPoolTaskExecutor delegate;
    @Getter
    private final String name;

    public TtlThreadPool(ThreadPoolTaskExecutor executor, String name) {
        this.delegate = executor;
        this.ttlExecutor = TtlExecutors.getTtlExecutor(executor.getThreadPoolExecutor());
        this.name = name;
    }

    @Override
    public void execute(Runnable task) {
        ttlExecutor.execute(task);
    }

    @Override
    public void execute(Runnable task, long startTimeout) {
        ttlExecutor.execute(task);
    }

    @Override
    public Future<?> submit(Runnable task) {
        return delegate.submit(FmkTtlUtil.wrapRunnable(task));
    }

    @Override
    public <T> Future<T> submit(Callable<T> task) {
        return delegate.submit(FmkTtlUtil.wrapCallable(task));
    }

    public void shutdown() {
        delegate.shutdown();
    }

    public ThreadPoolExecutor getThreadPoolExecutor() {
        return delegate.getThreadPoolExecutor();
    }

    /**
     * 获取线程池利用率
     *
     * @return 线程池利用率(0 - 1之间)
     */
    public double getUtilizationRate() {
        ThreadPoolExecutor executor = getThreadPoolExecutor();
        int poolSize = executor.getPoolSize();
        return poolSize > 0 ? (double) executor.getActiveCount() / poolSize : 0;
    }

    /**
     * 获取队列使用率
     *
     * @return 队列使用率(0 - 1之间)
     */
    public double getQueueUtilizationRate() {
        ThreadPoolExecutor executor = getThreadPoolExecutor();
        int queueSize = executor.getQueue().size();
        int queueCapacity = queueSize + executor.getQueue().remainingCapacity();
        return queueCapacity > 0 ? (double) queueSize / queueCapacity : 0;
    }

    /**
     * 检查线程池是否接近饱和
     *
     * @param threshold 阈值(0-1之间)
     * @return 是否接近饱和
     */
    public boolean isNearSaturation(double threshold) {
        return getUtilizationRate() > threshold || getQueueUtilizationRate() > threshold;
    }

    public ThreadPoolStatus getStatus() {
        ThreadPoolExecutor executor = getThreadPoolExecutor();
        return new ThreadPoolStatus(
                name,
                executor.getCorePoolSize(),
                executor.getMaximumPoolSize(),
                executor.getPoolSize(),
                executor.getActiveCount(),
                executor.getQueue().size(),
                executor.getCompletedTaskCount()
        );
    }

    /**
     * 线程池状态数据类
     */
    public record ThreadPoolStatus(
            String name,
            int corePoolSize,
            int maxPoolSize,
            int poolSize,
            int activeCount,
            int queueSize,
            long completedTaskCount
    ) {
        public double getUtilizationRate() {
            return poolSize > 0 ? (double) activeCount / poolSize : 0;
        }

        @Override
        public String toString() {
            return String.format(
                    "线程池[%s] - 活跃线程:%d/%d, 核心线程:%d, 最大线程:%d, 队列:%d, 已完成任务:%d, 利用率:%.2f%%",
                    name, activeCount, poolSize, corePoolSize, maxPoolSize,
                    queueSize, completedTaskCount, getUtilizationRate() * 100
            );
        }
    }
}