package com.cjlabs.boot.business.dict.mysql;

import com.cjlabs.db.domain.FmkBaseEntity;
import com.cjlabs.domain.enums.NormalEnum;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * fmk_dict 系统字典主表
 *
 * 2025-12-04 08:17:46
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class FmkDict extends FmkBaseEntity {

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