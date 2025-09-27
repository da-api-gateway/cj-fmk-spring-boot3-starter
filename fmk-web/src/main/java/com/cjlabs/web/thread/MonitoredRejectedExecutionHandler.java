package com.cjlabs.web.thread;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 增强的拒绝策略，支持监控和告警
 */
@Slf4j
public class MonitoredRejectedExecutionHandler implements RejectedExecutionHandler {
    private final String executorName;
    private final AtomicInteger rejectedCount = new AtomicInteger(0);

    public MonitoredRejectedExecutionHandler(String executorName) {
        this.executorName = executorName;
    }

    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        int count = rejectedCount.incrementAndGet();

        // 记录详细的拒绝信息
        String status = getThreadPoolStatus(executor);
        log.error("ThreadPoolConfig|任务被拒绝执行|executor={}|拒绝次数={}|任务={}|线程池状态:[{}]", executorName, count, r.getClass().getSimpleName(), status);

        // 可以在这里添加告警逻辑
        if (count % 10 == 1) { // 每10次拒绝告警一次，避免告警风暴
            sendRejectionAlert(executorName, count, status);
        }

        // 根据配置决定是否抛出异常
        // if (properties.isThrowOnRejection()) {
        throw new RejectedExecutionException(
                String.format("线程池[%s]已满，任务被拒绝执行: %s", executorName, r.toString()));
        // }
        // else {
        //     使用调用者线程执行（慎用）
        // log.info("使用调用者线程执行被拒绝的任务: {}", r.getClass().getSimpleName());
        // if (!executor.isShutdown()) {
        //     r.run();
        // }
        // }
    }

    private void sendRejectionAlert(String executorName, int rejectedCount, String status) {
        // 这里可以集成告警系统
        log.error("ThreadPoolConfig|【告警】线程池拒绝任务|executor={}|累计拒绝次数={}|状态:[{}]",
                executorName, rejectedCount, status);
    }

    /**
     * 获取线程池状态信息
     */
    private String getThreadPoolStatus(ThreadPoolExecutor executor) {
        return String.format("活跃线程:%d, 池大小:%d, 最大池大小:%d, 队列大小:%d, 已完成任务:%d",
                executor.getActiveCount(),
                executor.getPoolSize(),
                executor.getMaximumPoolSize(),
                executor.getQueue().size(),
                executor.getCompletedTaskCount());
    }
}
