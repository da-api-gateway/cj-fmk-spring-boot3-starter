// 文件路径：fmk-db/src/main/java/com/cjlabs/db/datasource/FmkMultiDataSourceConfig.java
package com.cjlabs.db.datasource;

import com.google.common.collect.Sets;
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
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

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
     * 存储所有可用的数据源名称
     */
    private static final Set<String> availableDataSources = new HashSet<>();

    /**
     * 严格模式标志
     */
    private static boolean strictMode = false;

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

        // 设置严格模式
        strictMode = multiDataSourceProperties.isStrictMode();
        log.info("FmkMultiDataSourcePropertiesDeal|dataSource|严格模式: {}", strictMode);

        for (Map.Entry<String, HikariConfig> entry : datasourceMap.entrySet()) {
            String name = entry.getKey();
            HikariConfig config = entry.getValue();

            try {
                log.info("FmkMultiDataSourcePropertiesDeal|dataSource|初始化数据源={}", name);
                HikariDataSource dataSource = new HikariDataSource(config);

                if (multiDataSourceProperties.isValidateOnStartup()) {
                    validateDataSource(dataSource, name);
                }

                targetDataSources.put(name, dataSource);
                availableDataSources.add(name);

                if (name.equals(masterName)) {
                    defaultDataSource = dataSource;
                }

                log.info("FmkMultiDataSourcePropertiesDeal|dataSource|数据源 [{}] 初始化成功 [连接池: {}, 最大连接数: {}]",
                        name, config.getPoolName(), config.getMaximumPoolSize());
            } catch (Exception e) {
                log.error("FmkMultiDataSourcePropertiesDeal|dataSource|数据源 [{}] 初始化失败", name, e);
                throw new IllegalStateException("数据源 [" + name + "] 初始化失败", e);
            }
        }

        if (defaultDataSource == null) {
            log.error("FmkMultiDataSourcePropertiesDeal|dataSource|默认数据源 [{}] 未配置", masterName);
            throw new IllegalStateException("默认数据源 [" + masterName + "] 未配置");
        }

        DynamicDataSource dynamicDataSource = new DynamicDataSource();
        dynamicDataSource.setTargetDataSources(targetDataSources);
        dynamicDataSource.setDefaultTargetDataSource(defaultDataSource);

        log.info("FmkMultiDataSourcePropertiesDeal|dataSource|多数据源配置完成，共 {} 个数据源，默认: {}, 可用数据源: {}",
                targetDataSources.size(), masterName, availableDataSources);
        return dynamicDataSource;
    }

    /**
     * 验证数据源连接
     */
    private void validateDataSource(DataSource dataSource, String name) {
        try (Connection connection = dataSource.getConnection()) {
            if (connection == null || !connection.isValid(3)) {
                log.error("FmkMultiDataSourcePropertiesDeal|validateDataSource|数据源 [{}] 连接验证失败", name);
                throw new IllegalStateException("数据源 [" + name + "] 验证失败");
            }
            log.info("FmkMultiDataSourcePropertiesDeal|validateDataSource|数据源 [{}] 连接验证成功", name);
        } catch (SQLException e) {
            log.error("FmkMultiDataSourcePropertiesDeal|validateDataSource|数据源 [{}] 验证异常: {}",
                    name, e.getMessage(), e);
            throw new IllegalStateException("数据源 [" + name + "] 验证失败", e);
        }
    }

    /**
     * 事务管理器
     */
    @Bean
    @Primary
    public PlatformTransactionManager transactionManager(DataSource dataSource) {
        log.info("FmkMultiDataSourcePropertiesDeal|transactionManager|初始化事务管理器");
        return new DataSourceTransactionManager(dataSource);
    }

    /**
     * 默认事务模板
     */
    @Bean(name = "tx")
    public TransactionTemplate txRequired(PlatformTransactionManager transactionManager) {
        log.info("FmkMultiDataSourcePropertiesDeal|txRequired|初始化默认事务模板");
        return new TransactionTemplate(transactionManager);
    }

    /**
     * 只读事务模板
     */
    @Bean(name = "txOnlyRead")
    public TransactionTemplate txReadOnly(PlatformTransactionManager transactionManager) {
        log.info("FmkMultiDataSourcePropertiesDeal|txReadOnly|初始化只读事务模板");
        TransactionTemplate template = new TransactionTemplate(transactionManager);
        template.setReadOnly(true);
        template.setIsolationLevel(TransactionDefinition.ISOLATION_READ_COMMITTED);
        return template;
    }

    /**
     * 检查数据源是否可用
     *
     * @param dataSourceName 数据源名称
     * @return 是否可用
     */
    public static boolean isDataSourceAvailable(String dataSourceName) {
        return availableDataSources.contains(dataSourceName);
    }

    /**
     * 获取所有可用的数据源名称
     *
     * @return 数据源名称集合
     */
    public static Set<String> getAvailableDataSources() {
        return Sets.newConcurrentHashSet(availableDataSources);
    }

    /**
     * 是否为严格模式
     *
     * @return 严格模式标志
     */
    public static boolean isStrictMode() {
        return strictMode;
    }
}