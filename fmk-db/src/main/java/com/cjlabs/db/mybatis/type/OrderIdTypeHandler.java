// OrderIdTypeHandler.java
package com.cjlabs.db.mybatis.type;

import com.cjlabs.core.types.strings.FmkOrderId;
import com.cjlabs.db.mybatis.handler.StringTypeHandler;
import org.apache.ibatis.type.MappedTypes;

/**
 * OrderId类型处理器
 */
@MappedTypes(FmkOrderId.class)
public class OrderIdTypeHandler extends StringTypeHandler<FmkOrderId> {

    public OrderIdTypeHandler() {
        super(FmkOrderId.class, FmkOrderId::of);
    }
}