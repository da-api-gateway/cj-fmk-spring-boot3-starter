# 多数据源配置与使用文档

## 📋 配置说明

### application.yml 配置示例

```

fmk:
  datasource:
    enabled: true                    # 是否启用多数据源（默认：false）
    master: master                   # 默认数据源名称（默认：master）
    validate-on-startup: true        # 启动时验证所有数据源连接（默认：true）
    strict-mode: false               # 严格模式：切换到不存在的数据源时抛异常（默认：false，使用master）
    datasources:
      # 主数据源（读写）
      master:
        jdbc-url: jdbc:mysql://localhost:3306/db_master?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai
        username: root
        password: password
        driver-class-name: com.mysql.cj.jdbc.Driver
        maximum-pool-size: 20          # 最大连接池大小
        minimum-idle: 5                # 最小空闲连接数
        connection-timeout: 30000      # 连接超时时间（毫秒）
        idle-timeout: 600000           # 空闲超时时间（毫秒）10分钟
        max-lifetime: 1800000          # 连接最大生命周期（毫秒）30分钟
        pool-name: Master-Pool         # 连接池名称
        auto-commit: true              # 是否自动提交
        connection-test-query: SELECT 1 # 连接测试查询
        leak-detection-threshold: 30000 # 连接泄漏检测阈值（毫秒）
        validation-timeout: 5000        # 验证超时时间（毫秒）
      
      # 从数据源（只读）
      slave:
        jdbc-url: jdbc:mysql://slave.host:3306/db_slave?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai
        username: readonly
        password: password
        driver-class-name: com.mysql.cj.jdbc.Driver
        maximum-pool-size: 10
        minimum-idle: 3
        pool-name: Slave-Pool
        connection-timeout: 30000
      
      # 报表数据源
      report:
        jdbc-url: jdbc:mysql://report.host:3306/db_report?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai
        username: report_user
        password: password
        driver-class-name: com.mysql.cj.jdbc.Driver
        maximum-pool-size: 5
        minimum-idle: 2
        pool-name: Report-Pool
      
      # 日志数据源
      log:
        jdbc-url: jdbc:mysql://log.host:3306/db_log?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai
        username: log_user
        password: password
        driver-class-name: com.mysql.cj.jdbc.Driver
        maximum-pool-size: 5
        minimum-idle: 2
        pool-name: Log-Pool## 🔧 配置参数说明

```

### 全局配置

| 参数                    | 类型      | 默认值    | 说明                 |
|-----------------------|---------|--------|--------------------|
| `enabled`             | boolean | false  | 是否启用多数据源功能         |
| `master`              | String  | master | 默认数据源名称            |
| `validate-on-startup` | boolean | true   | 启动时验证所有数据源连接       |
| `strict-mode`         | boolean | false  | 严格模式：切换不存在的数据源时抛异常 |

### HikariCP 连接池配置（每个数据源）

| 参数                         | 类型      | 说明                              |
|----------------------------|---------|---------------------------------|
| `jdbc-url`                 | String  | 数据库连接URL（必填）                    |
| `username`                 | String  | 数据库用户名（必填）                      |
| `password`                 | String  | 数据库密码（必填）                       |
| `driver-class-name`        | String  | JDBC驱动类名                        |
| `maximum-pool-size`        | int     | 最大连接池大小（默认10）                   |
| `minimum-idle`             | int     | 最小空闲连接数（默认与maximum-pool-size相同） |
| `connection-timeout`       | long    | 连接超时时间（毫秒，默认30000）              |
| `idle-timeout`             | long    | 空闲超时时间（毫秒，默认600000）             |
| `max-lifetime`             | long    | 连接最大生命周期（毫秒，默认1800000）          |
| `pool-name`                | String  | 连接池名称                           |
| `auto-commit`              | boolean | 是否自动提交（默认true）                  |
| `connection-test-query`    | String  | 连接测试查询语句                        |
| `leak-detection-threshold` | long    | 连接泄漏检测阈值（毫秒，0表示禁用）              |
| `validation-timeout`       | long    | 验证超时时间（毫秒，默认5000）               |

## 💡 使用示例

### 方式1：使用工具类（推荐）

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;
    
    // 1. 简单查询（有返回值）
    public User getUserById(Long id) {
        return FmkTransactionTemplateUtil.use("slave", 
            () -> userMapper.selectById(id));
    }
    
    // 2. 简单操作（无返回值）
    public void logOperation(String operation) {
        FmkTransactionTemplateUtil.run("log", 
            () -> logMapper.insert(new LogEntity(operation)));
    }
    
    // 3. 事务操作（有返回值）
    public User saveUser(User user) {
        return FmkTransactionTemplateUtil.executeTx("master", () -> {
            userMapper.insert(user);
            return user;
        });
    }
    
    // 4. 事务操作（无返回值）
    public void batchSaveUsers(List<User> users) {
        FmkTransactionTemplateUtil.executeTx("master", 
            () -> userMapper.batchInsert(users));
    }
    
    // 5. 只读事务（查询统计数据）
    public UserStatistics getStatistics() {
        return FmkTransactionTemplateUtil.executeReadOnly("report", 
            () -> userMapper.selectStatistics());
    }
    
    // 6. 只读事务（无返回值）
    public void printUserCount() {
        FmkTransactionTemplateUtil.executeReadOnly("slave", () -> {
            Long count = userMapper.selectCount(null);
            System.out.println("用户总数: " + count);
        });
    }

}### 方式2：手动切换（最灵活）

@Service
public class OrderService {

    @Autowired
    private OrderMapper orderMapper;
    
    @Autowired
    private LogMapper logMapper;
    
    // 复杂业务场景：多次切换数据源
    public void complexOperation(Order order) {
        String original = DynamicDataSourceContextHolder.getDataSource();
        try {
            // 1. 从库查询订单信息
            DynamicDataSourceContextHolder.setDataSource("slave");
            Order existingOrder = orderMapper.selectById(order.getId());
            
            // 2. 主库更新订单
            DynamicDataSourceContextHolder.setDataSource("master");
            orderMapper.updateById(order);
            
            // 3. 日志库记录操作日志
            DynamicDataSourceContextHolder.setDataSource("log");
            logMapper.insert(new LogEntity("更新订单: " + order.getId()));
            
        } finally {
            // 恢复原数据源
            DynamicDataSourceContextHolder.setDataSource(original);
        }
    }
    
    // 或使用resetToDefault恢复默认数据源
    public void simpleOperation() {
        try {
            DynamicDataSourceContextHolder.setDataSource("slave");
            // 执行查询
            orderMapper.selectList(null);
        } finally {
            DynamicDataSourceContextHolder.resetToDefault();
        }
    }

}### 方式3：与 @Transactional 配合使用

@Service
public class ProductService {

    @Autowired
    private ProductMapper productMapper;
    
    // 先切换数据源，再开启事务
    @Transactional(rollbackFor = Exception.class)
    public void saveProduct(Product product) {
        String original = DynamicDataSourceContextHolder.getDataSource();
        try {
            // 切换到主库
            DynamicDataSourceContextHolder.setDataSource("master");
            productMapper.insert(product);
            
            // 模拟异常，会触发事务回滚
            if (product.getPrice().compareTo(BigDecimal.ZERO) < 0) {
                throw new RuntimeException("价格不能为负数");
            }
        } finally {
            DynamicDataSourceContextHolder.setDataSource(original);
        }
    }

}## 🎯 最佳实践

### 1. 读写分离

// 查询用从库
public List<User> listUsers() {
return FmkTransactionTemplateUtil.use("slave",
() -> userMapper.selectList(null));
}

// 写入用主库
public void createUser(User user) {
FmkTransactionTemplateUtil.executeTx("master",
() -> userMapper.insert(user));
}### 2. 报表查询隔离
// 将耗时的报表查询放到专用报表库
public ReportData generateReport(Date startDate, Date endDate) {
return FmkTransactionTemplateUtil.executeReadOnly("report",
() -> reportMapper.generateReport(startDate, endDate));
}### 3. 日志数据隔离
// 将操作日志写入专用日志库
public void recordOperation(String userId, String operation) {
FmkTransactionTemplateUtil.run("log",
() -> operationLogMapper.insert(new OperationLog(userId, operation)));
}### 4. 严格模式使用

# 开发环境：开启严格模式，及时发现数据源配置错误

fmk:
datasource:
strict-mode: true

# 生产环境：关闭严格模式，提高容错性

fmk:
datasource:
strict-mode: false## ⚠️ 注意事项

1. **数据源切换时机**
    - 数据源切换必须在获取数据库连接之前完成
    - 在事务开启前切换数据源
    - 使用 `@Transactional` 时，数据源切换要在方法内部进行

2. **线程安全**
    - 使用 `TransmittableThreadLocal` 保证线程安全
    - 支持异步任务和线程池场景

3. **异常处理**
    - 务必在 `finally` 块中恢复原数据源
    - 或使用工具类方法自动处理

4. **性能考虑**
    - 频繁切换数据源有一定性能开销
    - 建议在方法级别切换，而非SQL级别

5. **事务传播**
    - 多数据源切换会导致分布式事务问题
    - 谨慎处理跨数据源的事务场景

## 🔍 调试日志

### 开启 DEBUG 日志查看数据源切换

logging:
level:
com.cjlabs.db.datasource: DEBUG### 日志输出示例