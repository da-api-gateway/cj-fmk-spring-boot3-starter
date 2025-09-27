package com.cjlabs.web.thread;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 自定义线程工厂，支持异常处理和线程命名
 */
@Slf4j
public class CustomThreadFactory implements ThreadFactory {
    private final AtomicInteger threadNumber = new AtomicInteger(1);
    private final String namePrefix;
    private final String executorName;
    private final ThreadGroup group;

    public CustomThreadFactory(String namePrefix, String executorName) {
        this.namePrefix = namePrefix.endsWith("-") ? namePrefix : namePrefix + "-";
        this.executorName = executorName;

        SecurityManager sm = System.getSecurityManager();
        this.group = (sm != null) ? sm.getThreadGroup() : Thread.currentThread().getThreadGroup();
    }

    @Override
    public Thread newThread(Runnable r) {
        String threadName = namePrefix + threadNumber.getAndIncrement();
        Thread thread = new Thread(group, r, threadName, 0);

        thread.setDaemon(false); // 明确设置为非守护线程
        thread.setPriority(Thread.NORM_PRIORITY);

        thread.setUncaughtExceptionHandler((t, e) -> {
            log.error("❌ Uncaught exception in thread | executor={} | thread={} | error={}",
                    executorName, t.getName(), e.getMessage(), e);
        });

        log.info("✅ Created new thread | executor={} | thread={}", executorName, threadName);
        return thread;
    }
}