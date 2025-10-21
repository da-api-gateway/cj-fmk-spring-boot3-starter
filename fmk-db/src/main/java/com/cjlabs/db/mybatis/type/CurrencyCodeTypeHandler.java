// CurrencyCodeTypeHandler.java
package com.cjlabs.db.mybatis.type;

import com.cjlabs.core.types.strings.FmkCurrencyCode;
import com.cjlabs.db.mybatis.handler.StringTypeHandler;

import org.apache.ibatis.type.MappedTypes;

/**
 * CurrencyCode类型处理器
 */
@MappedTypes(FmkCurrencyCode.class)
public class CurrencyCodeTypeHandler extends StringTypeHandler<FmkCurrencyCode> {

    public CurrencyCodeTypeHandler() {
        super(FmkCurrencyCode.class, FmkCurrencyCode::of);
    }
}