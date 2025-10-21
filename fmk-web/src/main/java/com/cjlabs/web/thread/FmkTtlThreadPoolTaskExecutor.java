package com.cjlabs.web.thread;

import com.alibaba.ttl.TtlRunnable;
import com.alibaba.ttl.TtlCallable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.*;

/**
 * 支持TTL的线程池执行器
 * 继承自Spring的ThreadPoolTaskExecutor，对所有任务进行TTL包装
 */
@Slf4j
public class FmkTtlThreadPoolTaskExecutor extends ThreadPoolTaskExecutor {

    private static final long serialVersionUID = 1L;

    /**
     * 最后一次监控的时间戳
     */
    private long lastMonitorTime = System.currentTimeMillis();

    /**
     * 提交的任务总数
     */
    private long totalTasks = 0;

    /**
     * 完成的任务总数
     */
    private long completedTasks = 0;

    /**
     * 拒绝的任务总数
     */
    private long rejectedTasks = 0;

    /**
     * 是否启用监控
     */
    private final boolean enableMonitoring;

    /**
     * 监控统计间隔(毫秒)
     */
    private final long monitoringPeriodMs;

    public FmkTtlThreadPoolTaskExecutor(boolean enableMonitoring, int monitoringPeriodSeconds) {
        this.enableMonitoring = enableMonitoring;
        this.monitoringPeriodMs = monitoringPeriodSeconds * 1000L;
    }

    // 在 FmkTtlThreadPoolTaskExecutor 中添加
    @Override
    public void execute(Runnable task) {
        incrementTotalTasks();
        try {
            super.execute(TtlRunnable.get(task));
        } catch (RejectedExecutionException e) {
            incrementRejectedTasks();
            throw e;
        }
    }

    @Override
    public void execute(Runnable task, long startTimeout) {
        incrementTotalTasks();
        super.execute(TtlRunnable.get(task), startTimeout);
    }

    @Override
    public Future<?> submit(Runnable task) {
        incrementTotalTasks();
        return super.submit(TtlRunnable.get(task));
    }

    @Override
    public <T> Future<T> submit(Callable<T> task) {
        incrementTotalTasks();
        return super.submit(TtlCallable.get(task));
    }

    /**
     * 提交可获取CompletableFuture结果的任务
     */
    public <T> CompletableFuture<T> submitCompletable(Callable<T> task) {
        incrementTotalTasks();
        return CompletableFuture.supplyAsync(() -> {
            try {
                return TtlCallable.get(task).call();
            } catch (Exception e) {
                throw new CompletionException(e);
            }
        }, this);
    }

    /**
     * 提交可获取CompletableFuture结果的Runnable任务
     */
    public CompletableFuture<Void> submitCompletable(Runnable task) {
        incrementTotalTasks();
        return CompletableFuture.runAsync(TtlRunnable.get(task), this);
    }

    @Override
    protected void cancelRemainingTask(Runnable task) {
        super.cancelRemainingTask(TtlRunnable.get(task));
    }

    /**
     * 增加总任务计数并检查是否需要输出监控信息
     */
    private synchronized void incrementTotalTasks() {
        totalTasks++;
        checkAndLogMetrics();
    }

    /**
     * 增加完成任务计数
     */
    public synchronized void incrementCompletedTasks() {
        completedTasks++;
    }

    /**
     * 增加拒绝任务计数
     */
    public synchronized void incrementRejectedTasks() {
        rejectedTasks++;
    }

    /**
     * 检查是否需要输出监控信息
     */
    private void checkAndLogMetrics() {
        if (!enableMonitoring) {
            return;
        }

        long currentTime = System.currentTimeMillis();
        if (currentTime - lastMonitorTime >= monitoringPeriodMs) {
            logMetrics();
            lastMonitorTime = currentTime;
        }
    }

    /**
     * 输出监控信息
     */
    public synchronized void logMetrics() {
        if (!enableMonitoring) {
            return;
        }

        ThreadPoolExecutor executor = getThreadPoolExecutor();
        int activeCount = executor.getActiveCount();
        int poolSize = executor.getPoolSize();
        int corePoolSize = executor.getCorePoolSize();
        int maxPoolSize = executor.getMaximumPoolSize();
        long taskCount = executor.getTaskCount();
        long completedTaskCount = executor.getCompletedTaskCount();
        BlockingQueue<Runnable> queue = executor.getQueue();
        int queueSize = queue.size();
        int queueRemainingCapacity = queue.remainingCapacity();

        log.info("Thread Pool Metrics - [{}]:", getThreadNamePrefix());
        log.info("  Pool Size: {}/{} (current/max)", poolSize, maxPoolSize);
        log.info("  Active Threads: {}", activeCount);
        log.info("  Core Pool Size: {}", corePoolSize);
        log.info("  Queue: {}/{} (used/total)", queueSize, queueSize + queueRemainingCapacity);
        log.info("  Tasks: {} submitted, {} completed, {} rejected",
                totalTasks, completedTasks, rejectedTasks);
        log.info("  Executor Tasks: {} submitted, {} completed",
                taskCount, completedTaskCount);
    }

    /**
     * 获取监控指标的快照
     */
    public ThreadPoolMetrics getMetrics() {
        ThreadPoolExecutor executor = getThreadPoolExecutor();
        ThreadPoolMetrics metrics = new ThreadPoolMetrics();

        metrics.setThreadNamePrefix(getThreadNamePrefix());
        metrics.setActiveCount(executor.getActiveCount());
        metrics.setPoolSize(executor.getPoolSize());
        metrics.setCorePoolSize(executor.getCorePoolSize());
        metrics.setMaxPoolSize(executor.getMaximumPoolSize());
        metrics.setTaskCount(executor.getTaskCount());
        metrics.setCompletedTaskCount(executor.getCompletedTaskCount());
        metrics.setQueueSize(executor.getQueue().size());
        metrics.setQueueRemainingCapacity(executor.getQueue().remainingCapacity());
        metrics.setTotalTasks(totalTasks);
        metrics.setCompletedTasks(completedTasks);
        metrics.setRejectedTasks(rejectedTasks);

        return metrics;
    }

    public static class MonitoredRejectedExecutionHandler implements RejectedExecutionHandler {
        private final RejectedExecutionHandler delegate;
        private final FmkTtlThreadPoolTaskExecutor taskExecutor;

        public MonitoredRejectedExecutionHandler(RejectedExecutionHandler delegate, FmkTtlThreadPoolTaskExecutor taskExecutor) {
            this.delegate = delegate;
            this.taskExecutor = taskExecutor;
        }

        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            // 记录被拒绝的任务
            if (taskExecutor != null) {
                taskExecutor.incrementRejectedTasks();
                log.warn("任务被线程池拒绝执行，线程池名称：{}，当前活动线程数：{}，队列大小：{}",
                        taskExecutor.getThreadNamePrefix(),
                        executor.getActiveCount(),
                        executor.getQueue().size());
            }

            // 委托给原始的拒绝处理器
            delegate.rejectedExecution(r, executor);
        }
    }

}