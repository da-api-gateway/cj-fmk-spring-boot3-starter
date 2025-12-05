package com.cjlabs.memory.local;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

/**
 * LocalCache 使用示例和测试
 */
public class LocalCacheTest {

    private LocalCacheFactory cacheFactory;

    @BeforeEach
    public void setUp() {
        cacheFactory = new LocalCacheFactory();
    }

    /**
     * 示例1：基本使用 - 直接创建缓存
     */
    @Test
    public void testBasicUsage() throws InterruptedException {
        // 创建一个缓存：名称为 "userCache"，最大1000条，5分钟过期
        LocalCache<String, String> userCache = new LocalCache<>("userCache", 1000, 5);

        // 1. 手动放入缓存
        userCache.put("user:1", "张三");
        userCache.put("user:2", "李四");

        // 2. 直接获取缓存
        String user1 = userCache.getIfPresent("user:1");
        System.out.println("获取用户1: " + user1); // 输出: 张三

//        TimeUnit.SECONDS.sleep(6);

        // 3. 获取不存在的key
        String user3 = userCache.getIfPresent("user:3");
        System.out.println("获取用户3: " + user3); // 输出: null

        // 断言
        assertEquals("张三", user1);
        assertNull(user3);
    }

    /**
     * 示例2：使用 Loader 自动加载数据
     */
    @Test
    public void testCacheWithLoader() {
        LocalCache<Integer, String> productCache = new LocalCache<>("productCache", 500, 10);

        // 使用 get 方法，如果缓存不存在则自动加载
        String product1 = productCache.get(1001, () -> {
            // 模拟从数据库加载数据
            System.out.println("从数据库加载商品 1001");
            return "iPhone 15 Pro";
        });

        // 第二次获取，会直接从缓存读取，不会执行 loader
        String product1Again = productCache.get(1001, () -> {
            System.out.println("这行不会执行");
            return "iPhone 15 Pro";
        });

        System.out.println("商品1: " + product1);
        System.out.println("商品1(再次获取): " + product1Again);

        assertEquals(product1, product1Again);
    }

    /**
     * 示例3：使用工厂管理多个缓存
     */
    @Test
    public void testCacheFactory() {
        // 获取用户缓存（自定义配置）
        LocalCache<String, User> userCache = cacheFactory.getCache("userCache", 1000, 30);

        // 获取商品缓存（使用默认配置：1000条，5分钟）
        LocalCache<String, Product> productCache = cacheFactory.getCache("productCache");

        // 使用用户缓存
        User user = new User("1", "张三", 25);
        userCache.put("user:1", user);

        User cachedUser = userCache.getIfPresent("user:1");
        assertNotNull(cachedUser);
        assertEquals("张三", cachedUser.getName());

        // 使用商品缓存
        Product product = new Product("p001", "MacBook Pro", 12999.0);
        productCache.put("product:p001", product);

        Product cachedProduct = productCache.getIfPresent("product:p001");
        assertNotNull(cachedProduct);
        assertEquals("MacBook Pro", cachedProduct.getName());
    }

    /**
     * 示例4：缓存删除操作
     */
    @Test
    public void testCacheInvalidation() {
        LocalCache<String, String> cache = new LocalCache<>("testCache", 100, 5);

        // 放入数据
        cache.put("key1", "value1");
        cache.put("key2", "value2");
        cache.put("key3", "value3");

        assertEquals(3, cache.size());

        // 删除单个缓存
        cache.deleteByKey("key1");
        assertNull(cache.getIfPresent("key1"));
        assertEquals(2, cache.size());

        // 清空所有缓存
        cache.deleteAll();
        assertEquals(0, cache.size());
    }

    /**
     * 示例5：查看缓存统计信息
     */
    @Test
    public void testCacheStats() {
        LocalCache<String, String> cache = new LocalCache<>("statsCache", 100, 5);

        // 模拟一些缓存操作
        cache.put("key1", "value1");
        cache.put("key2", "value2");

        // 命中
        cache.getIfPresent("key1");
        cache.getIfPresent("key2");

        // 未命中
        cache.getIfPresent("key3");
        cache.getIfPresent("key4");

        // 打印统计信息
        cache.logStats();

        // 获取统计对象
        var stats = cache.stats();
        System.out.println("命中次数: " + stats.hitCount());
        System.out.println("未命中次数: " + stats.missCount());
        System.out.println("命中率: " + (stats.hitRate() * 100) + "%");

        assertEquals(2, stats.hitCount());
        assertEquals(2, stats.missCount());
    }

    /**
     * 示例6：实际业务场景 - 用户信息缓存
     */
    @Test
    public void testRealWorldScenario() {
        LocalCache<Long, User> userCache = cacheFactory.getCache("userCache", 10000, 30);

        // 模拟获取用户信息（带数据库查询）
        User user = userCache.get(1001L, () -> {
            // 模拟数据库查询
            System.out.println("从数据库查询用户 1001");
            return queryUserFromDatabase(1001L);
        });

        System.out.println("用户信息: " + user);

        // 再次获取，直接从缓存读取
        User cachedUser = userCache.get(1001L, () -> {
            System.out.println("这行不会执行，因为缓存命中");
            return queryUserFromDatabase(1001L);
        });

        assertSame(user, cachedUser);
    }

    /**
     * 示例7：工厂统一管理所有缓存
     */
    @Test
    public void testFactoryManagement() {
        // 创建多个缓存
        LocalCache<String, String> cache1 = cacheFactory.getCache("cache1");
        LocalCache<String, String> cache2 = cacheFactory.getCache("cache2");
        LocalCache<String, String> cache3 = cacheFactory.getCache("cache3");

        // 放入数据
        cache1.put("key", "value1");
        cache2.put("key", "value2");
        cache3.put("key", "value3");

        // 打印所有缓存的统计信息
        cacheFactory.logAllStats();

        // 清空所有缓存
        cacheFactory.clearAll();

        assertEquals(0, cache1.size());
        assertEquals(0, cache2.size());
        assertEquals(0, cache3.size());
    }

    // ========== 辅助类和方法 ==========

    /**
     * 模拟从数据库查询用户
     */
    private User queryUserFromDatabase(Long userId) {
        // 模拟数据库延迟
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return new User(userId.toString(), "用户" + userId, 25);
    }

    /**
     * 用户实体类
     */
    static class User {
        private String id;
        private String name;
        private Integer age;

        public User(String id, String name, Integer age) {
            this.id = id;
            this.name = name;
            this.age = age;
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public Integer getAge() {
            return age;
        }

        @Override
        public String toString() {
            return "User{id='" + id + "', name='" + name + "', age=" + age + "}";
        }
    }

    /**
     * 商品实体类
     */
    static class Product {
        private String id;
        private String name;
        private Double price;

        public Product(String id, String name, Double price) {
            this.id = id;
            this.name = name;
            this.price = price;
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public Double getPrice() {
            return price;
        }

        @Override
        public String toString() {
            return "Product{id='" + id + "', name='" + name + "', price=" + price + "}";
        }
    }
}