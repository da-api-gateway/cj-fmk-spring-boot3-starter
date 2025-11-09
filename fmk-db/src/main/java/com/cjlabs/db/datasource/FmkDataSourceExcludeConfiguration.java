// 文件路径：fmk-db/src/main/java/com/cjlabs/db/datasource/FmkDataSourceExcludeConfiguration.java

package com.cjlabs.db.datasource;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.context.annotation.Configuration;

/**
 * 当启用多数据源时，通过优先级阻止默认数据源配置加载
 */
@Slf4j
@Configuration
@ConditionalOnProperty(prefix = "fmk.datasource", name = "enabled", havingValue = "true")
@AutoConfigureBefore({
    DataSourceAutoConfiguration.class,
    DataSourceTransactionManagerAutoConfiguration.class
})
public class FmkDataSourceExcludeConfiguration {
    
    public FmkDataSourceExcludeConfiguration() {
        log.info("FmkDataSourceExcludeConfiguration|多数据源已启用，将使用自定义数据源配置");
    }
}