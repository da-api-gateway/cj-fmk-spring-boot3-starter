package com.cjlabs.boot.business.multilang.service;

import com.cjlabs.boot.business.multilang.convert.FmkMultiLanguageMessageReqConvert;
import com.cjlabs.boot.business.multilang.mapper.FmkMultiLanguageMessageWrapMapper;
import com.cjlabs.boot.business.multilang.mysql.FmkMultiLanguageMessage;
import com.cjlabs.boot.business.multilang.reqquery.FmkMultiLanguageMessageReqQuery;
import com.cjlabs.boot.business.multilang.requpdate.FmkMultiLanguageMessageReqSave;
import com.cjlabs.boot.business.multilang.requpdate.FmkMultiLanguageMessageReqUpdate;
import com.cjlabs.db.domain.FmkPageResponse;
import com.cjlabs.db.domain.FmkRequest;
import com.cjlabs.web.check.FmkCheckUtil;
import com.cjlabs.web.exception.Error200ExceptionEnum;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * fmk_multi_language_message System message content table; 系统消息内容表
 * <p>
 * 2025-12-05 03:41:50
 */
@Slf4j
@Service
public class FmkMultiLanguageMessageService {

    @Autowired
    private FmkMultiLanguageMessageWrapMapper fmkMultiLanguageMessageWrapMapper;

    public FmkMultiLanguageMessage getById(FmkRequest<Void> input) {
        // 参数校验
        FmkCheckUtil.checkInput(Objects.isNull(input));
        FmkCheckUtil.checkInput(StringUtils.isBlank(input.getBusinessKey()));

        String id = input.getBusinessKey();
        return fmkMultiLanguageMessageWrapMapper.getById(id);
    }

    public FmkMultiLanguageMessage save(FmkMultiLanguageMessageReqSave request) {
        FmkCheckUtil.checkInput(Objects.isNull(request));

        FmkMultiLanguageMessage db = FmkMultiLanguageMessageReqConvert.toDb(request);

        int saved = fmkMultiLanguageMessageWrapMapper.save(db);
        FmkCheckUtil.throw200Error(saved == 0, Error200ExceptionEnum.DATA_NOT_FOUND);
        return db;
    }


    public boolean update(FmkMultiLanguageMessageReqUpdate request) {
        FmkCheckUtil.checkInput(Objects.isNull(request));

        FmkMultiLanguageMessage db = FmkMultiLanguageMessageReqConvert.toDb(request);

        int updated = fmkMultiLanguageMessageWrapMapper.updateById(db);
        if (updated > 0) {
            log.info("FmkMultiLanguageMessageService|update|update={}|id={}", updated, request.getId());
            return true;
        }
        return false;
    }

    public boolean deleteById(String businessKey) {
        // 参数校验
        FmkCheckUtil.checkInput(StringUtils.isBlank(businessKey));

        int deleted = fmkMultiLanguageMessageWrapMapper.deleteById(businessKey);
        if (deleted > 0) {
            log.info("FmkMultiLanguageMessageService|deleteById|deleteById={}|id={}", deleted, businessKey);
            return true;
        }
        return false;
    }

    /**
     * 查询所有（不分页）
     */
    public List<FmkMultiLanguageMessage> listAll() {
        List<FmkMultiLanguageMessage> entityList = fmkMultiLanguageMessageWrapMapper.listAllLimitService();
        return entityList;
    }

    /**
     * 分页查询
     */
    public FmkPageResponse<FmkMultiLanguageMessage> pageQuery(FmkRequest<FmkMultiLanguageMessageReqQuery> input) {
        // 参数校验
        FmkCheckUtil.checkInput(Objects.isNull(input));
        FmkCheckUtil.checkInput(Objects.isNull(input.getRequest()));

        // 执行分页查询
        FmkPageResponse<FmkMultiLanguageMessage> entityPage = fmkMultiLanguageMessageWrapMapper.pageQuery(input);

        return entityPage;
    }
}