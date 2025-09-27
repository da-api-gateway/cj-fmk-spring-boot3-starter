// TransactionHashTypeHandler.java
package com.cjlabs.db.mybatis.type;

import com.cjlabs.core.types.strings.TransactionHash;
import com.cjlabs.db.mybatis.handler.StringTypeHandler;
import org.apache.ibatis.type.MappedTypes;

/**
 * TransactionHash类型处理器
 */
@MappedTypes(TransactionHash.class)
public class TransactionHashTypeHandler extends StringTypeHandler<TransactionHash> {

    public TransactionHashTypeHandler() {
        super(TransactionHash.class, TransactionHash::of);
    }
}