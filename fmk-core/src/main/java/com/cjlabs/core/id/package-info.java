package com.cjlabs.core.id;

/**
 * 雪花算法ID生成器包（无机器ID版本）
 *
 * <h2>功能特性</h2>
 * <ul>
 *   <li>解决时钟回拨问题：支持小幅度时钟回拨的容忍和处理</li>
 *   <li>线程安全：使用ReentrantLock确保多线程环境下的安全性</li>
 *   <li>超高性能：每毫秒可生成4194304个唯一ID（约420万个）</li>
 *   <li>去掉机器ID：适用于单机环境，将机器ID位数分配给序列号</li>
 *   <li>ID解析：可以解析ID中包含的时间戳和序列号信息</li>
 *   <li>状态监控：提供详细的生成统计和状态信息</li>
 * </ul>
 *
 * <h2>使用示例</h2>
 *
 * <h3>1. 基本使用（推荐）</h3>
 * <pre>{@code
 * // 使用工具类（自动从Spring容器获取配置）
 * long id = SnowflakeUtil.nextId();
 * String idStr = SnowflakeUtil.nextIdString();
 * String prefixedId = SnowflakeUtil.nextIdString("USER_");
 *
 * // 批量生成
 * long[] ids = SnowflakeUtil.nextIds(100);
 *
 * // 解析ID
 * SnowflakeIdInfo info = SnowflakeUtil.parseId(id);
 * System.out.println(info.getDetailInfo());
 *
 * // 获取状态
 * SnowflakeStatus status = SnowflakeUtil.getStatus();
 * System.out.println(status.getSummary());
 *
 * // 获取性能信息
 * System.out.println(SnowflakeUtil.getPerformanceInfo());
 * }</pre>
 *
 * <h3>2. 注入使用</h3>
 * <pre>{@code
 * @Service
 * public class UserService {
 *     @Autowired
 *     private SnowflakeIdGenerator snowflakeIdGenerator;
 *
 *     public Long createUser() {
 *         Long userId = snowflakeIdGenerator.nextId();
 *         // 创建用户逻辑...
 *         return userId;
 *     }
 * }
 * }</pre>
 *
 * <h3>3. 自定义配置</h3>
 * <pre>{@code
 * # application.yml
 * snowflake:
 *   clock-backward-tolerance-ms: 10  # 时钟回拨容忍10ms
 * }</pre>
 *
 * <h3>4. 手动创建</h3>
 * <pre>{@code
 * // 使用默认配置
 * SnowflakeIdGenerator generator = new SnowflakeIdGenerator();
 *
 * // 指定时钟回拨容忍时间
 * SnowflakeIdGenerator generator = new SnowflakeIdGenerator(10L);
 * }</pre>
 *
 * <h2>ID结构说明</h2>
 * <pre>
 * 64位ID结构：
 * +----------+----------+----------+
 * | 1位符号位 | 41位时间戳 | 22位序列号 |
 * +----------+----------+----------+
 *
 * - 符号位：固定为0，保证ID为正数
 * - 时间戳：从起始时间(2025-07-20 00:00:00)开始的毫秒数，可用到2094年
 * - 序列号：同一毫秒内的序列号，支持每毫秒4194304个ID
 * </pre>
 *
 * <h2>时钟回拨处理</h2>
 * <p>当检测到时钟回拨时：</p>
 * <ol>
 *   <li>如果回拨时间小于等于容忍时间，等待时钟追上</li>
 *   <li>如果回拨时间超过容忍时间，抛出RuntimeException</li>
 *   <li>记录时钟回拨次数，可通过状态监控查看</li>
 * </ol>
 *
 * <h2>性能特性</h2>
 * <ul>
 *   <li>理论峰值：每秒41.94亿个ID（4194304 × 1000毫秒）</li>
 *   <li>实际性能：单机每秒可生成数百万个ID</li>
 *   <li>内存占用：极低，只有少量状态变量</li>
 *   <li>CPU占用：极低，只有简单的位运算</li>
 * </ul>
 *
 * <h2>主要变更</h2>
 * <ul>
 *   <li>起始时间：修改为2025年7月20日 00:00:00 (UTC+8)</li>
 *   <li>去掉机器ID：将10位机器ID分配给序列号，从12位扩展到22位</li>
 *   <li>序列号范围：从0-4095扩展到0-4194303</li>
 *   <li>单机性能：每毫秒从4096个ID提升到4194304个ID</li>
 * </ul>
 *
 * <h2>注意事项</h2>
 * <ul>
 *   <li>适用于单机环境，如需分布式请考虑其他方案</li>
 *   <li>避免频繁的系统时间调整</li>
 *   <li>生产环境建议配置时钟同步服务（如NTP）</li>
 *   <li>序列号位数增加，ID的数值会比传统雪花算法更大</li>
 * </ul>
 *
 * @author dcxj-team
 * @version 2.0
 * @since 2025-01-11
 */
