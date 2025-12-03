package com.cjlabs.db.mybatis.type;

import com.cjlabs.core.types.strings.FmkToken;
import com.cjlabs.db.mybatis.handler.StringTypeHandler;
import org.apache.ibatis.type.MappedTypes;

/**
 * FmkToken类型处理器
 */
@MappedTypes(FmkToken.class)
public class FmkTokenTypeHandler extends StringTypeHandler<FmkToken> {

    public FmkTokenTypeHandler() {
        super(FmkToken.class, FmkToken::of);
    }
}