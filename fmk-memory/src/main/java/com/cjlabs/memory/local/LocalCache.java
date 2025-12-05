package com.cjlabs.memory.local;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.stats.CacheStats;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * Caffeine 缓存封装组件
 * 提供简单易用的缓存操作接口
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
     * 获取缓存数据，如果不存在则加载
     *
     * @param key    缓存 key
     * @param loader 数据加载器（缓存未命中时调用）
     * @return 缓存的值
     */
    public V get(K key, Supplier<V> loader) {
        return cache.get(key, k -> {
            log.debug("缓存未命中: {} - key: {}", cacheName, key);
            return loader.get();
        });
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
        log.info("缓存统计 [{}] - 命中率: {}, 命中: {}, 未命中: {}, 加载: {}, 驱逐: {}",
                cacheName,
                stats.hitRate() * 100,
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
}