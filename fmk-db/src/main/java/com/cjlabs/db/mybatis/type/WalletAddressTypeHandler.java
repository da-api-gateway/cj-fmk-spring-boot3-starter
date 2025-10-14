// WalletAddressTypeHandler.java
package com.cjlabs.db.mybatis.type;

import com.cjlabs.core.types.strings.FmkWalletAddress;
import com.cjlabs.db.mybatis.handler.StringTypeHandler;
import org.apache.ibatis.type.MappedTypes;

/**
 * WalletAddress类型处理器
 */
@MappedTypes(FmkWalletAddress.class)
public class WalletAddressTypeHandler extends StringTypeHandler<FmkWalletAddress> {

    public WalletAddressTypeHandler() {
        super(FmkWalletAddress.class, FmkWalletAddress::of);
    }
}