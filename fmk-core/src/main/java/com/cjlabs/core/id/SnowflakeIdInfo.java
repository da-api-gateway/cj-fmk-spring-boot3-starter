package com.cjlabs.core.id;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/**
 * 雪花算法ID信息（无机器ID版本）
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SnowflakeIdInfo {

    /**
     * 原始ID
     */
    private long id;

    /**
     * 时间戳（毫秒）
     */
    private long timestamp;

    /**
     * 序列号
     */
    private long sequence;

    /**
     * 格式化的时间字符串 (UTC+0)
     */
    private String formattedTime;

    /**
     * 日期格式化器 - UTC
     */
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter
            .ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
            .withZone(ZoneOffset.UTC);

    /**
     * 三参数构造函数 (向后兼容)
     */
    public SnowflakeIdInfo(long id, long timestamp, long sequence) {
        this.id = id;
        this.timestamp = timestamp;
        this.sequence = sequence;
        this.formattedTime = getFormattedTime();
    }

    /**
     * 获取格式化的时间字符串 (UTC+0)
     *
     * @return 格式化时间
     */
    public String getFormattedTime() {
        if (formattedTime != null) {
            return formattedTime;
        }

        // 直接使用带时区的DateTimeFormatter格式化Instant
        return DATE_FORMATTER.format(Instant.ofEpochMilli(timestamp)) + " UTC";
    }

    /**
     * 返回人类可读的ID信息
     *
     * @return ID的可读字符串表示
     */
    @Override
    public String toString() {
        return String.format("ID: %d, 生成时间: %s, 序列号: %d",
                id, getFormattedTime(), sequence);
    }
}