// 文件路径：fmk-db/src/main/java/com/cjlabs/db/datasource/properties/FmkMultiDataSourceProperties.java
package com.cjlabs.db.datasource;

import com.zaxxer.hikari.HikariConfig;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 多数据源配置属性
 */
@Data
@Component
@ConfigurationProperties(prefix = "fmk.datasource")
public class FmkMultiDataSourceProperties {

    /**
     * 是否启用多数据源
     */
    private boolean enabled = false;

    /**
     * 默认数据源名称
     */
    private String master = "master";

    /**
     * 是否在启动时验证所有数据源连接
     */
    private boolean validateOnStartup = true;

    /**
     * 多个数据源配置
     */
    private Map<String, HikariConfig> datasources = new LinkedHashMap<>();

}