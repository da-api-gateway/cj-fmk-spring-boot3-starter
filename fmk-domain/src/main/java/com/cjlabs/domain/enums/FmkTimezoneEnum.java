package com.cjlabs.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.ZoneId;

/**
 * 常用时区枚举，与语言枚举对应
 * 使用标准的 IANA 时区标识符（如 Asia/Shanghai）
 */
@Getter
@AllArgsConstructor
public enum FmkTimezoneEnum implements IEnumStr {

    // ===== 中国 & 中文区 =====
    ASIA_SHANGHAI("Asia/Shanghai", "中国标准时间"),
    ASIA_TAIPEI("Asia/Taipei", "台北时间"),
    ASIA_HONG_KONG("Asia/Hong_Kong", "香港时间"),
    ASIA_SINGAPORE("Asia/Singapore", "新加坡时间"),

    // ===== 东亚 =====
    ASIA_TOKYO("Asia/Tokyo", "日本标准时间"),
    ASIA_SEOUL("Asia/Seoul", "韩国标准时间"),

    // ===== 东南亚 =====
    ASIA_BANGKOK("Asia/Bangkok", "泰国时间"),
    ASIA_HO_CHI_MINH("Asia/Ho_Chi_Minh", "越南时间"),
    ASIA_PHNOM_PENH("Asia/Phnom_Penh", "柬埔寨时间"),
    ASIA_KUALA_LUMPUR("Asia/Kuala_Lumpur", "马来西亚时间"),
    ASIA_JAKARTA("Asia/Jakarta", "印尼时间"),
    ASIA_MANILA("Asia/Manila", "菲律宾时间"),
    ASIA_YANGON("Asia/Yangon", "缅甸时间"),
    ASIA_VIENTIANE("Asia/Vientiane", "老挝时间"),

    // ===== 南亚 =====
    ASIA_KOLKATA("Asia/Kolkata", "印度时间"),
    ASIA_DHAKA("Asia/Dhaka", "孟加拉时间"),

    // ===== 中东 =====
    ASIA_DUBAI("Asia/Dubai", "阿联酋时间"),
    ASIA_RIYADH("Asia/Riyadh", "沙特时间"),
    ASIA_JERUSALEM("Asia/Jerusalem", "以色列时间"),

    // ===== 欧洲 / 美洲（你已有）=====
    EUROPE_LONDON("Europe/London", "英国时间"),
    EUROPE_PARIS("Europe/Paris", "法国时间"),
    EUROPE_BERLIN("Europe/Berlin", "德国时间"),
    EUROPE_MADRID("Europe/Madrid", "西班牙时间"),
    EUROPE_MOSCOW("Europe/Moscow", "莫斯科时间"),
    AMERICA_NEW_YORK("America/New_York", "美国东部时间"),

    // ===== 兜底 =====
    UTC("UTC", "协调世界时"),
    ;

    /**
     * 时区标识符（IANA 标准格式）
     */
    private final String code;

    /**
     * 中文描述
     */
    private final String msg;

    public ZoneId toZoneId() {
        return ZoneId.of(code);
    }


    public static FmkTimezoneEnum getByLanguage(FmkLanguageEnum language) {
        if (language == null) {
            return ASIA_SHANGHAI;
        }

        return switch (language) {

            // ===== 中文 =====
            case ZH_CN -> ASIA_SHANGHAI;
            case ZH_TW -> ASIA_TAIPEI;
            case ZH_HK -> ASIA_HONG_KONG;
            case ZH_SG -> ASIA_SINGAPORE;

            // ===== 英语 =====
            case EN_US -> AMERICA_NEW_YORK;
            case EN_GB -> EUROPE_LONDON;
            case EN_SG -> ASIA_SINGAPORE;
            case EN_PH -> ASIA_MANILA;
            case EN_IN -> ASIA_KOLKATA;

            // ===== 东亚 =====
            case JA_JP -> ASIA_TOKYO;
            case KO_KR -> ASIA_SEOUL;

            // ===== 东南亚 =====
            case TH_TH -> ASIA_BANGKOK;
            case VI_VN -> ASIA_HO_CHI_MINH;
            case KM_KH -> ASIA_PHNOM_PENH;
            case MS_MY -> ASIA_KUALA_LUMPUR;
            case ID_ID -> ASIA_JAKARTA;
            case TL_PH -> ASIA_MANILA;
            case MY_MM -> ASIA_YANGON;
            case LO_LA -> ASIA_VIENTIANE;

            // ===== 南亚 =====
            case HI_IN, BN_BD -> ASIA_KOLKATA;

            // ===== 中东 =====
            case AR_SA, AR_AE -> ASIA_RIYADH;
            case HE_IL -> ASIA_JERUSALEM;

            // ===== 欧洲 =====
            case FR_FR -> EUROPE_PARIS;
            case DE_DE -> EUROPE_BERLIN;
            case ES_ES -> EUROPE_MADRID;
            case RU_RU -> EUROPE_MOSCOW;

            default -> UTC;
        };
    }

}