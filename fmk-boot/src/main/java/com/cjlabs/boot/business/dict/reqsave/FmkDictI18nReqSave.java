package com.cjlabs.boot.business.dict.reqsave;

import com.cjlabs.domain.enums.FmkLanguageEnum;
import lombok.Data;

import java.time.Instant;

/**
 * fmk_dict_i18n 系统字典多语言表
 * <p>
 * 2025-12-04 08:17:46
 */
@Data
public class FmkDictI18nReqSave {

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

    /**
     * 描述信息，可选
     */
    private String remark;

    /**
     * 创建用户
     */
    private String createUser;

    /**
     * 创建时间（UTC毫秒）
     */
    private Instant createDate;

    /**
     * 更新用户
     */
    private String updateUser;

}