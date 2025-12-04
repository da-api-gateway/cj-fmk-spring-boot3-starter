package com.cjlabs.boot.business.dict.service;

import com.cjlabs.boot.business.dict.convert.FmkDictConvert;
import com.cjlabs.boot.business.dict.convert.FmkDictReqConvert;
import com.cjlabs.boot.business.dict.mapper.FmkDictWrapMapper;
import com.cjlabs.boot.business.dict.mysql.FmkDict;
import com.cjlabs.boot.business.dict.reqquery.FmkDictReqQuery;
import com.cjlabs.boot.business.dict.reqsave.FmkDictReqSave;
import com.cjlabs.boot.business.dict.requpdate.FmkDictReqUpdate;
import com.cjlabs.boot.business.dict.resp.FmkDictResp;
import com.cjlabs.db.domain.FmkPageResponse;
import com.cjlabs.db.domain.FmkRequest;
import com.cjlabs.web.check.FmkCheckUtil;
import com.cjlabs.web.exception.Error200ExceptionEnum;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * fmk_dict 系统字典主表
 * <p>
 * 2025-12-04 08:17:46
 */
@Slf4j
@Service
public class FmkDictService {

    @Autowired
    private FmkDictWrapMapper fmkDictWrapMapper;

    public FmkDictResp getById(FmkRequest<Void> input) {
        // 参数校验
        FmkCheckUtil.checkInput(Objects.isNull(input));
        FmkCheckUtil.checkInput(StringUtils.isBlank(input.getBusinessKey()));

        String id = input.getBusinessKey();

        FmkDict fmkDict = fmkDictWrapMapper.getById(id);
        return FmkDictConvert.toResp(fmkDict);
    }

    public FmkDictResp save(FmkRequest<FmkDictReqSave> input) {
        FmkCheckUtil.checkInput(Objects.isNull(input));
        FmkCheckUtil.checkInput(Objects.isNull(input.getRequest()));

        FmkDictReqSave request = input.getRequest();

        FmkDict db = FmkDictReqConvert.toDb(request);
        int saved = fmkDictWrapMapper.save(db);
        FmkCheckUtil.throw200Error(saved == 0, Error200ExceptionEnum.DATA_NOT_FOUND);

        return FmkDictConvert.toResp(db);
    }

    public boolean update(FmkRequest<FmkDictReqUpdate> input) {
        FmkCheckUtil.checkInput(Objects.isNull(input));
        FmkCheckUtil.checkInput(Objects.isNull(input.getRequest()));

        FmkDictReqUpdate request = input.getRequest();
        FmkDict db = FmkDictReqConvert.toDb(request);

        int updated = fmkDictWrapMapper.updateById(db);
        if (updated > 0) {
            log.info("FmkDictService|update|update={}|id={}", updated, request.getId());
            return true;
        }
        return false;
    }

    /**
     * 分页查询
     */
    public FmkPageResponse<FmkDictResp> pageQuery(FmkRequest<FmkDictReqQuery> input) {
        // 参数校验
        FmkCheckUtil.checkInput(Objects.isNull(input));
        FmkCheckUtil.checkInput(Objects.isNull(input.getRequest()));

        // 执行分页查询
        FmkPageResponse<FmkDict> entityPage = fmkDictWrapMapper.pageQuery(input);
        // 执行分页查询
        if (Objects.isNull(entityPage) || CollectionUtils.isEmpty(entityPage.getRecords())) {
            return FmkPageResponse.empty();
        }

        FmkPageResponse<FmkDictResp> pageResponse = FmkPageResponse.of(entityPage, FmkDictConvert::toResp);

        return pageResponse;
    }

}