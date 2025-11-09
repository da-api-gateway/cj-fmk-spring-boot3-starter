package com.cjlabs.db.datasource;

import com.alibaba.ttl.TransmittableThreadLocal;
import lombok.extern.slf4j.Slf4j;

/**
 * 数据源上下文持有者 - 用于管理当前线程使用的数据源
 * 这是手动切换数据源的核心工具
 */
@Slf4j
public class DynamicDataSourceContextHolder {

    /**
     * 默认数据源名称
     */
    public static final String DEFAULT_DATASOURCE = "master";

    /**
     * 使用ThreadLocal存储当前线程的数据源标识
     */
    private static final ThreadLocal<String> CONTEXT_HOLDER = new TransmittableThreadLocal<>();

    /**
     * 设置当前线程的数据源
     *
     * @param dataSource 数据源名称
     */
    public static void setDataSource(String dataSource) {
        if (dataSource == null || dataSource.trim().isEmpty()) {
            log.warn("DynamicDataSourceContextHolder|setDataSource|数据源名称为空，将使用默认数据源");
            CONTEXT_HOLDER.set(DEFAULT_DATASOURCE);
            return;
        }

        // 验证数据源是否存在（可选，通过配置启用严格模式）
        if (FmkMultiDataSourcePropertiesDeal.isStrictMode()
                && !FmkMultiDataSourcePropertiesDeal.isDataSourceAvailable(dataSource)) {
            log.error("DynamicDataSourceContextHolder|setDataSource|数据源 [{}] 不存在，可用数据源: {}",
                    dataSource, FmkMultiDataSourcePropertiesDeal.getAvailableDataSources());
            throw new IllegalArgumentException("数据源 [" + dataSource + "] 不存在");
        }

        log.debug("DynamicDataSourceContextHolder|setDataSource|切换数据源到={}", dataSource);
        CONTEXT_HOLDER.set(dataSource);
    }

    /**
     * 获取当前线程的数据源
     *
     * @return 数据源名称
     */
    public static String getDataSource() {
        String dataSource = CONTEXT_HOLDER.get();
        return dataSource != null ? dataSource : DEFAULT_DATASOURCE;
    }

    /**
     * 清除当前线程的数据源
     */
    public static void clearDataSource() {
        log.debug("DynamicDataSourceContextHolder|clearDataSource|清除数据源");
        CONTEXT_HOLDER.remove();
    }

    /**
     * 重置为默认数据源
     */
    public static void resetToDefault() {
        log.debug("DynamicDataSourceContextHolder|resetToDefault|重置为默认数据源");
        CONTEXT_HOLDER.set(DEFAULT_DATASOURCE);
    }
}