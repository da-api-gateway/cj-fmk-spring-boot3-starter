package com.cjlabs.boot.business.dict.controller;

import com.cjlabs.boot.business.dict.reqquery.FmkDictI18nReqQuery;
import com.cjlabs.boot.business.dict.reqsave.FmkDictI18nReqSave;
import com.cjlabs.boot.business.dict.requpdate.FmkDictI18nReqUpdate;
import com.cjlabs.boot.business.dict.resp.FmkDictI18nResp;
import com.cjlabs.boot.business.dict.service.FmkDictI18nService;
import com.cjlabs.db.domain.FmkRequest;
import com.cjlabs.web.threadlocal.FmkResult;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * fmk_dict_i18n 系统字典多语言表
 * <p>
 * 2025-12-04 08:17:46
 */
@Slf4j
@RestController
@RequestMapping("/fmkApi/dictI18n")
public class FmkDictI18nController {

    @Autowired
    private FmkDictI18nService fmkDictI18nApiService;

    /**
     * 查询所有（不分页）
     */
    @PostMapping("/list")
    public FmkResult<List<FmkDictI18nResp>> listByDictType(@RequestBody FmkRequest<FmkDictI18nReqQuery> input) {
        List<FmkDictI18nResp> list = fmkDictI18nApiService.listByDictType(input);
        return FmkResult.success(list);
    }

    /**
     * 新增
     */
    @PostMapping("/save")
    public FmkResult<FmkDictI18nResp> save(@RequestBody FmkRequest<FmkDictI18nReqSave> input) {
        FmkDictI18nResp fmkDictI18n = fmkDictI18nApiService.save(input);
        return FmkResult.success(fmkDictI18n);
    }

    /**
     * 更新
     */
    @PostMapping("/update/byId")
    public FmkResult<Boolean> update(@RequestBody FmkRequest<FmkDictI18nReqUpdate> input) {
        boolean result = fmkDictI18nApiService.update(input);
        return FmkResult.success(result);
    }

}