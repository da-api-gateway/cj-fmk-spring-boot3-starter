package com.cjlabs.memory.local;

import com.cjlabs.memory.lock.FmkLockUtil;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.stats.CacheStats;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * Caffeine 缓存封装组件（防缓存击穿版本）
 * 结合 FmkLockUtil 防止缓存击穿
 * <p>
 * 工作原理：
 * 1. 先查缓存，有则直接返回
 * 2. 缓存未命中时，使用 FmkLockUtil 获取互斥锁（3s 超时）
 * 3. 只有一个线程可以进入加载数据，其他线程等待
 * 4. 加载完成后，所有等待的线程都能从缓存获取数据
 */
@Slf4j
public class LocalCache<K, V> {

    private final Cache<K, V> cache;
    private final String cacheName;

    /**
     * 构造函数
     *
     * @param cacheName     缓存名称（用于日志）
     * @param maxSize       最大缓存数量
     * @param expireSeconds 过期时间（秒）
     */
    public LocalCache(String cacheName, long maxSize, long expireSeconds) {
        this.cacheName = cacheName;
        this.cache = Caffeine.newBuilder()
                .maximumSize(maxSize)
                .expireAfterWrite(expireSeconds, TimeUnit.SECONDS)
                .recordStats()
                .build();
        log.info("初始化缓存: {}, 最大容量: {}, 过期时间: {}秒", cacheName, maxSize, expireSeconds);
    }

    /**
     * 获取缓存数据（防缓存击穿版本）
     * <p>
     * 流程：
     * 1. 尝试从缓存获取数据
     * 2. 如果命中，直接返回
     * 3. 如果未命中，使用互斥锁保证只有一个线程加载
     * 4. 其他线程超时后降级处理
     *
     * @param key    缓存 key
     * @param loader 数据加载器（缓存未命中时调用，通常是数据库查询）
     * @return 缓存的值
     */
    public V get(K key, Function<K, V> loader) {
        // 第一步：尝试从缓存获取
        V value = cache.getIfPresent(key);
        if (value != null) {
            log.debug("缓存命中: {} - key: {}", cacheName, key);
            return value;
        }

        log.debug("缓存未命中，尝试使用互斥锁获取: {} - key: {}", cacheName, key);

        try {
            // 第二步：使用互斥锁，防止缓存击穿
            // 只有一个线程能进去加载，其他线程等待 500ms 后直接加载
            return FmkLockUtil.executeTryLock(
                    generateLockKey(key),
                    3,
                    TimeUnit.SECONDS,
                    () -> {
                        // 双重检查：可能其他线程已经加载了
                        V cachedValue = cache.getIfPresent(key);
                        if (cachedValue != null) {
                            log.debug("其他线程已加载缓存: {} - key: {}", cacheName, key);
                            return cachedValue;
                        }

                        // 真正的加载
                        log.info("加载数据到缓存: {} - key: {}", cacheName, key);
                        long startTime = System.currentTimeMillis();

                        V loadedValue = loader.apply(key);

                        long costTime = System.currentTimeMillis() - startTime;
                        log.info("数据加载完成: {} - key: {}, 耗时: {}ms", cacheName, key, costTime);

                        // 放入缓存
                        if (loadedValue != null) {
                            cache.put(key, loadedValue);
                        }

                        return loadedValue;
                    }
            );

        } catch (com.cjlabs.domain.exception.Error200Exception e) {
            // 获取锁超时（500ms）降级处理：直接查询数据库
            log.warn("获取锁超时（500ms），降级直接查询: {} - key: {}", cacheName, key);

            V loadedValue = loader.apply(key);
            if (loadedValue != null) {
                cache.put(key, loadedValue);
            }
            return loadedValue;
        }
    }

    /**
     * 直接获取缓存（不加载）
     *
     * @param key 缓存 key
     * @return 缓存的值，不存在返回 null
     */
    public V getIfPresent(K key) {
        return cache.getIfPresent(key);
    }

    /**
     * 手动放入缓存
     *
     * @param key   缓存 key
     * @param value 缓存值
     */
    public void put(K key, V value) {
        cache.put(key, value);
        log.debug("手动放入缓存: {} - key: {}", cacheName, key);
    }

    /**
     * 删除指定缓存
     *
     * @param key 缓存 key
     */
    public void deleteByKey(K key) {
        cache.invalidate(key);
        log.info("删除缓存: {} - key: {}", cacheName, key);
    }

    /**
     * 清空所有缓存
     */
    public void deleteAll() {
        cache.invalidateAll();
        log.info("清空所有缓存: {}", cacheName);
    }

    /**
     * 获取缓存统计信息
     *
     * @return 缓存统计
     */
    public CacheStats stats() {
        return cache.stats();
    }

    /**
     * 打印缓存统计信息
     */
    public void logStats() {
        CacheStats stats = cache.stats();
        double hitRate = stats.hitRate() * 100;
        log.info("缓存统计 [{}] - 命中率: {}%, 命中: {}, 未命中: {}, 加载: {}, 驱逐: {}",
                cacheName,
                String.format("%.2f", hitRate),
                stats.hitCount(),
                stats.missCount(),
                stats.loadCount(),
                stats.evictionCount());
    }

    /**
     * 获取缓存大小
     *
     * @return 当前缓存条目数
     */
    public long size() {
        return cache.estimatedSize();
    }

    /**
     * 生成锁的 key（缓存名 + 数据 key）
     * 确保不同的缓存数据使用不同的锁
     */
    private String generateLockKey(K key) {
        return cacheName + ":" + key;
    }
}