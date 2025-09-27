package com.cjlabs.db.mybatis.type;

import com.cjlabs.core.types.strings.FmkTraceId;
import com.cjlabs.db.mybatis.handler.StringTypeHandler;
import org.apache.ibatis.type.MappedTypes;

/**
 * FmkTraceId类型处理器
 */
@MappedTypes(FmkTraceId.class)
public class FmkTraceIdTypeHandler extends StringTypeHandler<FmkTraceId> {

    public FmkTraceIdTypeHandler() {
        super(FmkTraceId.class, FmkTraceId::of);
    }

}