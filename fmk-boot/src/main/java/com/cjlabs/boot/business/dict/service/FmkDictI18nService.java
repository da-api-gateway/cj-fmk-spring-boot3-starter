package com.cjlabs.boot.business.dict.service;

import com.cjlabs.boot.business.dict.convert.FmkDictI18nConvert;
import com.cjlabs.boot.business.dict.convert.FmkDictI18nReqConvert;
import com.cjlabs.boot.business.dict.mapper.FmkDictI18nWrapMapper;
import com.cjlabs.boot.business.dict.mysql.FmkDictI18n;
import com.cjlabs.boot.business.dict.reqquery.FmkDictI18nReqQuery;
import com.cjlabs.boot.business.dict.reqsave.FmkDictI18nReqSave;
import com.cjlabs.boot.business.dict.requpdate.FmkDictI18nReqUpdate;
import com.cjlabs.boot.business.dict.resp.FmkDictI18nResp;
import com.cjlabs.db.domain.FmkRequest;
import com.cjlabs.web.check.FmkCheckUtil;
import com.cjlabs.web.exception.Error200ExceptionEnum;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * fmk_dict_i18n 系统字典多语言表
 * <p>
 * 2025-12-04 08:17:46
 */
@Slf4j
@Service
public class FmkDictI18nService {

    @Autowired
    private FmkDictI18nWrapMapper fmkDictI18nWrapMapper;

    public FmkDictI18nResp save(FmkRequest<FmkDictI18nReqSave> input) {
        FmkCheckUtil.checkInput(Objects.isNull(input));
        FmkCheckUtil.checkInput(Objects.isNull(input.getRequest()));

        FmkDictI18nReqSave request = input.getRequest();
        FmkDictI18n fmkDictI18n = FmkDictI18nReqConvert.toDb(request);

        int saved = fmkDictI18nWrapMapper.save(fmkDictI18n);
        FmkCheckUtil.throw200Error(saved == 0, Error200ExceptionEnum.DATA_NOT_FOUND);
        return FmkDictI18nConvert.toResp(fmkDictI18n);
    }

    public boolean update(FmkRequest<FmkDictI18nReqUpdate> input) {
        FmkCheckUtil.checkInput(Objects.isNull(input));
        FmkCheckUtil.checkInput(Objects.isNull(input.getRequest()));

        FmkDictI18nReqUpdate request = input.getRequest();
        FmkDictI18n fmkDictI18n = FmkDictI18nReqConvert.toDb(request);

        int updated = fmkDictI18nWrapMapper.updateById(fmkDictI18n);
        if (updated > 0) {
            log.info("FmkDictI18nService|update|update={}|id={}", updated, request.getId());
            return true;
        }
        return false;
    }

    public List<FmkDictI18nResp> listByDictType(FmkRequest<FmkDictI18nReqQuery> input) {
        // 参数校验
        FmkCheckUtil.checkInput(Objects.isNull(input));
        FmkCheckUtil.checkInput(Objects.isNull(input.getRequest()));

        List<FmkDictI18n> fmkDictI18nList = fmkDictI18nWrapMapper.listByDictType(input.getRequest());
        return FmkDictI18nConvert.toResp(fmkDictI18nList);
    }
}