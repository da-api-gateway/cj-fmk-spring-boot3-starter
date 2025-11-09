// 文件路径：fmk-db/src/main/java/com/cjlabs/db/datasource/DynamicDataSource.java
package com.cjlabs.db.datasource;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * 动态数据源路由器
 */
@Slf4j
public class DynamicDataSource extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        String dataSource = DynamicDataSourceContextHolder.getDataSource();
        log.info("DynamicDataSource|determineCurrentLookupKey|当前使用的数据源={}", dataSource);
        return dataSource;
    }

    @Override
    public void afterPropertiesSet() {
        super.afterPropertiesSet();
        log.info("DynamicDataSource|afterPropertiesSet|动态数据源初始化完成");
    }
}