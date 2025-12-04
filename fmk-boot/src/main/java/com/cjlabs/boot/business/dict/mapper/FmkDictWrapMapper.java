package com.cjlabs.boot.business.dict.mapper;

import com.cjlabs.boot.business.dict.mysql.FmkDict;
import com.cjlabs.boot.business.dict.reqquery.FmkDictReqQuery;
import com.cjlabs.db.mp.FmkService;
import com.cjlabs.db.domain.FmkOrderItem;
import com.cjlabs.db.domain.FmkPageResponse;
import com.cjlabs.db.domain.FmkRequest;
import com.cjlabs.web.check.FmkCheckUtil;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * fmk_dict 系统字典主表
 * <p>
 * 2025-12-04 08:17:46
 */
@Slf4j
@Service
public class FmkDictWrapMapper extends FmkService<FmkDictMapper, FmkDict> {

    protected FmkDictWrapMapper(FmkDictMapper mapper) {
        super(mapper);
    }

    @Override
    protected Class<FmkDict> getEntityClass() {
        return FmkDict.class;
    }

    /**
     * 分页查询
     */
    public FmkPageResponse<FmkDict> pageQuery(FmkRequest<FmkDictReqQuery> input) {
        // 参数校验
        FmkCheckUtil.checkInput(Objects.isNull(input));
        FmkCheckUtil.checkInput(Objects.isNull(input.getRequest()));

        // 构建分页对象
        Page<FmkDict> page = new Page<>(input.getCurrent(), input.getSize());
        FmkDictReqQuery request = input.getRequest();

        // 构建查询条件
        LambdaQueryWrapper<FmkDict> lambdaQuery = buildLambdaQuery();


        List<FmkOrderItem> orderItemList = input.getOrderItemList();

        // 执行分页查询
        IPage<FmkDict> dbPage = super.pageByCondition(page, lambdaQuery, orderItemList);

        return FmkPageResponse.of(dbPage);
    }
}