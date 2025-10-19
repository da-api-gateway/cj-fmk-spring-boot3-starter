package com.cjlabs.web.thread;

import lombok.extern.slf4j.Slf4j;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 自定义线程工厂 - 简化版
 */
@Slf4j
public class CustomThreadFactory implements ThreadFactory {
    private final AtomicInteger threadNumber = new AtomicInteger(1);
    private final String namePrefix;
    private final String poolName;
    private final ThreadGroup group;

    public CustomThreadFactory(String namePrefix, String poolName) {
        this.namePrefix = namePrefix.endsWith("-") ? namePrefix : namePrefix + "-";
        this.poolName = poolName;
        this.group = Thread.currentThread().getThreadGroup();
    }

    @Override
    public Thread newThread(Runnable r) {
        String threadName = namePrefix + threadNumber.getAndIncrement();

        // 使用传统线程
        Thread thread = new Thread(group, r, threadName);

        // 设置为非守护线程
        thread.setDaemon(false);

        // 设置正常优先级
        thread.setPriority(Thread.NORM_PRIORITY);

        // 设置异常处理器
        thread.setUncaughtExceptionHandler((t, e) -> {
            log.error("线程异常 | pool={} | thread={} | error={}",
                    poolName, t.getName(), e.getMessage(), e);
        });

        return thread;
    }
}