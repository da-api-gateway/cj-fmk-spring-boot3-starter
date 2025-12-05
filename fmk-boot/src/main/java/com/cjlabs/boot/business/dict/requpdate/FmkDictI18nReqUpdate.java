package com.cjlabs.boot.business.dict.requpdate;

import com.cjlabs.domain.enums.FmkLanguageEnum;
import lombok.Data;

import java.time.Instant;

/**
 * fmk_dict_i18n 系统字典多语言表
 *
 * 2025-12-04 08:17:46
 */
@Data
public class FmkDictI18nReqUpdate {
    
    /**
     * ID（更新时必填）
     */
    private Long id;
    
    /**
     * 字典类型，与主表一致
     */
    private String dictType;

    /**
     * 字典键，与主表一致
     */
    private String dictKey;

    /**
     * 语言代码，例如 zh, en, ja
     */
    private FmkLanguageEnum languageCode;

    /**
     * 显示名称，例如：男 / Male / 男性
     */
    private String dictValue;

    private Integer orderNumber;

    /**
     * 描述信息，可选
     */
    private String remark;

}