package com.cjlabs.web.time;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class FmkTimestampUtil {

    public static Timestamp now() {
        return new Timestamp(System.currentTimeMillis());
    }

    /**
     * 将时间戳转换为默认格式字符串 (yyyy-MM-dd HH:mm:ss)
     *
     * @param timestamp 时间戳（毫秒）
     * @return 格式化后的时间字符串
     */
    public static String format(Timestamp timestamp) {
        if (Objects.isNull(timestamp)) {
            return null;
        }
        return format(timestamp.getTime(), FmkTimeConstant.yy_MM_dd_HH_mm_ss);
    }

    /**
     * 将时间戳转换为默认格式字符串 (yyyy-MM-dd HH:mm:ss)
     *
     * @param timestamp 时间戳（毫秒）
     * @return 格式化后的时间字符串
     */
    public static String format(long timestamp) {
        return format(timestamp, FmkTimeConstant.yy_MM_dd_HH_mm_ss);
    }

    /**
     * 将时间戳转换为指定格式字符串
     *
     * @param timestamp 时间戳（毫秒）
     * @param pattern   时间格式
     * @return 格式化后的时间字符串
     */
    public static String format(long timestamp, String pattern) {
        return format(timestamp, pattern, FmkTimeConstant.UTC_PLUS_8_ZONE);
    }

    /**
     * 将时间戳转换为指定格式和时区的字符串
     *
     * @param timestamp 时间戳（毫秒）
     * @param pattern   时间格式
     * @param zoneId    时区
     * @return 格式化后的时间字符串
     */
    public static String format(long timestamp, String pattern, ZoneId zoneId) {
        LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), zoneId);
        return dateTime.format(DateTimeFormatter.ofPattern(pattern));
    }
}
