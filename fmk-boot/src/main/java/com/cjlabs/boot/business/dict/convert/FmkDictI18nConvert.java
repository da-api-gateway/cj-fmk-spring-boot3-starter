package com.cjlabs.boot.business.dict.convert;

import com.cjlabs.boot.business.dict.mysql.FmkDictI18n;
import com.cjlabs.boot.business.dict.resp.FmkDictI18nResp;

import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class FmkDictI18nConvert {

    public static FmkDictI18nResp toResp(FmkDictI18n input) {
        if (Objects.isNull(input)) {
            return null;
        }
        FmkDictI18nResp fmkDictI18nResp = new FmkDictI18nResp();

        fmkDictI18nResp.setDictType(input.getDictType());
        fmkDictI18nResp.setDictKey(input.getDictKey());
        fmkDictI18nResp.setLanguageCode(input.getLanguageCode());
        fmkDictI18nResp.setDictValue(input.getDictValue());
        fmkDictI18nResp.setRemark(input.getRemark());

        return fmkDictI18nResp;
    }

    public static List<FmkDictI18nResp> toResp(List<FmkDictI18n> inputList) {
        if (CollectionUtils.isEmpty(inputList)) {
            return Lists.newArrayList();
        }
        return inputList.stream().map(FmkDictI18nConvert::toResp).collect(Collectors.toList());
    }
}