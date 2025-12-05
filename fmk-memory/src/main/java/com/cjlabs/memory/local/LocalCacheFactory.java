package com.cjlabs.memory.local;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Caffeine 缓存工厂
 * 统一管理所有缓存实例（已支持防缓存击穿）
 */
public class LocalCacheFactory {

    private final Map<String, LocalCache<?, ?>> cacheMap = new ConcurrentHashMap<>();

    /**
     * 创建或获取缓存实例
     *
     * @param cacheName     缓存名称
     * @param maxSize       最大容量
     * @param expireSeconds 过期时间（秒）
     * @return 缓存实例（已支持防缓存击穿）
     */
    @SuppressWarnings("unchecked")
    public <K, V> LocalCache<K, V> getCache(String cacheName, long maxSize, long expireSeconds) {
        return (LocalCache<K, V>) cacheMap.computeIfAbsent(cacheName,
                name -> new LocalCache<>(name, maxSize, expireSeconds));
    }

    /**
     * 创建缓存（使用默认配置）
     * 默认：最大 10000 条，5 分钟过期
     * 
     * @param cacheName 缓存名称
     * @return 缓存实例
     */
    public <K, V> LocalCache<K, V> getCache(String cacheName) {
        return getCache(cacheName, 10000, 300); // 300 秒 = 5 分钟
    }

    /**
     * 清空所有缓存
     */
    public void clearAll() {
        cacheMap.values().forEach(LocalCache::deleteAll);
    }

    /**
     * 打印所有缓存统计
     */
    public void logAllStats() {
        cacheMap.values().forEach(LocalCache::logStats);
    }
}