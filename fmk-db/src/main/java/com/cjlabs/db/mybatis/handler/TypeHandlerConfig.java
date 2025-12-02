package com.cjlabs.db.mybatis.handler;

import com.cjlabs.core.types.decimal.FmkAmount;
import com.cjlabs.core.types.longs.FmkUserId;
import com.cjlabs.core.types.strings.FmkCurrencyCode;
import com.cjlabs.core.types.strings.FmkTraceId;
import com.cjlabs.core.types.strings.FmkOrderId;
import com.cjlabs.core.types.strings.FmkTxHash;
import com.cjlabs.core.types.strings.FmkWalletAddress;
import com.cjlabs.db.mybatis.type.*;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.time.Instant;

/**
 * 类型处理器配置
 */
@Slf4j
@Configuration
public class TypeHandlerConfig implements InitializingBean {
    
    @Autowired
    private SqlSessionFactory sqlSessionFactory;
    
    @Override
    public void afterPropertiesSet() {
        if (sqlSessionFactory != null) {
            log.info("TypeHandlerConfig|afterPropertiesSet|registerTypeHandlers");
            // 注册自定义类型处理器
            registerTypeHandlers();
        }
    }
    
    private void registerTypeHandlers() {
        // 注册字符串类型处理器
        sqlSessionFactory.getConfiguration().getTypeHandlerRegistry().register(FmkTraceId.class, FmkTraceIdTypeHandler.class);
        sqlSessionFactory.getConfiguration().getTypeHandlerRegistry().register(FmkOrderId.class, OrderIdTypeHandler.class);
        sqlSessionFactory.getConfiguration().getTypeHandlerRegistry().register(FmkCurrencyCode.class, CurrencyCodeTypeHandler.class);
        sqlSessionFactory.getConfiguration().getTypeHandlerRegistry().register(FmkTxHash.class, TransactionHashTypeHandler.class);
        sqlSessionFactory.getConfiguration().getTypeHandlerRegistry().register(FmkWalletAddress.class, WalletAddressTypeHandler.class);

        // 注册Long类型处理器
        sqlSessionFactory.getConfiguration().getTypeHandlerRegistry().register(FmkUserId.class, UserIdTypeHandler.class);
        sqlSessionFactory.getConfiguration()
                .getTypeHandlerRegistry()
                .register(Instant.class, InstantEpochMilliTypeHandler.class);

        // 注册BigDecimal类型处理器
        sqlSessionFactory.getConfiguration().getTypeHandlerRegistry().register(FmkAmount.class, AmountTypeHandler.class);

    }
}