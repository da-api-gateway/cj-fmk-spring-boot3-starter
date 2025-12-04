package com.cjlabs.boot.business.dict.controller;

import com.cjlabs.boot.business.dict.reqquery.FmkDictReqQuery;
import com.cjlabs.boot.business.dict.reqsave.FmkDictReqSave;
import com.cjlabs.boot.business.dict.requpdate.FmkDictReqUpdate;
import com.cjlabs.boot.business.dict.resp.FmkDictResp;
import com.cjlabs.boot.business.dict.service.FmkDictService;
import com.cjlabs.db.domain.FmkPageResponse;
import com.cjlabs.db.domain.FmkRequest;
import com.cjlabs.web.threadlocal.FmkResult;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * fmk_dict 系统字典主表
 * <p>
 * 2025-12-04 08:17:46
 */
@Slf4j
@RestController
@RequestMapping("/fmkApi/dict")
public class FmkDictController {

    @Autowired
    private FmkDictService fmkDictService;

    /**
     * 分页查询
     */
    @PostMapping("/page")
    public FmkResult<FmkPageResponse<FmkDictResp>> page(@RequestBody FmkRequest<FmkDictReqQuery> input) {
        FmkPageResponse<FmkDictResp> page = fmkDictService.pageQuery(input);
        return FmkResult.success(page);
    }

    /**
     * 根据 ID 查询
     */
    @PostMapping("/get/byId")
    public FmkResult<FmkDictResp> getById(@RequestBody FmkRequest<Void> input) {
        FmkDictResp resp = fmkDictService.getById(input);
        return FmkResult.success(resp);
    }

    /**
     * 新增
     */
    @PostMapping("/save")
    public FmkResult<FmkDictResp> save(@RequestBody FmkRequest<FmkDictReqSave> input) {
        FmkDictResp save = fmkDictService.save(input);
        return FmkResult.success(save);
    }

    /**
     * 更新
     */
    @PostMapping("/update/byId")
    public FmkResult<Boolean> update(@RequestBody FmkRequest<FmkDictReqUpdate> input) {
        boolean result = fmkDictService.update(input);
        return FmkResult.success(result);
    }

}