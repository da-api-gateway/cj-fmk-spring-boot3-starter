package com.cjlabs.cloud;

import com.cjlabs.web.exception.BusinessException;
import com.cjlabs.web.exception.BusinessExceptionEnum;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 单机锁工具类
 * 提供基于key的细粒度锁机制，支持读写锁、重入锁等
 * 适用于JDK21 + Spring Boot 3环境
 */
@Slf4j
public class FmkLockUtil {

    // 存储重入锁的映射
    private static final ConcurrentHashMap<String, ReentrantLock> REENTRANT_LOCKS = new ConcurrentHashMap<>();

    // 锁的最大数量，防止内存泄漏
    private static final int MAX_LOCK_SIZE = 10000;

    // ==================== 锁任务接口 ====================

    /**
     * 锁任务接口（有返回值）
     *
     * @param <T> 任务返回值的类型
     */
    @FunctionalInterface
    public interface LockTask<T> {
        T execute();
    }

    /**
     * 锁任务接口（无返回值）
     */
    @FunctionalInterface
    public interface VoidLockTask {
        void execute();
    }

    // ==================== 重入锁相关方法 ====================

    /**
     * 获取指定key的重入锁
     *
     * @param key 锁的key
     * @return ReentrantLock实例
     */
    public static ReentrantLock getReentrantLock(String key) {
        if (key == null || key.trim().isEmpty()) {
            throw new IllegalArgumentException("锁的key不能为空");
        }

        // 检查锁数量限制
        if (REENTRANT_LOCKS.size() >= MAX_LOCK_SIZE) {
            log.warn("重入锁数量已达到上限: {}, 请检查是否存在内存泄漏", MAX_LOCK_SIZE);
        }

        return REENTRANT_LOCKS.computeIfAbsent(key, k -> {
            log.debug("创建新的重入锁: {}", k);
            return new ReentrantLock(true); // 公平锁
        });
    }

    /**
     * 执行带锁的操作（无返回值）- 阻塞等待直到获取锁
     *
     * @param key    锁的key
     * @param action 要执行的操作
     */
    public static void executeWithLock(String key, VoidLockTask action) {
        executeWithLock(key, () -> {
            action.execute();
            return null;
        });
    }

    /**
     * 执行带锁的操作（有返回值）- 阻塞等待直到获取锁
     *
     * @param key      锁的key
     * @param supplier 要执行的操作
     * @param <T>      返回值类型
     * @return 操作结果
     */
    public static <T> T executeWithLock(String key, LockTask<T> supplier) {
        ReentrantLock lock = getReentrantLock(key);
        lock.lock();
        try {
            log.debug("FmkLockUtil|executeWithLock|getLock={}", key);
            return supplier.execute();
        } catch (Exception e) {
            log.error("FmkLockUtil|executeWithLock|执行操作失败|key={}|error={}", key, e.getMessage(), e);
            throw e;
        } finally {
            lock.unlock();
            log.debug("FmkLockUtil|executeWithLock|unlock key={}", key);
        }
    }

    /**
     * 执行带超时锁的操作，获取锁失败时抛出异常（无返回值）
     *
     * @param key    锁的key
     * @param action 要执行的操作
     * @throws BusinessException 获取锁超时时抛出
     */
    public static <T> T executeWithTryLockOrThrow(String key, LockTask<T> action) {
        return executeWithTryLockOrThrow(key, 5, TimeUnit.SECONDS, action);
    }

    /**
     * 执行带超时锁的操作，获取锁失败时抛出异常（无返回值）
     *
     * @param key    锁的key
     * @param action 要执行的操作
     * @throws BusinessException 获取锁超时时抛出
     */
    public static void executeWithTryLockOrThrow(String key, VoidLockTask action) {
        executeWithTryLockOrThrow(key, 5, TimeUnit.SECONDS, () -> {
            action.execute();
            return null;
        });
    }

    /**
     * 执行带超时锁的操作，获取锁失败时抛出异常（无返回值）
     *
     * @param key     锁的key
     * @param timeout 超时时间
     * @param unit    时间单位
     * @param action  要执行的操作
     * @throws BusinessException 获取锁超时时抛出
     */
    public static void executeWithTryLockOrThrow(String key, long timeout, TimeUnit unit, VoidLockTask action) {
        executeWithTryLockOrThrow(key, timeout, unit, () -> {
            action.execute();
            return null;
        });
    }

    /**
     * 执行带超时锁的操作，获取锁失败时抛出异常（有返回值）
     *
     * @param key      锁的key
     * @param timeout  超时时间
     * @param unit     时间单位
     * @param supplier 要执行的操作
     * @param <T>      返回值类型
     * @return 操作结果
     * @throws BusinessException 获取锁超时时抛出
     */
    public static <T> T executeWithTryLockOrThrow(String key, long timeout, TimeUnit unit, LockTask<T> supplier) {
        ReentrantLock lock = getReentrantLock(key);
        try {
            if (lock.tryLock(timeout, unit)) {
                try {
                    log.debug("FmkLockUtil|executeWithTryLockOrThrow|getLock={}", key);
                    return supplier.execute();
                } catch (Exception e) {
                    log.error("FmkLockUtil|executeWithTryLockOrThrow|执行操作失败|key={}|error={}", key, e.getMessage(), e);
                    throw e;
                } finally {
                    lock.unlock();
                    log.debug("FmkLockUtil|executeWithTryLockOrThrow|unlock key={}", key);
                }
            } else {
                log.warn("FmkLockUtil|executeWithTryLockOrThrow|获取锁超时|key={}|timeout={}ms", key, unit.toMillis(timeout));
                throw new BusinessException(BusinessExceptionEnum.RATE_LIMIT_EXCEEDED);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.warn("FmkLockUtil|executeWithTryLockOrThrow|线程被中断|key={}", key);
            throw new BusinessException(BusinessExceptionEnum.RATE_LIMIT_EXCEEDED);
        }
    }

    // ==================== 便利方法 ====================

    /**
     * 立即尝试获取锁并执行操作，获取不到锁立即抛出异常（无返回值）
     *
     * @param key    锁的key
     * @param action 要执行的操作
     * @throws BusinessException 获取锁失败时立即抛出
     */
    public static void executeWithTryLockOrThrowImmediately(String key, VoidLockTask action) {
        executeWithTryLockOrThrow(key, 1, TimeUnit.MILLISECONDS, action);
    }

    /**
     * 立即尝试获取锁并执行操作，获取不到锁立即抛出异常（有返回值）
     *
     * @param key      锁的key
     * @param supplier 要执行的操作
     * @param <T>      返回值类型
     * @return 操作结果
     * @throws BusinessException 获取锁失败时立即抛出
     */
    public static <T> T executeWithTryLockOrThrowImmediately(String key, LockTask<T> supplier) {
        return executeWithTryLockOrThrow(key, 1, TimeUnit.MILLISECONDS, supplier);
    }

    /**
     * 获取当前锁的数量
     *
     * @return 当前锁的数量
     */
    public static int getLockCount() {
        return REENTRANT_LOCKS.size();
    }

}