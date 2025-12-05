package com.cjlabs.boot.business.multilang.mapper;

import com.cjlabs.boot.business.multilang.mysql.FmkMultiLanguageMessage;
import com.cjlabs.boot.business.multilang.reqquery.FmkMultiLanguageMessageReqQuery;
import com.cjlabs.db.mp.FmkService;
import com.cjlabs.db.domain.FmkOrderItem;
import com.cjlabs.db.domain.FmkPageResponse;
import com.cjlabs.db.domain.FmkRequest;
import com.cjlabs.web.check.FmkCheckUtil;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * fmk_multi_language_message System message content table; 系统消息内容表
 * <p>
 * 2025-12-05 02:39:34
 */
@Slf4j
@Service
public class FmkMultiLanguageMessageWrapMapper extends FmkService<FmkMultiLanguageMessageMapper, FmkMultiLanguageMessage> {

    protected FmkMultiLanguageMessageWrapMapper(FmkMultiLanguageMessageMapper mapper) {
        super(mapper);
    }

    @Override
    protected Class<FmkMultiLanguageMessage> getEntityClass() {
        return FmkMultiLanguageMessage.class;
    }

    /**
     * 分页查询
     */
    public FmkPageResponse<FmkMultiLanguageMessage> pageQuery(FmkRequest<FmkMultiLanguageMessageReqQuery> input) {
        // 参数校验
        FmkCheckUtil.checkInput(Objects.isNull(input));
        FmkCheckUtil.checkInput(Objects.isNull(input.getRequest()));

        // 构建分页对象
        Page<FmkMultiLanguageMessage> page = new Page<>(input.getCurrent(), input.getSize());
        FmkMultiLanguageMessageReqQuery request = input.getRequest();

        // 构建查询条件
        LambdaQueryWrapper<FmkMultiLanguageMessage> lambdaQuery = buildLambdaQuery();
        if (StringUtils.isNotBlank(request.getMessageType())) {
            lambdaQuery.eq(FmkMultiLanguageMessage::getMessageType, request.getMessageType());
        }
        if (StringUtils.isNotBlank(request.getMessageKey())) {
            lambdaQuery.eq(FmkMultiLanguageMessage::getMessageKey, request.getMessageKey());
        }
        if (StringUtils.isNotBlank(request.getMessageType())) {
            lambdaQuery.eq(FmkMultiLanguageMessage::getMessageType, request.getMessageType());
        }
        if (Objects.nonNull(request.getLanguageCode())) {
            lambdaQuery.eq(FmkMultiLanguageMessage::getLanguageCode, request.getLanguageCode());
        }
        if (CollectionUtils.isNotEmpty(request.getMessageKeyList())) {
            lambdaQuery.in(FmkMultiLanguageMessage::getMessageKey, request.getMessageKeyList());
        }

        List<FmkOrderItem> orderItemList = input.getOrderItemList();

        // 执行分页查询
        IPage<FmkMultiLanguageMessage> dbPage = super.pageByCondition(page, lambdaQuery, orderItemList);

        return FmkPageResponse.of(dbPage);
    }


}