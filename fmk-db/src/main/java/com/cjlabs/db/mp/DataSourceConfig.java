package com.cjlabs.db.mp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

@Component
@Configuration
public class DataSourceConfig {

    @Bean(name = "tx")
    public TransactionTemplate txRequired(@Autowired PlatformTransactionManager transactionManager) {
        return new TransactionTemplate(transactionManager);
    }

    @Bean(name = "txOnlyRead")
    public TransactionTemplate txReadCommit(@Autowired PlatformTransactionManager transactionManager) {
        TransactionTemplate template = new TransactionTemplate(transactionManager);
        template.setIsolationLevel(TransactionDefinition.ISOLATION_READ_COMMITTED);
        return template;
    }

    @Bean
    @ConditionalOnMissingBean
    public TransactionTemplateUtil transactionSupport(TransactionTemplate tx,
                                                      TransactionTemplate txOnlyRead) {
        return new TransactionTemplateUtil(tx, txOnlyRead);
    }
}
