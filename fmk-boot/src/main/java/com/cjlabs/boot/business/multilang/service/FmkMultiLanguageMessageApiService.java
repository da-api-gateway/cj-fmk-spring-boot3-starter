package com.cjlabs.boot.business.multilang.service;

import com.cjlabs.boot.business.multilang.convert.FmkMultiLanguageMessageConvert;
import com.cjlabs.boot.business.multilang.mysql.FmkMultiLanguageMessage;
import com.cjlabs.boot.business.multilang.reqquery.FmkMultiLanguageMessageReqQuery;
import com.cjlabs.boot.business.multilang.requpdate.FmkMultiLanguageMessageReqSave;
import com.cjlabs.boot.business.multilang.requpdate.FmkMultiLanguageMessageReqUpdate;
import com.cjlabs.boot.business.multilang.resp.FmkMultiLanguageMessageResp;
import com.cjlabs.db.domain.FmkPageResponse;
import com.cjlabs.db.domain.FmkRequest;
import com.cjlabs.web.check.FmkCheckUtil;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * fmk_multi_language_message System message content table; 系统消息内容表
 * <p>
 * 2025-12-05 03:41:50
 */
@Slf4j
@Service
public class FmkMultiLanguageMessageApiService {

    @Autowired
    private FmkMultiLanguageMessageService fmkMultiLanguageMessageService;

    public FmkMultiLanguageMessageResp getById(FmkRequest<Void> input) {
        // 参数校验
        FmkCheckUtil.checkInput(Objects.isNull(input));
        FmkCheckUtil.checkInput(StringUtils.isBlank(input.getBusinessKey()));

        FmkMultiLanguageMessage fmkMultiLanguageMessage = fmkMultiLanguageMessageService.getById(input);
        return FmkMultiLanguageMessageConvert.toResp(fmkMultiLanguageMessage);
    }

    public FmkMultiLanguageMessage save(FmkRequest<FmkMultiLanguageMessageReqSave> input) {
        // 参数校验
        FmkCheckUtil.checkInput(Objects.isNull(input));
        FmkCheckUtil.checkInput(Objects.isNull(input.getRequest()));

        FmkMultiLanguageMessageReqSave request = input.getRequest();
        if (Objects.isNull(request)) {
            log.info("FmkMultiLanguageMessageApiService|save|request is null");
            return null;
        }

        return fmkMultiLanguageMessageService.save(request);
    }


    public boolean update(FmkRequest<FmkMultiLanguageMessageReqUpdate> input) {
        // 参数校验
        FmkCheckUtil.checkInput(Objects.isNull(input));
        FmkCheckUtil.checkInput(Objects.isNull(input.getRequest()));

        FmkMultiLanguageMessageReqUpdate request = input.getRequest();
        if (Objects.isNull(request)) {
            log.info("FmkMultiLanguageMessageApiService|update|request is null");
            return false;
        }
        return fmkMultiLanguageMessageService.update(request);
    }

    public boolean deleteById(FmkRequest<Void> input) {
        // 参数校验
        FmkCheckUtil.checkInput(Objects.isNull(input));
        FmkCheckUtil.checkInput(Objects.isNull(input.getBusinessKey()));

        String businessKey = input.getBusinessKey();
        if (businessKey == null) {
            log.info("FmkMultiLanguageMessageApiService|deleteById|request is null");
            return false;
        }
        return fmkMultiLanguageMessageService.deleteById(businessKey);
    }

    /**
     * 查询所有（不分页）
     */
    public List<FmkMultiLanguageMessageResp> listAll() {
        List<FmkMultiLanguageMessage> entityList = fmkMultiLanguageMessageService.listAll();
        List<FmkMultiLanguageMessageResp> respList = FmkMultiLanguageMessageConvert.toResp(entityList);
        return respList;
    }

    /**
     * 分页查询
     */
    public FmkPageResponse<FmkMultiLanguageMessageResp> pageQuery(FmkRequest<FmkMultiLanguageMessageReqQuery> input) {
        // 参数校验
        FmkCheckUtil.checkInput(Objects.isNull(input));
        FmkCheckUtil.checkInput(Objects.isNull(input.getRequest()));

        // 执行分页查询
        FmkPageResponse<FmkMultiLanguageMessage> entityPage = fmkMultiLanguageMessageService.pageQuery(input);

        if (Objects.isNull(entityPage) || CollectionUtils.isEmpty(entityPage.getRecords())) {
            return FmkPageResponse.empty();
        }

        FmkPageResponse<FmkMultiLanguageMessageResp> pageResponse = FmkPageResponse.of(entityPage, FmkMultiLanguageMessageConvert::toResp);

        return pageResponse;
    }

    public Map<String, List<FmkMultiLanguageMessageResp>> listByTypeReturnMap(FmkRequest<FmkMultiLanguageMessageReqQuery> input) {
        FmkCheckUtil.checkInput(Objects.isNull(input));
        FmkCheckUtil.checkInput(Objects.isNull(input.getRequest()));
        FmkMultiLanguageMessageReqQuery request = input.getRequest();
        FmkCheckUtil.checkInput(CollectionUtils.isEmpty(request.getMessageKeyList()));
        // List<String> messageTypeList = request.getMessageTypeList();

        FmkPageResponse<FmkMultiLanguageMessageResp> pagedQuery = pageQuery(input);

        List<FmkMultiLanguageMessageResp> messageRespList = pagedQuery.getRecords();
        if (CollectionUtils.isEmpty(messageRespList)) {
            return Maps.newHashMap();
        }

        return messageRespList.stream().collect(Collectors.groupingBy(FmkMultiLanguageMessageResp::getMessageKey));
    }

    /**
     *
     * @return messageType messageKey Message
     */
    public Map<String, Map<String, List<FmkMultiLanguageMessageResp>>> listAllReturnMap() {
        List<FmkMultiLanguageMessage> messageList = fmkMultiLanguageMessageService.listAll();
        if (CollectionUtils.isEmpty(messageList)) {
            return Maps.newHashMap();
        }

        List<FmkMultiLanguageMessageResp> messageRespList = FmkMultiLanguageMessageConvert.toResp(messageList);
        return messageRespList.stream()
                .collect(Collectors.groupingBy(FmkMultiLanguageMessageResp::getMessageType,
                        Collectors.groupingBy(FmkMultiLanguageMessageResp::getMessageKey)));
    }
}