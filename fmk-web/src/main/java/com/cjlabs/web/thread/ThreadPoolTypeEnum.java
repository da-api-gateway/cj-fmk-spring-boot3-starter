package com.cjlabs.web.thread;

/**
 * 线程池类型枚举
 */
public enum ThreadPoolTypeEnum {
    /**
     * IO密集型任务线程池
     */
    IO,
    
    /**
     * CPU密集型任务线程池
     */
    CPU,
    
    /**
     * 自定义线程池
     */
    CUSTOM
}