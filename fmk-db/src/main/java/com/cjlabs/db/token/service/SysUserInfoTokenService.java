package com.cjlabs.db.token.service;

import com.cjlabs.core.time.FmkInstantUtil;
import com.cjlabs.core.types.strings.FmkToken;
import com.cjlabs.db.domain.FmkPageResponse;
import com.cjlabs.db.domain.FmkRequest;
import com.cjlabs.domain.sysuserinfotoken.enums.TokenStatusEnum;
import com.cjlabs.domain.sysuserinfotoken.reqquery.SysUserInfoTokenReqQuery;
import com.cjlabs.domain.sysuserinfotoken.requpdate.SysUserInfoTokenReqSave;
import com.cjlabs.domain.sysuserinfotoken.requpdate.SysUserInfoTokenReqUpdate;
import com.cjlabs.service.sysuserinfotoken.convert.SysUserInfoTokenReqConvert;
import com.cjlabs.service.sysuserinfotoken.mapper.SysUserInfoTokenWrapMapper;
import com.cjlabs.service.sysuserinfotoken.mysql.SysUserInfoToken;
import com.cjlabs.web.check.FmkCheckUtil;
import com.cjlabs.web.exception.BusinessExceptionEnum;
import com.cjlabs.web.threadlocal.ClientInfo;
import com.cjlabs.web.threadlocal.FmkContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * sys_user_info_token 管理员访问令牌表
 * <p>
 * 2025-12-03 04:34:19
 */
@Slf4j
@Service
public class SysUserInfoTokenService {

    @Autowired
    private SysUserInfoTokenWrapMapper sysUserInfoTokenWrapMapper;

    public SysUserInfoToken getById(FmkRequest<Void> input) {
        // 参数校验
        FmkCheckUtil.checkInput(Objects.isNull(input));
        FmkCheckUtil.checkInput(StringUtils.isBlank(input.getBusinessKey()));

        String id = input.getBusinessKey();
        return sysUserInfoTokenWrapMapper.getById(id);
    }

    public SysUserInfoToken save(SysUserInfoTokenReqSave request) {
        if (Objects.isNull(request)) {
            log.info("SysUserInfoTokenService|save|request is null");
            return null;
        }
        Optional<ClientInfo> clientInfoOptional = FmkContextUtil.getClientInfo();
        clientInfoOptional.ifPresent(clientInfo -> {
            // request.setClientType();
            request.setIpAddress(clientInfo.getIpAddress());
            request.setUserAgent(clientInfo.getUserAgent());
            request.setClientType(clientInfo.getClientType());
        });
        request.setStatus(TokenStatusEnum.ACTIVE);
        Instant expireAt = FmkInstantUtil.plus(Instant.now(), 12, ChronoUnit.HOURS);
        request.setExpireTime(expireAt);
        FmkToken fmkToken = FmkToken.generate();
        request.setToken(fmkToken);
        SysUserInfoToken sysUserInfoToken = SysUserInfoTokenReqConvert.toDb(request);

        int saved = sysUserInfoTokenWrapMapper.save(sysUserInfoToken);
        FmkCheckUtil.throwBusiness(saved == 0, BusinessExceptionEnum.DATA_NOT_FOUND);
        return sysUserInfoToken;
    }

    public boolean update(SysUserInfoTokenReqUpdate request) {
        if (Objects.isNull(request)) {
            log.info("SysUserInfoTokenService|update|request is null");
            return false;
        }

        Long inputId = request.getId();
        SysUserInfoToken sysUserInfoToken = sysUserInfoTokenWrapMapper.getById(inputId);
        if (Objects.isNull(sysUserInfoToken)) {
            log.info("SysUserInfoTokenService|update|db sysUserInfoToken is null");
            return false;
        }

        int updated = sysUserInfoTokenWrapMapper.updateById(sysUserInfoToken);
        if (updated > 0) {
            log.info("SysUserInfoTokenService|update|update={}|id={}", updated, request.getId());
            return true;
        }
        return false;
    }

    public boolean deleteById(String businessKey) {
        if (StringUtils.isBlank(businessKey)) {
            log.info("SysUserInfoTokenService|deleteById|businessKey is null");
            return false;
        }
        int deleted = sysUserInfoTokenWrapMapper.deleteById(businessKey);
        if (deleted > 0) {
            log.info("SysUserInfoTokenService|deleteById|deleteById={}|id={}", deleted, businessKey);
            return true;
        }
        return false;
    }

    /**
     * 查询所有（不分页）
     */
    public List<SysUserInfoToken> listAll() {
        List<SysUserInfoToken> entityList = sysUserInfoTokenWrapMapper.listAllLimitService();
        return entityList;
    }

    /**
     * 分页查询
     */
    public FmkPageResponse<SysUserInfoToken> pageQuery(FmkRequest<SysUserInfoTokenReqQuery> input) {
        // 参数校验
        FmkCheckUtil.checkInput(Objects.isNull(input));
        FmkCheckUtil.checkInput(Objects.isNull(input.getRequest()));

        // 执行分页查询
        FmkPageResponse<SysUserInfoToken> entityPage = sysUserInfoTokenWrapMapper.pageQuery(input);

        return entityPage;
    }
}