package com.cjlabs.core.id;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 完善的雪花算法ID生成器，解决时钟回拨等各种问题
 * 
 * ID结构（64位）：
 * 1位符号位（固定为0） + 41位时间戳 + 22位序列号
 * 
 * 特性：
 * 1. 解决时钟回拨问题
 * 2. 线程安全
 * 3. 高性能（每毫秒可生成4194304个ID，约420万个）
 * 4. 去掉机器ID，适用于单机环境
 * 5. 支持自定义配置
 * 6. 提供详细的监控信息
 */
@Slf4j
// @Component
public class SnowflakeIdGenerator {

    // ======================== 基础配置 ========================
    
    /** 起始时间戳（2025-07-20 00:00:00 UTC+8） */
    private static final long START_TIMESTAMP = 1721404800000L;
    
    /** 序列号位数（22位，原来的12位序列号 + 10位机器ID） */
    private static final long SEQUENCE_BITS = 22L;
    
    /** 时间戳位数 */
    private static final long TIMESTAMP_BITS = 41L;
    
    /** 序列号最大值（2^22 - 1 = 4194303） */
    private static final long MAX_SEQUENCE = ~(-1L << SEQUENCE_BITS);
    
    /** 时间戳左移位数 */
    private static final long TIMESTAMP_SHIFT = SEQUENCE_BITS;
    
    /** 最大时间戳（可使用到2094年） */
    private static final long MAX_TIMESTAMP = ~(-1L << TIMESTAMP_BITS);
    
    // ======================== 实例变量 ========================
    
    /** 当前序列号 */
    private long sequence = 0L;
    
    /** 上次生成ID的时间戳 */
    private long lastTimestamp = -1L;
    
    /** 时钟回拨容忍时间（毫秒） */
    private final long clockBackwardToleranceMs;
    
    /** 同步锁 */
    private final Lock lock = new ReentrantLock();
    
    /** 性能统计 */
    private volatile long totalGenerated = 0L;
    private volatile long clockBackwardCount = 0L;
    private volatile long waitCount = 0L;
    
    // ======================== 构造函数 ========================
    
    /**
     * 默认构造函数
     */
    public SnowflakeIdGenerator() {
        this(5L);
    }
    
    /**
     * 构造函数
     * 
     * @param clockBackwardToleranceMs 时钟回拨容忍时间（毫秒）
     */
    public SnowflakeIdGenerator(long clockBackwardToleranceMs) {
        this.clockBackwardToleranceMs = Math.max(0, clockBackwardToleranceMs);
        
        log.info("雪花算法ID生成器初始化完成 - 时钟回拨容忍时间: {}ms, 起始时间: {}, 序列号位数: {}位", 
                clockBackwardToleranceMs, START_TIMESTAMP, SEQUENCE_BITS);
    }
    
    // ======================== 核心方法 ========================
    
    /**
     * 生成下一个ID
     * 
     * @return 唯一ID
     * @throws RuntimeException 当时钟回拨超过容忍时间时抛出异常
     */
    public long nextId() {
        lock.lock();
        try {
            long currentTimestamp = getCurrentTimestamp();
            
            // 处理时钟回拨
            if (currentTimestamp < lastTimestamp) {
                handleClockBackward(currentTimestamp);
                currentTimestamp = getCurrentTimestamp();
            }
            
            // 同一毫秒内生成
            if (currentTimestamp == lastTimestamp) {
                sequence = (sequence + 1) & MAX_SEQUENCE;
                
                // 序列号溢出，等待下一毫秒
                if (sequence == 0) {
                    currentTimestamp = waitForNextMillis(lastTimestamp);
                    waitCount++;
                }
            } else {
                // 新的毫秒，重置序列号
                sequence = 0L;
            }
            
            lastTimestamp = currentTimestamp;
            totalGenerated++;
            
            // 验证时间戳不超过最大值
            long timestampDelta = currentTimestamp - START_TIMESTAMP;
            if (timestampDelta > MAX_TIMESTAMP) {
                throw new RuntimeException("时间戳超过最大值，雪花算法已达到使用期限");
            }
            
            return ((timestampDelta) << TIMESTAMP_SHIFT) | sequence;
                    
        } finally {
            lock.unlock();
        }
    }
    
    /**
     * 批量生成ID
     * 
     * @param count 生成数量
     * @return ID数组
     */
    public long[] nextIds(int count) {
        if (count <= 0) {
            throw new IllegalArgumentException("生成数量必须大于0");
        }
        
        long[] ids = new long[count];
        for (int i = 0; i < count; i++) {
            ids[i] = nextId();
        }
        return ids;
    }
    
    // ======================== 时钟回拨处理 ========================
    
    /**
     * 处理时钟回拨
     * 
     * @param currentTimestamp 当前时间戳
     */
    private void handleClockBackward(long currentTimestamp) {
        long backwardMs = lastTimestamp - currentTimestamp;
        clockBackwardCount++;
        
        log.warn("检测到时钟回拨 {}ms，上次时间戳: {}, 当前时间戳: {}", 
                backwardMs, lastTimestamp, currentTimestamp);
        
        if (backwardMs <= clockBackwardToleranceMs) {
            // 在容忍范围内，等待时钟追上
            try {
                Thread.sleep(backwardMs + 1);
                log.info("时钟回拨处理完成，等待了 {}ms", backwardMs + 1);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("等待时钟追上时被中断", e);
            }
        } else {
            // 超过容忍范围，抛出异常
            throw new RuntimeException(
                String.format("时钟回拨超过容忍时间，回拨时间: %dms, 容忍时间: %dms", 
                        backwardMs, clockBackwardToleranceMs));
        }
    }
    
    /**
     * 等待下一毫秒
     * 
     * @param lastTimestamp 上次时间戳
     * @return 下一毫秒的时间戳
     */
    private long waitForNextMillis(long lastTimestamp) {
        long currentTimestamp = getCurrentTimestamp();
        while (currentTimestamp <= lastTimestamp) {
            currentTimestamp = getCurrentTimestamp();
        }
        return currentTimestamp;
    }
    
    /**
     * 获取当前时间戳
     * 
     * @return 当前时间戳（毫秒）
     */
    private long getCurrentTimestamp() {
        return System.currentTimeMillis();
    }
    
    // ======================== ID解析 ========================
    
    /**
     * 解析ID信息
     * 
     * @param id 要解析的ID
     * @return ID信息
     */
    public SnowflakeIdInfo parseId(long id) {
        long timestamp = ((id >> TIMESTAMP_SHIFT) & MAX_TIMESTAMP) + START_TIMESTAMP;
        long sequence = id & MAX_SEQUENCE;
        
        return new SnowflakeIdInfo(id, timestamp, sequence);
    }
    
    // ======================== 状态监控 ========================
    
    /**
     * 获取生成器状态
     * 
     * @return 生成器状态信息
     */
    public SnowflakeStatus getStatus() {
        return new SnowflakeStatus(
                totalGenerated,
                clockBackwardCount,
                waitCount,
                lastTimestamp,
                getCurrentTimestamp(),
                clockBackwardToleranceMs
        );
    }
    
    /**
     * 重置统计信息
     */
    public void resetStatistics() {
        lock.lock();
        try {
            totalGenerated = 0L;
            clockBackwardCount = 0L;
            waitCount = 0L;
            log.info("雪花算法统计信息已重置");
        } finally {
            lock.unlock();
        }
    }
    
    // ======================== 工具方法 ========================
    
    /**
     * 验证ID是否由此生成器生成
     * 
     * @param id 要验证的ID
     * @return 是否由此生成器生成
     */
    public boolean isValidId(long id) {
        try {
            SnowflakeIdInfo info = parseId(id);
            return info.getTimestamp() >= START_TIMESTAMP
                    && info.getTimestamp() <= System.currentTimeMillis();
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * 获取当前配置信息
     * 
     * @return 配置信息字符串
     */
    public String getConfigInfo() {
        return String.format(
                "雪花算法配置: 起始时间=%d, 时钟回拨容忍=%dms, " +
                "时间戳位数=%d, 序列号位数=%d, 最大序列号=%d",
                START_TIMESTAMP, clockBackwardToleranceMs,
                TIMESTAMP_BITS, SEQUENCE_BITS, MAX_SEQUENCE
        );
    }
    
    /**
     * 获取理论性能信息
     * 
     * @return 性能信息字符串
     */
    public String getPerformanceInfo() {
        long maxIdsPerSecond = (MAX_SEQUENCE + 1) * 1000;
        return String.format(
                "理论性能: 每毫秒最多%d个ID, 每秒最多%d个ID (约%.1f万个/秒)",
                MAX_SEQUENCE + 1, maxIdsPerSecond, maxIdsPerSecond / 10000.0
        );
    }
}