package com.cjlabs.boot.business.dict.mapper;

import com.cjlabs.boot.business.dict.mysql.FmkDictI18n;
import com.cjlabs.boot.business.dict.reqquery.FmkDictI18nReqQuery;
import com.cjlabs.db.mp.FmkService;
import com.cjlabs.db.domain.FmkOrderItem;
import com.cjlabs.db.domain.FmkPageResponse;
import com.cjlabs.db.domain.FmkRequest;
import com.cjlabs.web.check.FmkCheckUtil;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
public class FmkDictI18nWrapMapper extends FmkService<FmkDictI18nMapper, FmkDictI18n> {

    protected FmkDictI18nWrapMapper(FmkDictI18nMapper mapper) {
        super(mapper);
    }

    @Override
    protected Class<FmkDictI18n> getEntityClass() {
        return FmkDictI18n.class;
    }

    /**
     * 分页查询
     */
    public FmkPageResponse<FmkDictI18n> pageQuery(FmkRequest<FmkDictI18nReqQuery> input) {
        // 参数校验
        FmkCheckUtil.checkInput(Objects.isNull(input));
        FmkCheckUtil.checkInput(Objects.isNull(input.getRequest()));

        // 构建分页对象
        Page<FmkDictI18n> page = new Page<>(input.getCurrent(), input.getSize());
        FmkDictI18nReqQuery request = input.getRequest();

        // 构建查询条件
        LambdaQueryWrapper<FmkDictI18n> lambdaQuery = buildLambdaQuery();


        List<FmkOrderItem> orderItemList = input.getOrderItemList();

        // 执行分页查询
        IPage<FmkDictI18n> dbPage = super.pageByCondition(page, lambdaQuery, orderItemList);

        return FmkPageResponse.of(dbPage);
    }

    public List<FmkDictI18n> listByDictType(FmkDictI18nReqQuery input) {
        FmkCheckUtil.checkInput(Objects.isNull(input));

        LambdaQueryWrapper<FmkDictI18n> lambdaQuery = buildLambdaQuery();
        lambdaQuery.eq(FmkDictI18n::getDictType, input.getDictType());

        if (StringUtils.isNotBlank(input.getDictKey())) {
            lambdaQuery.eq(FmkDictI18n::getDictKey, input.getDictKey());
        }
        return super.listByCondition(lambdaQuery);
    }
}