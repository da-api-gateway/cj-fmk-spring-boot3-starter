// WalletAddressTypeHandler.java
package com.cjlabs.db.mybatis.type;

import com.cjlabs.core.types.strings.WalletAddress;
import com.cjlabs.db.mybatis.handler.StringTypeHandler;
import org.apache.ibatis.type.MappedTypes;

/**
 * WalletAddress类型处理器
 */
@MappedTypes(WalletAddress.class)
public class WalletAddressTypeHandler extends StringTypeHandler<WalletAddress> {

    public WalletAddressTypeHandler() {
        super(WalletAddress.class, WalletAddress::of);
    }
}