// 文件路径：fmk-db/src/main/java/com/cjlabs/db/datasource/FmkMultiDataSourceConfig.java
package com.cjlabs.db.datasource;

import com.cjlabs.web.json.FmkJacksonUtil;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * 多数据源配置类
 * 当 fmk.datasource.enabled=true 时启用
 */
@Slf4j
@Configuration
@ConditionalOnProperty(prefix = "fmk.datasource", name = "enabled", havingValue = "true")
public class FmkMultiDataSourcePropertiesDeal {

    @Autowired
    private FmkMultiDataSourceProperties multiDataSourceProperties;

    /**
     * 创建主数据源（动态数据源）
     */
    @Bean
    @Primary
    public DataSource dataSource() {
        Map<String, HikariConfig> datasourceMap = multiDataSourceProperties.getDatasources();
        if (MapUtils.isEmpty(datasourceMap)) {
            throw new IllegalStateException("至少需要配置一个数据源");
        }

        Map<Object, Object> targetDataSources = new HashMap<>();
        String masterName = multiDataSourceProperties.getMaster();
        DataSource defaultDataSource = null;

        // 创建所有配置的数据源
        for (Map.Entry<String, HikariConfig> entry : datasourceMap.entrySet()) {
            String name = entry.getKey();
            HikariConfig config = entry.getValue();

            try {
                log.info("FmkMultiDataSourcePropertiesDeal|dataSource|初始化数据源={}", name);
                HikariDataSource dataSource = new HikariDataSource(config);

                // 验证连接（如果启用）
                if (multiDataSourceProperties.isValidateOnStartup()) {
                    validateDataSource(ds, name);
                }

                targetDataSources.put(name, ds);

                // 设置默认数据源
                if (name.equals(masterName)) {
                    defaultDataSource = ds;
                }

                log.info("数据源 [{}] 初始化成功 [连接池: {}, 最大连接数: {}]",
                        name, config.getPoolName(), config.getMaximumPoolSize());
            } catch (Exception e) {
                log.error("数据源 [{}] 初始化失败", name, e);
                throw new IllegalStateException("数据源 [" + name + "] 初始化失败", e);
            }
        }

        if (targetDataSources.isEmpty()) {
            throw new IllegalStateException("至少需要配置一个数据源");
        }

        // 设置默认数据源
        String primaryName = multiDataSourceProperties.getMaster();
        Object defaultDataSource = targetDataSources.get(primaryName);

        if (defaultDataSource == null) {
            throw new IllegalStateException("默认数据源 [" + primaryName + "] 未配置");
        }

        // 创建动态数据源
        DynamicDataSource dynamicDataSource = new DynamicDataSource();
        dynamicDataSource.setTargetDataSources(targetDataSources);
        dynamicDataSource.setDefaultTargetDataSource(defaultDataSource);

        log.info("多数据源配置完成，共 {} 个数据源，默认: {}", targetDataSources.size(), primaryName);
        return dynamicDataSource;
    }


    /**
     * 事务管理器
     */
    @Bean
    @Primary
    public PlatformTransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    /**
     * 默认事务模板
     */
    @Bean(name = "tx")
    public TransactionTemplate txRequired(PlatformTransactionManager transactionManager) {
        return new TransactionTemplate(transactionManager);
    }

    /**
     * 只读事务模板
     */
    @Bean(name = "txOnlyRead")
    public TransactionTemplate txReadOnly(PlatformTransactionManager transactionManager) {
        TransactionTemplate template = new TransactionTemplate(transactionManager);
        template.setReadOnly(true);
        template.setIsolationLevel(TransactionDefinition.ISOLATION_READ_COMMITTED);
        return template;
    }
}