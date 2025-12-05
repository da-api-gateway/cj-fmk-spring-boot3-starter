package com.cjlabs.boot.business.dict.convert;

import com.cjlabs.boot.business.dict.mysql.FmkDict;
import com.cjlabs.boot.business.dict.resp.FmkDictResp;

import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class FmkDictConvert {

    public static FmkDictResp toResp(FmkDict input) {
        if (Objects.isNull(input)) {
            return null;
        }
        FmkDictResp fmkDictResp = new FmkDictResp();

        fmkDictResp.setId(input.getId());
        fmkDictResp.setDictType(input.getDictType());
        fmkDictResp.setStatus(input.getStatus());
        fmkDictResp.setRemark(input.getRemark());
        fmkDictResp.setCreateUser(input.getCreateUser());
        fmkDictResp.setCreateDate(input.getCreateDate());
        fmkDictResp.setUpdateUser(input.getUpdateUser());
        fmkDictResp.setUpdateDate(input.getUpdateDate());

        return fmkDictResp;
    }

    public static List<FmkDictResp> toResp(List<FmkDict> inputList) {
        if (CollectionUtils.isEmpty(inputList)) {
            return Lists.newArrayList();
        }
        return inputList.stream().map(FmkDictConvert::toResp).collect(Collectors.toList());
    }
}