package com.cjlabs.boot.business.dict.mysql;

import com.cjlabs.db.domain.FmkBaseEntity;

import com.cjlabs.domain.enums.FmkLanguageEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * fmk_dict_i18n 系统字典多语言表
 * <p>
 * 2025-12-04 08:17:46
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class FmkDictI18n extends FmkBaseEntity {

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

    /**
     * 排序号
     */
    private Integer orderNumber;

    /**
     * 描述信息，可选
     */
    private String remark;

}