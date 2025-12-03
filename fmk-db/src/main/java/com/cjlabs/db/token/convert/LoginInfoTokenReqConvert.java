package com.cjlabs.db.token.convert;

import com.cjlabs.domain.sysuserinfotoken.reqquery.SysUserInfoTokenReqQuery;
import com.cjlabs.domain.sysuserinfotoken.requpdate.SysUserInfoTokenReqSave;
import com.cjlabs.domain.sysuserinfotoken.requpdate.SysUserInfoTokenReqUpdate;
import com.cjlabs.service.sysuserinfotoken.mysql.SysUserInfoToken;

import java.util.Objects;


public class LoginInfoTokenReqConvert {

    public static SysUserInfoToken toDb(SysUserInfoTokenReqQuery input) {
        if (Objects.isNull(input)) {
            return null;
        }
        SysUserInfoToken sysUserInfoToken = new SysUserInfoToken();

        sysUserInfoToken.setUserId(input.getUserId());
        sysUserInfoToken.setUsername(input.getUsername());
        sysUserInfoToken.setToken(input.getToken());
        sysUserInfoToken.setClientType(input.getClientType());
        sysUserInfoToken.setIpAddress(input.getIpAddress());
        sysUserInfoToken.setUserAgent(input.getUserAgent());
        sysUserInfoToken.setExpireTime(input.getExpireTime());
        sysUserInfoToken.setStatus(input.getStatus());
        sysUserInfoToken.setRemark(input.getRemark());

        return sysUserInfoToken;
    }

    public static SysUserInfoToken toDb(SysUserInfoTokenReqUpdate input) {
        if (Objects.isNull(input)) {
            return null;
        }
        SysUserInfoToken sysUserInfoToken = new SysUserInfoToken();

        sysUserInfoToken.setUserId(input.getUserId());
        sysUserInfoToken.setUsername(input.getUsername());
        sysUserInfoToken.setToken(input.getToken());
        sysUserInfoToken.setClientType(input.getClientType());
        sysUserInfoToken.setIpAddress(input.getIpAddress());
        sysUserInfoToken.setUserAgent(input.getUserAgent());
        sysUserInfoToken.setExpireTime(input.getExpireTime());
        sysUserInfoToken.setStatus(input.getStatus());
        sysUserInfoToken.setRemark(input.getRemark());

        return sysUserInfoToken;
    }

    public static SysUserInfoToken toDb(SysUserInfoTokenReqSave input) {
        if (Objects.isNull(input)) {
            return null;
        }
        SysUserInfoToken sysUserInfoToken = new SysUserInfoToken();

        sysUserInfoToken.setUserId(input.getUserId());
        sysUserInfoToken.setUsername(input.getUsername());
        sysUserInfoToken.setToken(input.getToken());
        sysUserInfoToken.setClientType(input.getClientType());
        sysUserInfoToken.setIpAddress(input.getIpAddress());
        sysUserInfoToken.setUserAgent(input.getUserAgent());
        sysUserInfoToken.setExpireTime(input.getExpireTime());
        sysUserInfoToken.setStatus(input.getStatus());
        sysUserInfoToken.setRemark(input.getRemark());

        return sysUserInfoToken;
    }
}