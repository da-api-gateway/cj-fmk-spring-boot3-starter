package com.cjlabs.db.mybatis.type;

import com.cjlabs.core.types.longs.UserId;
import com.cjlabs.db.mybatis.handler.LongTypeHandler;
import org.apache.ibatis.type.MappedTypes;

/**
 * UserId类型处理器
 */
@MappedTypes(UserId.class)
public class UserIdTypeHandler extends LongTypeHandler<UserId> {
    
    public UserIdTypeHandler() {
        super(UserId::of);
    }
}