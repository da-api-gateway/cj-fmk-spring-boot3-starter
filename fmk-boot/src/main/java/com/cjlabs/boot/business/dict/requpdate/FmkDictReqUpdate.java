package com.cjlabs.boot.business.dict.requpdate;

import com.cjlabs.domain.enums.NormalEnum;
import lombok.Data;

import java.time.Instant;

/**
 * fmk_dict 系统字典主表
 *
 * 2025-12-04 08:17:46
 */
@Data
public class FmkDictReqUpdate {
    
    /**
     * ID（更新时必填）
     */
    private Long id;
    
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