// CurrencyCodeTypeHandler.java
package com.cjlabs.db.mybatis.type;

import com.cjlabs.core.types.strings.CurrencyCode;

import com.cjlabs.db.mybatis.handler.StringTypeHandler;
import org.apache.ibatis.type.MappedTypes;

/**
 * CurrencyCode类型处理器
 */
@MappedTypes(CurrencyCode.class)
public class CurrencyCodeTypeHandler extends StringTypeHandler<CurrencyCode> {

    public CurrencyCodeTypeHandler() {
        super(CurrencyCode.class, CurrencyCode::of);
    }
}