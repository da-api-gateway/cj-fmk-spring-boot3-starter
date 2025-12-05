package com.cjlabs.boot.business.multilang.convert;

import com.cjlabs.boot.business.multilang.mysql.FmkMultiLanguageMessage;
import com.cjlabs.boot.business.multilang.resp.FmkMultiLanguageMessageResp;

import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


public class FmkMultiLanguageMessageConvert {

    public static FmkMultiLanguageMessageResp toResp(FmkMultiLanguageMessage input) {
        if (Objects.isNull(input)) {
            return null;
        }
        FmkMultiLanguageMessageResp fmkMultiLanguageMessageResp = new FmkMultiLanguageMessageResp();

        fmkMultiLanguageMessageResp.setId(input.getId());
        fmkMultiLanguageMessageResp.setMessageType(input.getMessageType());
        fmkMultiLanguageMessageResp.setMessageKey(input.getMessageKey());
        fmkMultiLanguageMessageResp.setLanguageCode(input.getLanguageCode());
        fmkMultiLanguageMessageResp.setMessageValue(input.getMessageValue());
        fmkMultiLanguageMessageResp.setCreateUser(input.getCreateUser());
        fmkMultiLanguageMessageResp.setCreateDate(input.getCreateDate());
        fmkMultiLanguageMessageResp.setUpdateUser(input.getUpdateUser());
        fmkMultiLanguageMessageResp.setUpdateDate(input.getUpdateDate());

        return fmkMultiLanguageMessageResp;
    }

    public static List<FmkMultiLanguageMessageResp> toResp(List<FmkMultiLanguageMessage> inputList) {
        if (CollectionUtils.isEmpty(inputList)) {
            return Lists.newArrayList();
        }
        return inputList.stream().map(FmkMultiLanguageMessageConvert::toResp).collect(Collectors.toList());
    }
}