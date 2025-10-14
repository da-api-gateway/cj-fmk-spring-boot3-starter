// TransactionHashTypeHandler.java
package com.cjlabs.db.mybatis.type;

import com.cjlabs.core.types.strings.FmkTxHash;
import com.cjlabs.db.mybatis.handler.StringTypeHandler;
import org.apache.ibatis.type.MappedTypes;

/**
 * TransactionHash类型处理器
 */
@MappedTypes(FmkTxHash.class)
public class TransactionHashTypeHandler extends StringTypeHandler<FmkTxHash> {

    public TransactionHashTypeHandler() {
        super(FmkTxHash.class, FmkTxHash::of);
    }
}