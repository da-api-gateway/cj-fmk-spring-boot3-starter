// 文件路径：fmk-db/src/main/java/com/cjlabs/db/datasource/FmkDsUtil.java
package com.cjlabs.db.datasource;

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
 * 1. 简单切换：FmkDsUtil.use("secondary", () -> userMapper.selectById(id));
 * 2. 事务操作：FmkDsUtil.executeTx("secondary", () -> userMapper.insert(user));
 * 3. 无返回值：FmkDsUtil.run("secondary", () -> logMapper.insert(log));
 */
@Slf4j
@Component
public class FmkTransactionTemplateUtil {

    private static TransactionTemplate tx;
    private static TransactionTemplate txOnlyRead;

    @Autowired
    public void setTx(@Qualifier("tx") TransactionTemplate tx) {
        FmkTransactionTemplateUtil.tx = tx;
    }

    @Autowired
    public void setTxOnlyRead(@Qualifier("txOnlyRead") TransactionTemplate txOnlyRead) {
        FmkTransactionTemplateUtil.txOnlyRead = txOnlyRead;
    }

    /**
     * 在指定数据源上执行操作（有返回值）
     *
     * @param dataSourceName 数据源名称
     * @param supplier       业务逻辑
     * @param <T>            返回值类型
     * @return 执行结果
     */
    public static <T> T use(String dataSourceName, Supplier<T> supplier) {
        String originalDs = DynamicDataSourceContextHolder.getDataSource();
        try {
            DynamicDataSourceContextHolder.setDataSource(dataSourceName);
            log.debug("切换到数据源: {}", dataSourceName);
            return supplier.get();
        } finally {
            DynamicDataSourceContextHolder.setDataSource(originalDs);
            log.debug("恢复数据源: {}", originalDs);
        }
    }

    /**
     * 在指定数据源上执行操作（无返回值）
     *
     * @param dataSourceName 数据源名称
     * @param runnable       业务逻辑
     */
    public static void run(String dataSourceName, Runnable runnable) {
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
    public static <T> T executeTx(String dataSourceName, Supplier<T> supplier) {
        return use(dataSourceName, () ->
                tx.execute(status -> supplier.get())
        );
    }

    /**
     * 在指定数据源上执行事务操作（无返回值）
     *
     * @param dataSourceName 数据源名称
     * @param runnable       业务逻辑
     */
    public static void executeTx(String dataSourceName, Runnable runnable) {
        use(dataSourceName, () -> {
            tx.execute(status -> {
                runnable.run();
                return null;
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
    public static <T> T executeReadOnly(String dataSourceName, Supplier<T> supplier) {
        return use(dataSourceName, () ->
                txOnlyRead.execute(status -> supplier.get())
        );
    }

    /**
     * 在指定数据源上执行只读事务操作（无返回值）
     *
     * @param dataSourceName 数据源名称
     * @param runnable       业务逻辑
     */
    public static void executeReadOnly(String dataSourceName, Runnable runnable) {
        use(dataSourceName, () -> {
            txOnlyRead.execute(status -> {
                runnable.run();
                return null;
            });
            return null;
        });
    }

}