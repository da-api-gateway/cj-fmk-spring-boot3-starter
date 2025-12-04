package com.cjlabs.boot.business.dict.reqsave;

import com.cjlabs.domain.enums.NormalEnum;

import lombok.Data;

import java.util.List;

/**
 * fmk_dict 系统字典主表
 * <p>
 * 2025-12-04 08:17:46
 */
@Data
public class FmkDictReqSave {

    /**
     * 字典类型，例如 gender, currency, country
     */
    private String dictType;

    /**
     * 状态：NORMAL 启用，ABNORMAL 禁用
     */
    private NormalEnum status;

    /**
     * 备注说明
     */
    private String remark;

}