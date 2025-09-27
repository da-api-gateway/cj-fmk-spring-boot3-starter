// OrderIdTypeHandler.java
package com.cjlabs.db.mybatis.type;

import com.cjlabs.core.types.strings.OrderId;
import com.cjlabs.db.mybatis.handler.StringTypeHandler;
import org.apache.ibatis.type.MappedTypes;

/**
 * OrderId类型处理器
 */
@MappedTypes(OrderId.class)
public class OrderIdTypeHandler extends StringTypeHandler<OrderId> {

    public OrderIdTypeHandler() {
        super(OrderId.class, OrderId::of);
    }
}