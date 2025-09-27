package com.cjlabs.core.id;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
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
     * 获取格式化的时间字符串
     *
     * @return 格式化时间
     */
    public String getFormattedTime() {
        LocalDateTime dateTime = LocalDateTime.ofInstant(
                Instant.ofEpochMilli(timestamp),
                ZoneId.systemDefault()
        );
        return dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));
    }
}