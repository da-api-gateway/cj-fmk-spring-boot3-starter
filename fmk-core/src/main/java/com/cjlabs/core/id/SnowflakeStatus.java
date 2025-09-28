package com.cjlabs.core.id;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/**
 * 雪花算法生成器状态信息（无机器ID版本）
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SnowflakeStatus {

    /**
     * 总生成数量
     */
    private long totalGenerated;

    /**
     * 时钟回拨次数
     */
    private long clockBackwardCount;

    /**
     * 等待次数（序列号溢出）
     */
    private long waitCount;

    /**
     * 上次生成时间戳
     */
    private long lastTimestamp;

    /**
     * 上次生成时间的格式化字符串 (UTC+0)
     */
    private String lastGeneratedTime;

    /**
     * 当前时间戳
     */
    private long currentTimestamp;

    /**
     * 当前时间的格式化字符串 (UTC+0)
     */
    private String currentTime;

    /**
     * 时钟回拨容忍时间
     */
    private long clockBackwardToleranceMs;

    /**
     * 六参数构造函数 (向后兼容)
     */
    public SnowflakeStatus(long totalGenerated, long clockBackwardCount, long waitCount,
                           long lastTimestamp, long currentTimestamp, long clockBackwardToleranceMs) {
        this.totalGenerated = totalGenerated;
        this.clockBackwardCount = clockBackwardCount;
        this.waitCount = waitCount;
        this.lastTimestamp = lastTimestamp;
        this.currentTimestamp = currentTimestamp;
        this.clockBackwardToleranceMs = clockBackwardToleranceMs;
        this.lastGeneratedTime = formatTimestamp(lastTimestamp);
        this.currentTime = formatTimestamp(currentTimestamp);
    }

    /**
     * 获取上次生成时间的格式化字符串 (UTC+0)
     *
     * @return 格式化时间
     */
    public String getLastGeneratedTime() {
        if (lastGeneratedTime != null) {
            return lastGeneratedTime;
        }

        if (lastTimestamp <= 0) {
            return "未生成过ID";
        }
        return formatTimestamp(lastTimestamp);
    }

    /**
     * 获取当前时间的格式化字符串 (UTC+0)
     *
     * @return 格式化时间
     */
    public String getCurrentTime() {
        if (currentTime != null) {
            return currentTime;
        }

        return formatTimestamp(currentTimestamp);
    }

    /**
     * 格式化时间戳为UTC+0时间
     *
     * @param timestamp 时间戳
     * @return 格式化的UTC时间字符串
     */
    private String formatTimestamp(long timestamp) {
        LocalDateTime dateTime = LocalDateTime.ofInstant(
                Instant.ofEpochMilli(timestamp),
                ZoneOffset.UTC
        );
        return dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")) + " UTC";
    }

    /**
     * 计算平均每毫秒生成ID数量
     *
     * @return 平均生成速率
     */
    public double getAverageGenerationRate() {
        long timeDiff = currentTimestamp - (lastTimestamp > 0 ? lastTimestamp : currentTimestamp);
        if (timeDiff <= 0 || totalGenerated <= 0) {
            return 0.0;
        }
        return (double) totalGenerated / timeDiff;
    }
}