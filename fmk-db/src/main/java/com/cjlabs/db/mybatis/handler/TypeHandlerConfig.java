package com.cjlabs.db.mybatis.handler;

import com.cjlabs.core.types.decimal.Amount;
import com.cjlabs.core.types.longs.UserId;
import com.cjlabs.core.types.strings.CurrencyCode;
import com.cjlabs.core.types.strings.FmkTraceId;
import com.cjlabs.core.types.strings.OrderId;
import com.cjlabs.core.types.strings.TransactionHash;
import com.cjlabs.core.types.strings.WalletAddress;
import com.cjlabs.db.mybatis.type.AmountTypeHandler;
import com.cjlabs.db.mybatis.type.CurrencyCodeTypeHandler;
import com.cjlabs.db.mybatis.type.FmkTraceIdTypeHandler;

import com.cjlabs.db.mybatis.type.OrderIdTypeHandler;
import com.cjlabs.db.mybatis.type.TransactionHashTypeHandler;
import com.cjlabs.db.mybatis.type.UserIdTypeHandler;
import com.cjlabs.db.mybatis.type.WalletAddressTypeHandler;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

/**
 * 类型处理器配置
 */
@Configuration
public class TypeHandlerConfig implements InitializingBean {
    
    @Autowired(required = false)
    private SqlSessionFactory sqlSessionFactory;
    
    @Override
    public void afterPropertiesSet() {
        if (sqlSessionFactory != null) {
            // 注册自定义类型处理器
            registerTypeHandlers();
        }
    }
    
    private void registerTypeHandlers() {
        // 注册字符串类型处理器
        sqlSessionFactory.getConfiguration().getTypeHandlerRegistry().register(FmkTraceId.class, FmkTraceIdTypeHandler.class);
        sqlSessionFactory.getConfiguration().getTypeHandlerRegistry().register(OrderId.class, OrderIdTypeHandler.class);
        sqlSessionFactory.getConfiguration().getTypeHandlerRegistry().register(CurrencyCode.class, CurrencyCodeTypeHandler.class);
        sqlSessionFactory.getConfiguration().getTypeHandlerRegistry().register(TransactionHash.class, TransactionHashTypeHandler.class);
        sqlSessionFactory.getConfiguration().getTypeHandlerRegistry().register(WalletAddress.class, WalletAddressTypeHandler.class);

        // 注册Long类型处理器
        sqlSessionFactory.getConfiguration().getTypeHandlerRegistry().register(UserId.class, UserIdTypeHandler.class);

        // 注册BigDecimal类型处理器
        sqlSessionFactory.getConfiguration().getTypeHandlerRegistry().register(Amount.class, AmountTypeHandler.class);

    }
}