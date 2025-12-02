package com.cjlabs.core.id;

import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 完善的雪花算法ID生成器，解决时钟回拨等各种问题
 * <p>
 * ID结构（64位）：
 * 1位符号位（固定为0） + 41位时间戳 + 22位序列号
 * <p>
 * 特性：
 * 1. 解决时钟回拨问题
 * 2. 线程安全
 * 3. 高性能（每毫秒可生成4194304个ID，约420万个）
 * 4. 去掉机器ID，适用于单机环境
 * 5. 支持自定义配置
 * 6. 提供详细的监控信息
 * 7. 使用UTC+0时区，确保全球一致性
 * 8. 针对JDK 21优化
 */
@Slf4j
public class FmkSnowflakeIdGenerator {

    // ======================== 基础配置 ========================

    /**
     * 起始时间戳（2025-07-20 00:00:00 UTC+0）
     */
    private static final long START_TIMESTAMP = 1721433600000L; // UTC+0时间

    /**
     * 序列号位数（22位，原来的12位序列号 + 10位机器ID）
     */
    private static final long SEQUENCE_BITS = 22L;

    /**
     * 时间戳位数
     */
    private static final long TIMESTAMP_BITS = 41L;

    /**
     * 序列号最大值（2^22 - 1 = 4194303）
     */
    private static final long MAX_SEQUENCE = ~(-1L << SEQUENCE_BITS);

    /**
     * 时间戳左移位数
     */
    private static final long TIMESTAMP_SHIFT = SEQUENCE_BITS;

    /**
     * 最大时间戳（可使用到2094年）
     */
    private static final long MAX_TIMESTAMP = ~(-1L << TIMESTAMP_BITS);

    /**
     * 日期格式化器 - UTC - 使用不可变的DateTimeFormatter
     */
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
            .withZone(ZoneOffset.UTC);

    // ======================== 实例变量 ========================

    /**
     * 当前序列号
     */
    private static long sequence = 0L;

    /**
     * 上次生成ID的时间戳
     */
    private static long lastTimestamp = -1L;

    /**
     * 时钟回拨容忍时间（毫秒）
     */
    private static long clockBackwardToleranceMs;

    /**
     * 同步锁
     */
    private static final Lock lock = new ReentrantLock();

    /**
     * 性能统计 - 使用原子变量确保线程安全
     */
    private static final AtomicLong totalGenerated = new AtomicLong(0);
    private static final AtomicLong clockBackwardCount = new AtomicLong(0);
    private static final AtomicLong waitCount = new AtomicLong(0);

    // ======================== 构造函数 ========================

    /**
     * 默认构造函数
     */
    public FmkSnowflakeIdGenerator() {
        this(5L);
    }

    /**
     * 构造函数
     *
     * @param clockBackwardToleranceMs 时钟回拨容忍时间（毫秒）
     */
    public FmkSnowflakeIdGenerator(long clockBackwardToleranceMs) {
        FmkSnowflakeIdGenerator.clockBackwardToleranceMs = Math.max(0, clockBackwardToleranceMs);

        String startTimeFormatted = formatTimestamp(START_TIMESTAMP);
        log.info("雪花算法ID生成器初始化完成 - 时钟回拨容忍时间: {}ms, 起始时间: {} (UTC+0), 序列号位数: {}位",
                clockBackwardToleranceMs, startTimeFormatted, SEQUENCE_BITS);
    }

    // ======================== 核心方法 ========================

    /**
     * 生成下一个ID
     *
     * @return 唯一ID
     * @throws RuntimeException 当时钟回拨超过容忍时间时抛出异常
     */
    public static long nextId() {
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
                    waitCount.incrementAndGet();
                }
            } else {
                // 新的毫秒，重置序列号
                sequence = 0L;
            }

            lastTimestamp = currentTimestamp;
            totalGenerated.incrementAndGet();

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
     * 生成下一个ID
     *
     * @return 唯一ID
     * @throws RuntimeException 当时钟回拨超过容忍时间时抛出异常
     */
    public static String nextIdStr(String prefix) {
        return prefix + nextId();
    }

    /**
     * 生成下一个ID
     *
     * @return 唯一ID
     * @throws RuntimeException 当时钟回拨超过容忍时间时抛出异常
     */
    public static String nextIdStr() {
        return nextIdStr("");
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
    private static void handleClockBackward(long currentTimestamp) {
        long backwardMs = lastTimestamp - currentTimestamp;
        clockBackwardCount.incrementAndGet();

        log.warn("检测到时钟回拨 {}ms，上次时间戳: {} ({}), 当前时间戳: {} ({})",
                backwardMs,
                lastTimestamp, formatTimestamp(lastTimestamp),
                currentTimestamp, formatTimestamp(currentTimestamp));

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
    private static long waitForNextMillis(long lastTimestamp) {
        long currentTimestamp = getCurrentTimestamp();
        while (currentTimestamp <= lastTimestamp) {
            currentTimestamp = getCurrentTimestamp();
        }
        return currentTimestamp;
    }

    /**
     * 获取当前时间戳 (UTC+0)
     * JDK 21中System.currentTimeMillis()已经是UTC时间戳
     *
     * @return 当前时间戳（毫秒）
     */
    private static long getCurrentTimestamp() {
        return System.currentTimeMillis();
    }

    /**
     * 格式化时间戳为可读的UTC时间
     * 使用Instant直接格式化，避免LocalDateTime转换
     *
     * @param timestamp 时间戳
     * @return 格式化后的时间字符串 (UTC+0)
     */
    private static String formatTimestamp(long timestamp) {
        return DATE_FORMATTER.format(Instant.ofEpochMilli(timestamp)) + " UTC+0";
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

        return new SnowflakeIdInfo(id, timestamp, sequence, formatTimestamp(timestamp));
    }

    // ======================== 状态监控 ========================

    /**
     * 获取生成器状态
     *
     * @return 生成器状态信息
     */
    public SnowflakeStatus getStatus() {
        long currentTimestamp = getCurrentTimestamp();
        return new SnowflakeStatus(
                totalGenerated.get(),
                clockBackwardCount.get(),
                waitCount.get(),
                lastTimestamp,
                currentTimestamp,
                clockBackwardToleranceMs
        );
    }

    /**
     * 重置统计信息
     */
    public void resetStatistics() {
        lock.lock();
        try {
            totalGenerated.set(0);
            clockBackwardCount.set(0);
            waitCount.set(0);
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
                "雪花算法配置: 起始时间=%s, 时钟回拨容忍=%dms, " +
                        "时间戳位数=%d, 序列号位数=%d, 最大序列号=%d",
                formatTimestamp(START_TIMESTAMP), clockBackwardToleranceMs,
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