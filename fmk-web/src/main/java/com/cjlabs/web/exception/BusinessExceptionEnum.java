package com.cjlabs.web.exception;

import com.cjlabs.domain.enums.IEnumStrException;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BusinessExceptionEnum implements IEnumStrException {

    // code = type
    // msg = key

   /**
     * 未授权访问
     */
    UNAUTHORIZED("BUSINESS_ERROR", "UNAUTHORIZED"),

    
    /**
     * 超出访问频率限制
     */
    RATE_LIMIT_EXCEEDED("BUSINESS_ERROR", "RATE_LIMIT_EXCEEDED"),

    ;

    private final String type;
    private final String key;
}
