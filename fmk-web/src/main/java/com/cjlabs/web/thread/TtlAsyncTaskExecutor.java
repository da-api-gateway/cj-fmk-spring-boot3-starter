package com.cjlabs.web.thread;

import com.xodo.fmk.threadlocal.FmkTtlUtil;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * TTL支持的AsyncTaskExecutor实现
 */
class TtlAsyncTaskExecutor implements AsyncTaskExecutor {

    private final Executor ttlExecutor;
    private final ThreadPoolTaskExecutor delegate;

    public TtlAsyncTaskExecutor(Executor ttlExecutor, ThreadPoolTaskExecutor delegate) {
        this.ttlExecutor = ttlExecutor;
        this.delegate = delegate;
    }

    @Override
    public void execute(Runnable task) {
        ttlExecutor.execute(task);
    }

    @Override
    public void execute(Runnable task, long startTimeout) {
        ttlExecutor.execute(task);
    }

    @Override
    public java.util.concurrent.Future<?> submit(Runnable task) {
        return delegate.submit(FmkTtlUtil.wrapRunnable(task));
    }

    @Override
    public <T> java.util.concurrent.Future<T> submit(java.util.concurrent.Callable<T> task) {
        return delegate.submit(FmkTtlUtil.wrapCallable(task));
    }

    // 委托其他方法到原始executor
    public void shutdown() {
        delegate.shutdown();
    }

    public ThreadPoolExecutor getThreadPoolExecutor() {
        return delegate.getThreadPoolExecutor();
    }

    public int getCorePoolSize() {
        return delegate.getCorePoolSize();
    }

    public int getMaxPoolSize() {
        return delegate.getMaxPoolSize();
    }

    public int getActiveCount() {
        return delegate.getActiveCount();
    }

    public int getPoolSize() {
        return delegate.getPoolSize();
    }

}
