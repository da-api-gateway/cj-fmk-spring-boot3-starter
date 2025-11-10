// 文件路径：fmk-db/src/main/java/com/cjlabs/db/datasource/FmkDsUtil.java
package com.cjlabs.db.datasource;

import com.cjlabs.web.json.FmkJacksonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.function.Supplier;

/**
 * 多数据源工具类 - 框架核心API
 * 提供简洁的手动切换数据源方法
 * <p>
 * 使用示例：
 * 1. 简单切换：FmkTransactionTemplateUtil.use("secondary", () -> userMapper.selectById(id));
 * 2. 事务操作：FmkTransactionTemplateUtil.executeTx("secondary", () -> userMapper.insert(user));
 * 3. 无返回值：FmkTransactionTemplateUtil.run("secondary", () -> logMapper.insert(log));
 */
@Slf4j
@Component
public class FmkTransactionTemplateUtil {
    @Autowired
    @Qualifier(value = "tx")
    private TransactionTemplate tx;
    @Autowired
    @Qualifier(value = "txOnlyRead")
    private TransactionTemplate txOnlyRead;

    /**
     * 在指定数据源上执行操作（有返回值）
     *
     * @param dataSourceName 数据源名称
     * @param supplier       业务逻辑
     * @param <T>            返回值类型
     * @return 执行结果
     */
    public <T> T use(String dataSourceName, Supplier<T> supplier) {
        String originalDs = DynamicDataSourceContextHolder.getDataSource();
        try {
            DynamicDataSourceContextHolder.setDataSource(dataSourceName);
            log.debug("FmkTransactionTemplateUtil|use|切换数据源: {} -> {}", originalDs, dataSourceName);
            return supplier.get();
        } catch (Exception e) {
            log.error("FmkTransactionTemplateUtil|use|执行数据源 [{}] 操作失败", dataSourceName, e);
            throw e;
        } finally {
            DynamicDataSourceContextHolder.setDataSource(originalDs);
            log.debug("FmkTransactionTemplateUtil|use|恢复数据源: {}", originalDs);
        }
    }

    /**
     * 在指定数据源上执行操作（无返回值）
     *
     * @param dataSourceName 数据源名称
     * @param runnable       业务逻辑
     */
    public void run(String dataSourceName, Runnable runnable) {
        use(dataSourceName, () -> {
            runnable.run();
            return null;
        });
    }

    /**
     * 在指定数据源上执行事务操作（有返回值）
     *
     * @param dataSourceName 数据源名称
     * @param supplier       业务逻辑
     * @param <T>            返回值类型
     * @return 执行结果
     */
    public <T> T executeTx(String dataSourceName, Supplier<T> supplier) {
        return use(dataSourceName, () -> {
            log.debug("FmkTransactionTemplateUtil|executeTx|开始事务: {}", dataSourceName);
            return tx.execute(status -> {
                try {
                    return supplier.get();
                } catch (Exception e) {
                    log.error("FmkTransactionTemplateUtil|executeTx|事务执行失败，准备回滚", e);
                    status.setRollbackOnly();
                    throw e;
                }
            });
        });
    }

    /**
     * 在指定数据源上执行事务操作（无返回值）
     *
     * @param dataSourceName 数据源名称
     * @param runnable       业务逻辑
     */
    public void executeTx(String dataSourceName, Runnable runnable) {
        use(dataSourceName, () -> {
            log.debug("FmkTransactionTemplateUtil|executeTx|开始事务: {}", dataSourceName);
            tx.execute(status -> {
                try {
                    runnable.run();
                    return null;
                } catch (Exception e) {
                    log.error("FmkTransactionTemplateUtil|executeTx|事务执行失败，准备回滚", e);
                    status.setRollbackOnly();
                    throw e;
                }
            });
            return null;
        });
    }

    /**
     * 在指定数据源上执行只读事务操作（有返回值）
     *
     * @param dataSourceName 数据源名称
     * @param supplier       业务逻辑
     * @param <T>            返回值类型
     * @return 执行结果
     */
    public <T> T executeReadOnly(String dataSourceName, Supplier<T> supplier) {
        return use(dataSourceName, () -> {
            log.debug("FmkTransactionTemplateUtil|executeReadOnly|开始只读事务: {}", dataSourceName);
            return txOnlyRead.execute(status -> supplier.get());
        });
    }

    /**
     * 在指定数据源上执行只读事务操作（无返回值）
     *
     * @param dataSourceName 数据源名称
     * @param runnable       业务逻辑
     */
    public void executeReadOnly(String dataSourceName, Runnable runnable) {
        use(dataSourceName, () -> {
            log.debug("FmkTransactionTemplateUtil|executeReadOnly|开始只读事务: {}", dataSourceName);
            txOnlyRead.execute(status -> {
                runnable.run();
                return null;
            });
            return null;
        });
    }


    // ========== 🔥 使用当前数据源的便捷方法（新增） ==========

    /**
     * 在当前数据源上执行事务（有返回值）
     */
    public <T> T executeTx(Supplier<T> supplier) {
        String currentDs = DynamicDataSourceContextHolder.getDataSource();
        log.info("FmkTransactionTemplateUtil|executeTx|在当前数据源 [{}] 上执行事务", currentDs);
        return executeTx(currentDs, supplier);
    }

    /**
     * 在当前数据源上执行事务（无返回值）
     */
    public void executeTx(Runnable runnable) {
        String currentDs = DynamicDataSourceContextHolder.getDataSource();
        log.info("FmkTransactionTemplateUtil|executeTx|在当前数据源 [{}] 上执行事务", currentDs);
        executeTx(currentDs, runnable);
    }

    /**
     * 在当前数据源上执行只读事务（有返回值）
     */
    public <T> T executeReadOnly(Supplier<T> supplier) {
        String currentDs = DynamicDataSourceContextHolder.getDataSource();
        log.info("FmkTransactionTemplateUtil|executeReadOnly|在当前数据源 [{}] 上执行只读事务", currentDs);
        return executeReadOnly(currentDs, supplier);
    }

    /**
     * 在当前数据源上执行只读事务（无返回值）
     */
    public void executeReadOnly(Runnable runnable) {
        String currentDs = DynamicDataSourceContextHolder.getDataSource();
        log.info("FmkTransactionTemplateUtil|executeReadOnly|在当前数据源 [{}] 上执行只读事务", currentDs);
        executeReadOnly(currentDs, runnable);
    }

    /**
     * 获取当前数据源名称
     */
    public String getCurrentDataSource() {
        return DynamicDataSourceContextHolder.getDataSource();
    }
}