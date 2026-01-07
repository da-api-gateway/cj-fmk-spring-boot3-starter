package com.cjlabs.core.time;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class FmkTimeConstant {
// ===== 亚洲 =====

    // 柬埔寨（金边） UTC+7
    public static final ZoneId CAMBODIA_ZONE = ZoneId.of("Asia/Phnom_Penh");

    // 泰国（曼谷） UTC+7
    public static final ZoneId THAILAND_ZONE = ZoneId.of("Asia/Bangkok");

    // 越南（河内） UTC+7
    public static final ZoneId VIETNAM_ZONE = ZoneId.of("Asia/Ho_Chi_Minh");

    // 新加坡 UTC+8
    public static final ZoneId SINGAPORE_ZONE = ZoneId.of("Asia/Singapore");

    // 中国 UTC+8
    public static final ZoneId CHINA_ZONE = ZoneId.of("Asia/Shanghai");

    // 香港 UTC+8
    public static final ZoneId HONGKONG_ZONE = ZoneId.of("Asia/Hong_Kong");

    // 日本 UTC+9
    public static final ZoneId JAPAN_ZONE = ZoneId.of("Asia/Tokyo");

    public static final ZoneId UTC_ZONE = ZoneId.of("UTC");

    // ===== UTC-12 ~ UTC-1 =====
    public static final ZoneId UTC_MINUS_12_ZONE = ZoneId.of("UTC-12");
    public static final ZoneId UTC_MINUS_11_ZONE = ZoneId.of("UTC-11");
    public static final ZoneId UTC_MINUS_10_ZONE = ZoneId.of("UTC-10");
    public static final ZoneId UTC_MINUS_9_ZONE = ZoneId.of("UTC-09");
    public static final ZoneId UTC_MINUS_8_ZONE = ZoneId.of("UTC-08");
    public static final ZoneId UTC_MINUS_7_ZONE = ZoneId.of("UTC-07");
    public static final ZoneId UTC_MINUS_6_ZONE = ZoneId.of("UTC-06");
    public static final ZoneId UTC_MINUS_5_ZONE = ZoneId.of("UTC-05");
    public static final ZoneId UTC_MINUS_4_ZONE = ZoneId.of("UTC-04");
    public static final ZoneId UTC_MINUS_3_ZONE = ZoneId.of("UTC-03");
    public static final ZoneId UTC_MINUS_2_ZONE = ZoneId.of("UTC-02");
    public static final ZoneId UTC_MINUS_1_ZONE = ZoneId.of("UTC-01");

    // ===== UTC+1 ~ UTC+14 =====
    public static final ZoneId UTC_PLUS_1_ZONE = ZoneId.of("UTC+01");
    public static final ZoneId UTC_PLUS_2_ZONE = ZoneId.of("UTC+02");
    public static final ZoneId UTC_PLUS_3_ZONE = ZoneId.of("UTC+03");
    public static final ZoneId UTC_PLUS_4_ZONE = ZoneId.of("UTC+04");
    public static final ZoneId UTC_PLUS_5_ZONE = ZoneId.of("UTC+05");
    public static final ZoneId UTC_PLUS_6_ZONE = ZoneId.of("UTC+06");
    public static final ZoneId UTC_PLUS_7_ZONE = ZoneId.of("UTC+07");
    public static final ZoneId UTC_PLUS_8_ZONE = ZoneId.of("UTC+08");
    public static final ZoneId UTC_PLUS_9_ZONE = ZoneId.of("UTC+09");
    public static final ZoneId UTC_PLUS_10_ZONE = ZoneId.of("UTC+10");
    public static final ZoneId UTC_PLUS_11_ZONE = ZoneId.of("UTC+11");
    public static final ZoneId UTC_PLUS_12_ZONE = ZoneId.of("UTC+12");
    public static final ZoneId UTC_PLUS_13_ZONE = ZoneId.of("UTC+13");
    public static final ZoneId UTC_PLUS_14_ZONE = ZoneId.of("UTC+14");

    // ==================== 常用时间格式常量 ====================
    // 基础格式
    public static final String yy_MM_dd_HH_mm_ss = "yyyy-MM-dd HH:mm:ss";
    public static final String yy_MM_dd = "yyyy-MM-dd";
    public static final String HH_mm_ss = "HH:mm:ss";

    // ISO 8601 格式（带T分隔符）
    public static final String ISO_DATE_TIME = "yyyy-MM-dd'T'HH:mm:ss";
    public static final String ISO_DATE_TIME_MS = "yyyy-MM-dd'T'HH:mm:ss.SSS";
    public static final String ISO_DATE_TIME_Z = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    public static final String ISO_DATE_TIME_MS_Z = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    // ISO 8601 格式（带UTC时区偏移）
    public static final String ISO_DATE_TIME_UTC = "yyyy-MM-dd'T'HH:mm:ssX";
    public static final String ISO_DATE_TIME_MS_UTC = "yyyy-MM-dd'T'HH:mm:ss.SSSX";

    // 紧凑格式（无分隔符）
    public static final String COMPACT_DATE_TIME = "yyyyMMddHHmmss";
    public static final String COMPACT_DATE_TIME_MS = "yyyyMMddHHmmssSSS";
    public static final String COMPACT_DATE = "yyyyMMdd";

    // 中文友好格式
    public static final String CN_DATE_TIME = "yyyy年MM月dd日 HH:mm:ss";
    public static final String CN_DATE = "yyyy年MM月dd日";

    // ==================== 日期格式化器 ====================

    // 基础格式化器
    public static final DateTimeFormatter yy_MM_dd_HH_mm_ss_Formatter = DateTimeFormatter.ofPattern(yy_MM_dd_HH_mm_ss);

    public static final DateTimeFormatter yy_MM_dd_Formatter = DateTimeFormatter.ofPattern(yy_MM_dd);

    public static final DateTimeFormatter HH_mm_ss_Formatter = DateTimeFormatter.ofPattern(HH_mm_ss);

    // ISO 8601 格式化器（本地时间，无时区）
    public static final DateTimeFormatter yy_MM_dd_T_HH_mm_ss_Formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public static final DateTimeFormatter ISO_DATE_TIME_Formatter = DateTimeFormatter.ofPattern(ISO_DATE_TIME);

    public static final DateTimeFormatter ISO_DATE_TIME_MS_Formatter = DateTimeFormatter.ofPattern(ISO_DATE_TIME_MS);

    // ISO 8601 格式化器（UTC时区，带Z后缀）
    public static final DateTimeFormatter ISO_DATE_TIME_Z_Formatter = DateTimeFormatter.ofPattern(ISO_DATE_TIME_Z).withZone(ZoneOffset.UTC);

    public static final DateTimeFormatter ISO_DATE_TIME_MS_Z_Formatter = DateTimeFormatter.ofPattern(ISO_DATE_TIME_MS_Z).withZone(ZoneOffset.UTC);

    // ISO 8601 格式化器（UTC时区，带偏移量）
    public static final DateTimeFormatter ISO_DATE_TIME_UTC_Formatter = DateTimeFormatter.ofPattern(ISO_DATE_TIME_UTC).withZone(ZoneOffset.UTC);

    public static final DateTimeFormatter ISO_DATE_TIME_MS_UTC_Formatter = DateTimeFormatter.ofPattern(ISO_DATE_TIME_MS_UTC).withZone(ZoneOffset.UTC);

    // 紧凑格式化器
    public static final DateTimeFormatter COMPACT_DATE_TIME_Formatter = DateTimeFormatter.ofPattern(COMPACT_DATE_TIME);

    public static final DateTimeFormatter COMPACT_DATE_TIME_MS_Formatter = DateTimeFormatter.ofPattern(COMPACT_DATE_TIME_MS);

    public static final DateTimeFormatter COMPACT_DATE_Formatter = DateTimeFormatter.ofPattern(COMPACT_DATE);

    // 中文格式化器
    public static final DateTimeFormatter CN_DATE_TIME_Formatter = DateTimeFormatter.ofPattern(CN_DATE_TIME);

    public static final DateTimeFormatter CN_DATE_Formatter = DateTimeFormatter.ofPattern(CN_DATE);

    // ==================== 特殊用途格式化器 ====================
}
