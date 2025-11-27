package com.cjlabs.core.time;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import static com.cjlabs.core.time.FmkTimeConstant.*;

public class FmkInstantUtil {
    private FmkInstantUtil() {
        // 工具类禁止实例化
    }

    // ---------------------- 基础方法 ----------------------

    /**
     * 获取当前时间（UTC）
     */
    public static Instant now() {
        return Instant.now();
    }

    /**
     * 获取当前毫秒时间戳
     */
    public static long currentMillis() {
        return Instant.now().toEpochMilli();
    }

    /**
     * 获取当前秒级时间戳
     */
    public static long currentSeconds() {
        return Instant.now().getEpochSecond();
    }

    // ---------------------- 转换方法 ----------------------

    /**
     * Instant → 毫秒时间戳
     */
    public static long toMillis(Instant instant) {
        return instant == null ? 0L : instant.toEpochMilli();
    }

    /**
     * 毫秒时间戳 → Instant
     */
    public static Instant fromMillis(long millis) {
        return Instant.ofEpochMilli(millis);
    }

    // ---------------------- 加减时间 ----------------------

    /**
     * 增加指定单位
     */
    public static Instant plus(Instant instant, long amount, ChronoUnit unit) {
        return instant.plus(amount, unit);
    }

    /**
     * 减少指定单位
     */
    public static Instant minus(Instant instant, long amount, ChronoUnit unit) {
        return instant.minus(amount, unit);
    }

    /**
     * 增加秒
     */
    public static Instant plusSeconds(Instant instant, long seconds) {
        return instant.plusSeconds(seconds);
    }

    /**
     * 增加天
     */
    public static Instant plusDays(Instant instant, long days) {
        return instant.plus(days, ChronoUnit.DAYS);
    }

    // ---------------------- 比较与差值 ----------------------

    /**
     * 是否在另一个 Instant 之后
     */
    public static boolean isAfter(Instant a, Instant b) {
        return a.isAfter(b);
    }

    /**
     * 是否在另一个 Instant 之前
     */
    public static boolean isBefore(Instant a, Instant b) {
        return a.isBefore(b);
    }

    /**
     * 计算两个时间的毫秒差
     */
    public static long diffMillis(Instant start, Instant end) {
        return Duration.between(start, end).toMillis();
    }

    /**
     * 计算两个时间的秒差
     */
    public static long diffSeconds(Instant start, Instant end) {
        return Duration.between(start, end).getSeconds();
    }

    /**
     * 计算两个时间的天数差
     */
    public static long diffDays(Instant start, Instant end) {
        return Duration.between(start, end).toDays();
    }

    // ---------------------- 便捷转换 ----------------------

    /**
     * Instant → ISO8601 标准字符串
     */
    public static String toIsoString(Instant instant) {
        return instant == null ? null : instant.toString();
    }

    /**
     * ISO8601 字符串 → Instant
     */
    public static Instant fromIsoString(String isoString) {
        return isoString == null ? null : Instant.parse(isoString);
    }


    // ---------------------- 格式化方法 ----------------------

    /**
     * 格式化为指定格式的字符串（默认 UTC 时区）
     *
     * @param instant 时间实例
     * @param pattern 格式模式，如 "yyyy-MM-dd HH:mm:ss"
     */
    public static String format(Instant instant, String pattern) {
        if (instant == null) {
            return null;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return format(instant, formatter, ZoneId.systemDefault());
    }

    /**
     * 格式化为指定格式的字符串（指定时区）
     *
     * @param instant   时间实例
     * @param formatter 格式模式，如 "yyyy-MM-dd HH:mm:ss"
     * @param zoneId    时区，如 ZoneId.of("Asia/Shanghai")
     */
    public static String format(Instant instant, DateTimeFormatter formatter, ZoneId zoneId) {
        if (instant == null) {
            return null;
        }
        formatter = formatter.withZone(zoneId);
        return formatter.format(instant);
    }

    /**
     * 格式化为 yyyy-MM-dd HH:mm:ss（UTC）
     */
    public static String formatDateTime(Instant instant) {
        return format(instant, yy_MM_dd_HH_mm_ss);
    }

    /**
     * 格式化为 yyyy-MM-dd（UTC）
     */
    public static String formatDate(Instant instant) {
        return format(instant, yy_MM_dd);
    }

    /**
     * 格式化为 HH:mm:ss（UTC）
     */
    public static String formatTime(Instant instant) {
        return format(instant, HH_mm_ss);
    }

    /**
     * 解析字符串为 Instant（默认 UTC 时区）
     *
     * @param dateTimeStr 时间字符串
     * @param pattern     格式模式，如 "yyyy-MM-dd HH:mm:ss"
     */
    public static Instant parse(String dateTimeStr, String pattern) {
        return parse(dateTimeStr, pattern, ZoneId.systemDefault());
    }

    /**
     * 解析字符串为 Instant（指定时区）
     *
     * @param dateTimeStr 时间字符串
     * @param pattern     格式模式，如 "yyyy-MM-dd HH:mm:ss"
     * @param zoneId      时区
     */
    public static Instant parse(String dateTimeStr, String pattern, ZoneId zoneId) {
        if (dateTimeStr == null) {
            return null;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern)
                .withZone(zoneId);
        return Instant.from(formatter.parse(dateTimeStr));
    }

}
