package com.cjlabs.boot.business.multilang.controller;

import com.cjlabs.boot.business.multilang.mysql.FmkMultiLanguageMessage;
import com.cjlabs.boot.business.multilang.requpdate.FmkMultiLanguageMessageReqSave;
import com.cjlabs.boot.business.multilang.requpdate.FmkMultiLanguageMessageReqUpdate;
import com.cjlabs.boot.business.multilang.service.FmkMultiLanguageMessageApiService;
import com.cjlabs.db.domain.FmkRequest;
import com.cjlabs.web.threadlocal.FmkResult;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * fmk_multi_language_message System message content table; 系统消息内容表
 * <p>
 * 2025-12-05 03:41:50
 */
@Slf4j
@RestController
@RequestMapping("/fmkApi/multiLanguage")
public class FmkMultiLanguageMessageUpdateController {

    @Autowired
    private FmkMultiLanguageMessageApiService fmkMultiLanguageMessageApiService;

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