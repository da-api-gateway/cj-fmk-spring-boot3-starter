package com.cjlabs.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 常用语言的 Accept-Language 请求头枚举
 */
@Getter
@AllArgsConstructor
public enum LanguageEnum implements IEnumStr {

    ZH_CN("zh-CN", "简体中文（中国大陆）"),
    ZH_TW("zh-TW", "繁体中文（台湾）"),
    EN_US("en-US", "英语（美国）"),
    EN_GB("en-GB", "英语（英国）"),
    JA_JP("ja-JP", "日语（日本）"),
    KO_KR("ko-KR", "韩语（韩国）"),
    FR_FR("fr-FR", "法语（法国）"),
    DE_DE("de-DE", "德语（德国）"),
    ES_ES("es-ES", "西班牙语（西班牙）"),
    RU_RU("ru-RU", "俄语（俄罗斯）"),
    TH_TH("th-TH", "泰语（泰国）"),
    VI_VN("vi-VN", "越南语（越南）"),


    ;

    private final String code;
    private final String msg;
}
