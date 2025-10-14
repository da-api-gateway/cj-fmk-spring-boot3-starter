package com.cjlabs.db.mybatis.type;

import com.cjlabs.core.types.longs.FmkUserId;
import com.cjlabs.db.mybatis.handler.LongTypeHandler;
import org.apache.ibatis.type.MappedTypes;

/**
 * UserId类型处理器
 */
@MappedTypes(FmkUserId.class)
public class UserIdTypeHandler extends LongTypeHandler<FmkUserId> {
    
    public UserIdTypeHandler() {
        super(FmkUserId::of);
    }
}