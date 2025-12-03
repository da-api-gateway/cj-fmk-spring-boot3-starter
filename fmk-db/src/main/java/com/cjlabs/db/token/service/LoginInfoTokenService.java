package com.cjlabs.db.token.service;

import com.cjlabs.core.time.FmkInstantUtil;
import com.cjlabs.core.types.strings.FmkToken;
import com.cjlabs.db.domain.FmkPageResponse;
import com.cjlabs.db.domain.FmkRequest;
import com.cjlabs.db.token.enums.TokenStatusEnum;
import com.cjlabs.db.token.mapper.LoginInfoTokenWrapMapper;
import com.cjlabs.db.token.mysql.LoginInfoToken;
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
public class LoginInfoTokenService {

    @Autowired
    private LoginInfoTokenWrapMapper loginInfoTokenWrapMapper;

    public LoginInfoToken getById(FmkRequest<Void> input) {
        // 参数校验
        FmkCheckUtil.checkInput(Objects.isNull(input));
        FmkCheckUtil.checkInput(StringUtils.isBlank(input.getBusinessKey()));

        String id = input.getBusinessKey();
        return loginInfoTokenWrapMapper.getById(id);
    }

    public LoginInfoToken save(LoginInfoToken request) {
        if (Objects.isNull(request)) {
            log.info("LoginInfoTokenService|save|request is null");
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

        int saved = loginInfoTokenWrapMapper.save(request);
        FmkCheckUtil.throwBusiness(saved == 0, BusinessExceptionEnum.DATA_NOT_FOUND);
        return request;
    }

    public boolean update(LoginInfoToken request) {
        if (Objects.isNull(request)) {
            log.info("LoginInfoTokenService|update|request is null");
            return false;
        }

        Long inputId = request.getId();
        LoginInfoToken dbLoginInfoToken = loginInfoTokenWrapMapper.getById(inputId);
        if (Objects.isNull(dbLoginInfoToken)) {
            log.info("LoginInfoTokenService|update|dbLoginInfoToken is null");
            return false;
        }

        int updated = loginInfoTokenWrapMapper.updateById(dbLoginInfoToken);
        if (updated > 0) {
            log.info("LoginInfoTokenService|update|update={}|id={}", updated, request.getId());
            return true;
        }
        return false;
    }

    public boolean deleteById(String businessKey) {
        if (StringUtils.isBlank(businessKey)) {
            log.info("LoginInfoTokenService|deleteById|businessKey is null");
            return false;
        }
        int deleted = loginInfoTokenWrapMapper.deleteById(businessKey);
        if (deleted > 0) {
            log.info("LoginInfoTokenService|deleteById|deleteById={}|id={}", deleted, businessKey);
            return true;
        }
        return false;
    }

    /**
     * 查询所有（不分页）
     */
    public List<LoginInfoToken> listAll() {
        List<LoginInfoToken> entityList = loginInfoTokenWrapMapper.listAllLimitService();
        return entityList;
    }

    /**
     * 分页查询
     */
    public FmkPageResponse<LoginInfoToken> pageQuery(FmkRequest<LoginInfoToken> input) {
        // 参数校验
        FmkCheckUtil.checkInput(Objects.isNull(input));
        FmkCheckUtil.checkInput(Objects.isNull(input.getRequest()));

        // 执行分页查询
        FmkPageResponse<LoginInfoToken> entityPage = loginInfoTokenWrapMapper.pageQuery(input);

        return entityPage;
    }
}