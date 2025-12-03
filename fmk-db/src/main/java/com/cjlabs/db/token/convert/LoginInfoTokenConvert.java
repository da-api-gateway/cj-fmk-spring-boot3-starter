package com.cjlabs.db.token.convert;

import com.cjlabs.domain.sysuserinfotoken.resp.SysUserInfoTokenResp;
import com.cjlabs.service.sysuserinfotoken.mysql.SysUserInfoToken;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


public class LoginInfoTokenConvert {

    public static SysUserInfoTokenResp toResp(SysUserInfoToken input) {
        if (Objects.isNull(input)) {
            return null;
        }
        SysUserInfoTokenResp sysUserInfoTokenResp = new SysUserInfoTokenResp();

        sysUserInfoTokenResp.setUserId(input.getUserId());
        sysUserInfoTokenResp.setUsername(input.getUsername());
        sysUserInfoTokenResp.setToken(input.getToken());
        sysUserInfoTokenResp.setClientType(input.getClientType());
        sysUserInfoTokenResp.setIpAddress(input.getIpAddress());
        sysUserInfoTokenResp.setUserAgent(input.getUserAgent());
        sysUserInfoTokenResp.setExpireTime(input.getExpireTime());
        sysUserInfoTokenResp.setStatus(input.getStatus());
        sysUserInfoTokenResp.setRemark(input.getRemark());

        return sysUserInfoTokenResp;
    }

    public static List<SysUserInfoTokenResp> toResp(List<SysUserInfoToken> inputList) {
        if (CollectionUtils.isEmpty(inputList)) {
            return Lists.newArrayList();
        }
        return inputList.stream().map(LoginInfoTokenConvert::toResp).collect(Collectors.toList());
    }
}