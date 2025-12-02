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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.event.ContextRefreshedEvent;

import java.time.Instant;

/**
 * 类型处理器配置
 * 🔥 使用 ApplicationListener 在容器完全启动后注册，避免循环依赖
 */
@Slf4j
@Configuration
@AutoConfiguration
@DependsOn("sqlSessionFactory")  // 🔥 确保在 SqlSessionFactory 创建后才初始化
public class TypeHandlerConfig implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired(required = false)  // 🔥 设置为非必需，进一步避免循环依赖
    private SqlSessionFactory sqlSessionFactory;

    private boolean typeHandlersRegistered = false;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        // 防止多次执行
        if (typeHandlersRegistered) {
            return;
        }

        if (sqlSessionFactory == null) {
            log.warn("TypeHandlerConfig|onApplicationEvent|SqlSessionFactory 未注入，跳过类型处理器注册");
            return;
        }

        log.info("TypeHandlerConfig|onApplicationEvent|开始注册所有自定义类型处理器");
        registerTypeHandlers();
        typeHandlersRegistered = true;
        log.info("TypeHandlerConfig|onApplicationEvent|所有类型处理器注册完成");
    }

    private void registerTypeHandlers() {
        // 🔥 注册 Instant 类型处理器（最重要！）
        sqlSessionFactory.getConfiguration()
                .getTypeHandlerRegistry()
                .register(Instant.class, InstantEpochMilliTypeHandler.class);
        log.info("  ✅ 已注册: Instant -> InstantEpochMilliTypeHandler");

        // 注册字符串类型处理器
        sqlSessionFactory.getConfiguration()
                .getTypeHandlerRegistry()
                .register(FmkTraceId.class, FmkTraceIdTypeHandler.class);
        log.info("  ✅ 已注册: FmkTraceId -> FmkTraceIdTypeHandler");

        sqlSessionFactory.getConfiguration()
                .getTypeHandlerRegistry()
                .register(FmkOrderId.class, OrderIdTypeHandler.class);
        log.info("  ✅ 已注册: FmkOrderId -> OrderIdTypeHandler");

        sqlSessionFactory.getConfiguration()
                .getTypeHandlerRegistry()
                .register(FmkCurrencyCode.class, CurrencyCodeTypeHandler.class);
        log.info("  ✅ 已注册: FmkCurrencyCode -> CurrencyCodeTypeHandler");

        sqlSessionFactory.getConfiguration()
                .getTypeHandlerRegistry()
                .register(FmkTxHash.class, TransactionHashTypeHandler.class);
        log.info("  ✅ 已注册: FmkTxHash -> TransactionHashTypeHandler");

        sqlSessionFactory.getConfiguration()
                .getTypeHandlerRegistry()
                .register(FmkWalletAddress.class, WalletAddressTypeHandler.class);
        log.info("  ✅ 已注册: FmkWalletAddress -> WalletAddressTypeHandler");

        // 注册Long类型处理器
        sqlSessionFactory.getConfiguration()
                .getTypeHandlerRegistry()
                .register(FmkUserId.class, UserIdTypeHandler.class);
        log.info("  ✅ 已注册: FmkUserId -> UserIdTypeHandler");

        // 注册BigDecimal类型处理器
        sqlSessionFactory.getConfiguration()
                .getTypeHandlerRegistry()
                .register(FmkAmount.class, AmountTypeHandler.class);
        log.info("  ✅ 已注册: FmkAmount -> AmountTypeHandler");
    }
}