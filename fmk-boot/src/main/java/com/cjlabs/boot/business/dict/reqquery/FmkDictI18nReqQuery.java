package com.cjlabs.boot.business.dict.reqquery;

import lombok.Data;

/**
 * fmk_dict_i18n 系统字典多语言表
 * <p>
 * 2025-12-04 08:17:46
 */
@Data
public class FmkDictI18nReqQuery {

    /**
     * 字典类型，与主表一致
     */
    private String dictType;

    /**
     * 字典键，与主表一致
     */
    private String dictKey;

}