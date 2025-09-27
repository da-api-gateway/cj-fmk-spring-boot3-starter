package com.cjlabs.web.time;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FmkLocalDateTimeUtil {

    /**
     * 获取当前时间的格式化字符串
     *
     * @return 格式化后的当前时间字符串
     */
    public static LocalDateTime now() {
        return LocalDateTime.now();
    }

    /**
     * 获取当前时间的格式化字符串
     *
     * @return 格式化后的当前时间字符串
     */
    public static String nowToStr() {
        return now().format(FmkTimeConstant.yy_MM_dd_HH_mm_ss_Formatter);
    }

    /**
     * 获取当前时间的格式化字符串
     *
     * @return 格式化后的当前时间字符串
     */
    public static String nowToStr(DateTimeFormatter formatter) {
        return now().format(formatter);
    }

    /**
     * 将字符串转为 LocalDateTime（默认格式 yyyy-MM-dd HH:mm:ss）
     */
    public static LocalDateTime strToLocalDateTime(String dateTimeStr) {
        return strToLocalDateTime(dateTimeStr, FmkTimeConstant.yy_MM_dd_HH_mm_ss_Formatter);
    }

    /**
     * 将字符串转为 LocalDateTime（自定义格式）
     */
    public static LocalDateTime strToLocalDateTime(String dateTimeStr, DateTimeFormatter formatter) {
        if (dateTimeStr == null || dateTimeStr.trim().isEmpty()) {
            return null;
        }
        try {
            return LocalDateTime.parse(dateTimeStr, formatter);
        } catch (Exception e) {
            return null;
        }
    }
}
