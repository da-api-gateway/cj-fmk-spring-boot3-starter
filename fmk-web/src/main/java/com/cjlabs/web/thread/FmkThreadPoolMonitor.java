package com.cjlabs.web.thread;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池监控服务
 */
@Slf4j
public class FmkThreadPoolMonitor {

    private final ThreadPoolTaskExecutor threadPoolTaskExecutor;
    private final ScheduledExecutorService scheduler;
    private final int monitoringPeriodSeconds;

    // 默认60秒
    public FmkThreadPoolMonitor(ThreadPoolTaskExecutor threadPoolTaskExecutor) {
        this(threadPoolTaskExecutor, 60);
    }

    public FmkThreadPoolMonitor(ThreadPoolTaskExecutor threadPoolTaskExecutor, int monitoringPeriodSeconds) {
        this.threadPoolTaskExecutor = threadPoolTaskExecutor;
        this.monitoringPeriodSeconds = monitoringPeriodSeconds;
        this.scheduler = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread(r, "thread-pool-monitor");
            t.setDaemon(true);
            return t;
        });

        // 启动定时监控
        startMonitoring();
    }

    /**
     * 启动定时监控
     */
    public void startMonitoring() {
        scheduler.scheduleAtFixedRate(
                this::monitorThreadPool,
                monitoringPeriodSeconds,
                monitoringPeriodSeconds,
                TimeUnit.SECONDS
        );
    }

    /**
     * 停止监控
     */
    public void stopMonitoring() {
        scheduler.shutdown();
    }

    /**
     * 监控线程池状态
     */
    public void monitorThreadPool() {
        if (threadPoolTaskExecutor instanceof FmkTtlThreadPoolTaskExecutor) {
            ((FmkTtlThreadPoolTaskExecutor) threadPoolTaskExecutor).logMetrics();
        } else {
            // 如果不是我们自定义的线程池，则使用标准方式记录信息
            ThreadPoolExecutor executor = threadPoolTaskExecutor.getThreadPoolExecutor();
            log.info("Thread Pool Status - [{}]:", threadPoolTaskExecutor.getThreadNamePrefix());
            log.info("  Pool Size: {}/{}", executor.getPoolSize(), executor.getMaximumPoolSize());
            log.info("  Active Threads: {}", executor.getActiveCount());
            log.info("  Queue Size: {}", executor.getQueue().size());
            log.info("  Task Count: {}", executor.getTaskCount());
            log.info("  Completed Tasks: {}", executor.getCompletedTaskCount());
        }
    }

    /**
     * 获取线程池指标
     */
    public Object getThreadPoolMetrics() {
        if (threadPoolTaskExecutor instanceof FmkTtlThreadPoolTaskExecutor) {
            return ((FmkTtlThreadPoolTaskExecutor) threadPoolTaskExecutor).getMetrics();
        } else {
            ThreadPoolExecutor executor = threadPoolTaskExecutor.getThreadPoolExecutor();
            return new Object() {
                public String getThreadNamePrefix() {
                    return threadPoolTaskExecutor.getThreadNamePrefix();
                }

                public int getActiveCount() {
                    return executor.getActiveCount();
                }

                public int getPoolSize() {
                    return executor.getPoolSize();
                }

                public int getCorePoolSize() {
                    return executor.getCorePoolSize();
                }

                public int getMaxPoolSize() {
                    return executor.getMaximumPoolSize();
                }

                public long getTaskCount() {
                    return executor.getTaskCount();
                }

                public long getCompletedTaskCount() {
                    return executor.getCompletedTaskCount();
                }

                public int getQueueSize() {
                    return executor.getQueue().size();
                }

                public int getQueueRemainingCapacity() {
                    return executor.getQueue().remainingCapacity();
                }
            };
        }
    }
}