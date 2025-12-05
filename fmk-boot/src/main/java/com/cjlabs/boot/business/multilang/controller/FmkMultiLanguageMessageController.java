package com.cjlabs.boot.business.multilang.controller;

import com.cjlabs.boot.business.multilang.mysql.FmkMultiLanguageMessage;
import com.cjlabs.boot.business.multilang.reqquery.FmkMultiLanguageMessageReqQuery;
import com.cjlabs.boot.business.multilang.requpdate.FmkMultiLanguageMessageReqSave;
import com.cjlabs.boot.business.multilang.requpdate.FmkMultiLanguageMessageReqUpdate;
import com.cjlabs.boot.business.multilang.resp.FmkMultiLanguageMessageResp;
import com.cjlabs.boot.business.multilang.service.FmkMultiLanguageMessageApiService;
import com.cjlabs.db.domain.FmkPageResponse;
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
 * fmk_multi_language_message System message content table; 系统消息内容表
 * <p>
 * 2025-12-05 03:41:50
 */
@Slf4j
@RestController
@RequestMapping("/fmkApi/multiLanguage")
public class FmkMultiLanguageMessageController {

    @Autowired
    private FmkMultiLanguageMessageApiService fmkMultiLanguageMessageApiService;

    /**
     * 分页查询
     */
    @PostMapping("/page")
    public FmkResult<FmkPageResponse<FmkMultiLanguageMessageResp>> page(@RequestBody FmkRequest<FmkMultiLanguageMessageReqQuery> input) {
        FmkPageResponse<FmkMultiLanguageMessageResp> page = fmkMultiLanguageMessageApiService.pageQuery(input);
        return FmkResult.success(page);
    }

    /**
     * 查询所有（不分页）
     */
    @PostMapping("/list")
    public FmkResult<List<FmkMultiLanguageMessageResp>> list() {
        List<FmkMultiLanguageMessageResp> list = fmkMultiLanguageMessageApiService.listAll();
        return FmkResult.success(list);
    }

    /**
     * 根据 ID 查询
     */
    @PostMapping("/get/byId")
    public FmkResult<FmkMultiLanguageMessageResp> getById(@RequestBody FmkRequest<Void> input) {
        FmkMultiLanguageMessageResp resp = fmkMultiLanguageMessageApiService.getById(input);
        return FmkResult.success(resp);
    }

    /**
     * 新增
     */
    @PostMapping("/save")
    public FmkResult<FmkMultiLanguageMessage> save(@RequestBody FmkRequest<FmkMultiLanguageMessageReqSave> input) {
        FmkMultiLanguageMessage result = fmkMultiLanguageMessageApiService.save(input);
        return FmkResult.success(result);
    }

    /**
     * 更新
     */
    @PostMapping("/update/byId")
    public FmkResult<Boolean> update(@RequestBody FmkRequest<FmkMultiLanguageMessageReqUpdate> input) {
        boolean result = fmkMultiLanguageMessageApiService.update(input);
        return FmkResult.success(result);
    }

    /**
     * 删除
     */
    @PostMapping("/delete/byId")
    public FmkResult<Boolean> delete(@RequestBody FmkRequest<Void> input) {
        boolean result = fmkMultiLanguageMessageApiService.deleteById(input);
        return FmkResult.success(result);
    }

}