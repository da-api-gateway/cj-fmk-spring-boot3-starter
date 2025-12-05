package com.cjlabs.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 常用语言枚举，与数据库 language_code 保持一致（使用下划线格式，如 zh_CN）
 * 前端 Accept-Language 请求头直接使用相同格式。
 */
@Getter
@AllArgsConstructor
public enum FmkLanguageEnum implements IEnumStr {

    ZH_CN("ZH_CN", "简体中文（中国大陆）"),
    ZH_TW("ZH_TW", "繁体中文（台湾）"),
    EN_US("EN_US", "英语（美国）"),
    EN_GB("EN_GB", "英语（英国）"),
    JA_JP("JA_JP", "日语（日本）"),
    KO_KR("KO_KR", "韩语（韩国）"),
    FR_FR("FR_FR", "法语（法国）"),
    DE_DE("DE_DE", "德语（德国）"),
    ES_ES("ES_ES", "西班牙语（西班牙）"),
    RU_RU("RU_RU", "俄语（俄罗斯）"),
    TH_TH("TH_TH", "泰语（泰国）"),
    VI_VN("VI_VN", "越南语（越南）"),
    ;

    /** 数据库存储和前端传递的语言编码（下划线格式） */
    private final String code;

    /** 中文描述 */
    private final String msg;

}
