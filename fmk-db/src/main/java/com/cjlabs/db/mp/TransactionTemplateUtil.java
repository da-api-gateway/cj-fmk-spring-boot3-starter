package com.cjlabs.db.mp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.function.Consumer;
import java.util.function.Supplier;

@Component
public class TransactionTemplateUtil {

    @Autowired
    @Qualifier(value = "tx")
    private TransactionTemplate tx;
    @Qualifier(value = "txOnlyRead")
    private TransactionTemplate txOnlyRead;

    public TransactionTemplateUtil(TransactionTemplate tx,
                                   TransactionTemplate txOnlyRead) {
        this.tx = tx;
        this.txOnlyRead = txOnlyRead;
    }

    /**
     * 在默认事务中执行操作（PROPAGATION_REQUIRED）
     *
     * @param action 要执行的操作
     * @param <T>    返回值类型
     * @return 操作结果
     */
    public <T> T executeTx(TransactionCallback<T> action) {
        return tx.execute(action);
    }

    /**
     * 在READ_COMMITTED隔离级别的事务中执行操作
     *
     * @param action 要执行的操作
     * @param <T>    返回值类型
     * @return 操作结果
     */
    public <T> T executeOnlyRead(TransactionCallback<T> action) {
        return txOnlyRead.execute(action);
    }

    /**
     * 使用Lambda表达式简化事务操作（默认事务）
     *
     * @param supplier 提供结果的操作
     * @param <T>      返回值类型
     * @return 操作结果
     */
    public <T> T executeTx(Supplier<T> supplier) {
        return tx.execute(status -> supplier.get());
    }

    /**
     * 使用Lambda表达式简化事务操作（READ_COMMITTED隔离级别）
     *
     * @param supplier 提供结果的操作
     * @param <T>      返回值类型
     * @return 操作结果
     */
    public <T> T executeOnlyRead(Supplier<T> supplier) {
        return txOnlyRead.execute(status -> supplier.get());
    }

    /**
     * 执行无返回值的事务操作（默认事务）
     *
     * @param runnable 要执行的操作
     */
    public void executeTx(Runnable runnable) {
        tx.execute(status -> {
            runnable.run();
            return null;
        });
    }

    /**
     * 执行无返回值的事务操作（READ_COMMITTED隔离级别）
     *
     * @param runnable 要执行的操作
     */
    public void executeOnlyRead(Runnable runnable) {
        txOnlyRead.execute(status -> {
            runnable.run();
            return null;
        });
    }

    /**
     * 执行事务操作，并在发生异常时回滚
     *
     * @param supplier     提供结果的操作
     * @param <T>          返回值类型
     * @param errorHandler 异常处理器
     * @return 操作结果，如果发生异常则返回null
     */
    public <T> T executeTxAndRollbackOnException(Supplier<T> supplier,
                                                 Consumer<Exception> errorHandler) {
        try {
            return tx.execute(status -> {
                try {
                    return supplier.get();
                } catch (Exception e) {
                    status.setRollbackOnly();
                    errorHandler.accept(e);
                    return null;
                }
            });
        } catch (Exception e) {
            errorHandler.accept(e);
            return null;
        }
    }
}
