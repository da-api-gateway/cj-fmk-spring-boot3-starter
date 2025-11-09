
```

# application.yml
fmk:
  datasource:
    enabled: true              # 启用多数据源
    master: master            # 默认数据源名称
    validate-on-startup: true # 启动时验证所有数据源连接
    datasources:
      # 主数据源（读写）
      master:
        jdbc-url: jdbc:mysql://localhost:3306/db_master?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai
        username: root
        password: password
        driver-class-name: com.mysql.cj.jdbc.Driver
        maximum-pool-size: 20
        minimum-idle: 5
        connection-timeout: 30000
        idle-timeout: 600000
        max-lifetime: 1800000
        pool-name: Master-Pool
        auto-commit: true
        connection-test-query: SELECT 1
      
      # 从数据源（只读）
      slave:
        jdbc-url: jdbc:mysql://slave.host:3306/db_slave?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai
        username: readonly
        password: password
        maximum-pool-size: 10
        minimum-idle: 3
        pool-name: Slave-Pool
      
      # 报表数据源
      report:
        jdbc-url: jdbc:mysql://report.host:3306/db_report?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai
        username: report_user
        password: password
        maximum-pool-size: 5
        minimum-idle: 2
        pool-name: Report-Pool
      
      # 日志数据源
      log:
        jdbc-url: jdbc:mysql://log.host:3306/db_log?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai
        username: log_user
        password: password
        maximum-pool-size: 5
        minimum-idle: 2
        pool-name: Log-Pool

```