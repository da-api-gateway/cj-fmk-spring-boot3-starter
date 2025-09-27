package com.cjlabs.web.time;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class FmkTimeConstant {
    // 柬埔寨金边时区 (UTC+7)
    public static final ZoneId CAMBODIA_ZONE = ZoneId.of("Asia/Phnom_Penh");
    public static final ZoneId UTC_PLUS_7_ZONE = ZoneOffset.of("+07:00");  // UTC+7

    // 保持向后兼容，但建议使用 CAMBODIA_ZONE
    @Deprecated
    public static final ZoneId UTC_PLUS_8_ZONE = ZoneOffset.of("+08:00");  // UTC+8

    // 常用时间格式常量
    public static final String yy_MM_dd_HH_mm_ss = "yyyy-MM-dd HH:mm:ss";
    public static final String yy_MM_dd = "yyyy-MM-dd";
    // public static final String TIME_PATTERN = "HH:mm:ss";
    // public static final String DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    // public static final String DATETIME_MS_PATTERN = "yyyy-MM-dd HH:mm:ss.SSS";
    // public static final String ISO_PATTERN = "yyyy-MM-dd'T'HH:mm:ss";
    // public static final String ISO_MS_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSS";
    // public static final String COMPACT_PATTERN = "yyyyMMddHHmmss";
    // public static final String COMPACT_MS_PATTERN = "yyyyMMddHHmmssSSS";

    // 定义不同的日期格式化器
    public static final DateTimeFormatter yy_MM_dd_HH_mm_ss_Formatter = DateTimeFormatter.ofPattern(yy_MM_dd_HH_mm_ss);
    // DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    // 2027-08-07T00:00:00
    public static final DateTimeFormatter yy_MM_dd_T_HH_mm_ss_Formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    public static final DateTimeFormatter yy_MM_dd_Formatter = DateTimeFormatter.ofPattern(yy_MM_dd);

    // 定义不同的日期格式化器 - 使用柬埔寨时区
    public static final DateTimeFormatter yy_MM_dd_HH_mm_ss_Formatter_Cambodia =
            DateTimeFormatter.ofPattern(yy_MM_dd_HH_mm_ss).withZone(CAMBODIA_ZONE);
    public static final DateTimeFormatter yy_MM_dd_Formatter_Cambodia =
            DateTimeFormatter.ofPattern(yy_MM_dd).withZone(CAMBODIA_ZONE);

}
