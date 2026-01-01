package com.cjlabs.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 常用语言枚举，与数据库 language_code 保持一致（下划线格式，如 zh_CN）
 * 前端 Accept-Language 统一使用该格式
 */
@Getter
@AllArgsConstructor
public enum FmkLanguageEnum implements IEnumStr {

    // ===== 中文系 =====
    ZH_CN("ZH_CN", "简体中文（中国大陆）"),
    ZH_TW("ZH_TW", "繁体中文（台湾）"),
    ZH_HK("ZH_HK", "繁体中文（香港）"),
    ZH_SG("ZH_SG", "中文（新加坡）"),

    // ===== 英语系 =====
    EN_US("EN_US", "英语（美国）"),
    EN_GB("EN_GB", "英语（英国）"),
    EN_SG("EN_SG", "英语（新加坡）"),
    EN_IN("EN_IN", "英语（印度）"),
    EN_PH("EN_PH", "英语（菲律宾）"),

    // ===== 东亚 =====
    JA_JP("JA_JP", "日语（日本）"),
    KO_KR("KO_KR", "韩语（韩国）"),

    // ===== 东南亚 =====
    TH_TH("TH_TH", "泰语（泰国）"),
    VI_VN("VI_VN", "越南语（越南）"),
    ID_ID("ID_ID", "印尼语（印度尼西亚）"),
    MS_MY("MS_MY", "马来语（马来西亚）"),
    TL_PH("TL_PH", "他加禄语（菲律宾）"),
    KM_KH("KM_KH", "高棉语（柬埔寨）"),
    LO_LA("LO_LA", "老挝语（老挝）"),
    MY_MM("MY_MM", "缅甸语（缅甸）"),

    // ===== 南亚 =====
    HI_IN("HI_IN", "印地语（印度）"),
    BN_BD("BN_BD", "孟加拉语（孟加拉国）"),
    UR_PK("UR_PK", "乌尔都语（巴基斯坦）"),
    NE_NP("NE_NP", "尼泊尔语（尼泊尔）"),
    SI_LK("SI_LK", "僧伽罗语（斯里兰卡）"),
    TA_LK("TA_LK", "泰米尔语（斯里兰卡）"),

    // ===== 中东 =====
    AR_SA("AR_SA", "阿拉伯语（沙特）"),
    AR_AE("AR_AE", "阿拉伯语（阿联酋）"),
    HE_IL("HE_IL", "希伯来语（以色列）"),

    // ===== 欧洲 / 其他（你已有）=====
    FR_FR("FR_FR", "法语（法国）"),
    DE_DE("DE_DE", "德语（德国）"),
    ES_ES("ES_ES", "西班牙语（西班牙）"),
    RU_RU("RU_RU", "俄语（俄罗斯）"),
    ;

    /** 数据库存储 & 前端传递的语言编码 */
    private final String code;

    /** 中文描述 */
    private final String msg;
}
