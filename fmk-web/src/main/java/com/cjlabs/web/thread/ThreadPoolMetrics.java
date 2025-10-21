package com.cjlabs.web.thread;

import lombok.Getter;
import lombok.Setter;

/**
 * 线程池指标数据类
 */
@Getter
@Setter
public class ThreadPoolMetrics {
    private String threadNamePrefix;
    private int activeCount;
    private int poolSize;
    private int corePoolSize;
    private int maxPoolSize;
    private long taskCount;
    private long completedTaskCount;
    private int queueSize;
    private int queueRemainingCapacity;
    private long totalTasks;
    private long completedTasks;
    private long rejectedTasks;

}