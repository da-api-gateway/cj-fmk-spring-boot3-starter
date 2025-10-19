package com.cjlabs.web.thread;

import lombok.extern.slf4j.Slf4j;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 拒绝处理策略 - 简化版
 */
@Slf4j
public class RejectionHandler implements RejectedExecutionHandler {
    private final String poolName;
    private final boolean throwOnRejection;
    private final AtomicInteger rejectedCount = new AtomicInteger(0);
    private static final int ALERT_THRESHOLD = 10;

    public RejectionHandler(String poolName, boolean throwOnRejection) {
        this.poolName = poolName;
        this.throwOnRejection = throwOnRejection;
    }

    @Override
    public void rejectedExecution(Runnable task, ThreadPoolExecutor executor) {
        int count = rejectedCount.incrementAndGet();
        
        String status = String.format(
            "活跃线程:%d/%d, 队列:%d/%d", 
            executor.getActiveCount(), 
            executor.getPoolSize(),
            executor.getQueue().size(),
            executor.getQueue().size() + executor.getQueue().remainingCapacity()
        );
        
        log.error("任务被拒绝 | pool={} | 拒绝次数={} | 任务={} | 状态:[{}]", 
                  poolName, count, task.getClass().getSimpleName(), status);

        if (count % ALERT_THRESHOLD == 1) {
            log.error("【告警】线程池拒绝任务过多 | pool={} | 累计次数={}", poolName, count);
        }

        if (throwOnRejection) {
            throw new RejectedExecutionException("线程池[" + poolName + "]已满，任务被拒绝");
        } else if (!executor.isShutdown()) {
            task.run(); // 使用调用者线程执行
        }
    }
}