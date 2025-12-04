package com.cjlabs.web.token.bo;

import com.cjlabs.core.types.longs.FmkUserId;
import com.cjlabs.core.types.strings.FmkToken;
import com.cjlabs.domain.enums.ClientTypeEnum;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

/**
 * Token 信息 DTO
 */
@Data
public class FmkTokenInfo {
    /**
     * Token 值
     */
    private FmkToken token;

    /**
     * 用户ID
     */
    private FmkUserId userId;

    /**
     * 过期时间
     */
    private Instant expireTime;

    /**
     * 备注
     */
    private FmkClientInfo clientInfo;
}