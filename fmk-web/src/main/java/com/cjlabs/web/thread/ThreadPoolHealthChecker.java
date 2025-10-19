package com.cjlabs.web.thread;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 线程池健康检查
 */
@Slf4j
@Component
@ConditionalOnProperty(prefix = "thread-pool", name = "health-check-enabled", havingValue = "true", matchIfMissing = true)
public class ThreadPoolHealthChecker {

    private final ThreadPoolFactory threadPoolFactory;
    private static final double WARNING_THRESHOLD = 0.8; // 80%
    
    public ThreadPoolHealthChecker(ThreadPoolFactory threadPoolFactory) {
        this.threadPoolFactory = threadPoolFactory;
    }
    
    @Scheduled(fixedDelayString = "${thread-pool.health-check-interval-ms:60000}")
    public void checkHealth() {
        log.debug("执行线程池健康检查");
        
        for (TtlThreadPool pool : threadPoolFactory.getManagedThreadPools()) {
            double utilizationRate = pool.getUtilizationRate();
            double queueUtilizationRate = pool.getQueueUtilizationRate();
            
            if (utilizationRate > WARNING_THRESHOLD) {
                log.warn("线程池[{}]线程使用率过高: {:.2f}%", 
                        pool.getName(), utilizationRate * 100);
            }
            
            if (queueUtilizationRate > WARNING_THRESHOLD) {
                log.warn("线程池[{}]队列使用率过高: {:.2f}%", 
                        pool.getName(), queueUtilizationRate * 100);
            }
        }
    }
}