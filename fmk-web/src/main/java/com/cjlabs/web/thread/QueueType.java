package com.cjlabs.web.thread;

/**
 * 队列类型枚举
 */
public enum QueueType {
    /**
     * LinkedBlockingQueue - 无界队列
     */
    LINKED,
    
    /**
     * ArrayBlockingQueue - 有界队列
     */
    ARRAY,
    
    /**
     * SynchronousQueue - 同步队列，无缓冲
     */
    SYNCHRONOUS,
    
    /**
     * PriorityBlockingQueue - 优先级队列
     */
    PRIORITY
}