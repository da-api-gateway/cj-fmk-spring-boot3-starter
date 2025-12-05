package com.cjlabs.memory.lock;

import com.cjlabs.domain.exception.Error200Exception;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

/**
 * FmkLockUtil 测试类
 * 测试所有锁相关方法的功能和并发场景
 */
@Slf4j
@DisplayName("FmkLockUtil 单机锁工具测试")
class FmkLockUtilTest {

    private static final String TEST_KEY = "test_lock_key";
    private static final String TEST_KEY_2 = "test_lock_key_2";

    @BeforeEach
    void setUp() {
        System.out.println("\n========== 开始测试 ==========");
    }

    // ==================== getReentrantLock 测试 ====================

    @Test
    @DisplayName("测试获取重入锁 - 正常情况")
    void testGetReentrantLock_Normal() {
        var lock = FmkLockUtil.getReentrantLock(TEST_KEY);
        assertNotNull(lock, "锁不应该为空");

        // 同一个 key 应该返回同一个锁实例
        var lock2 = FmkLockUtil.getReentrantLock(TEST_KEY);
        assertSame(lock, lock2, "相同 key 应该返回相同的锁实例");
        System.out.println("✅ 相同 key 返回相同的锁实例");
    }

    @Test
    @DisplayName("测试获取重入锁 - 空 key 抛出异常")
    void testGetReentrantLock_NullKey() {
        assertThrows(IllegalArgumentException.class, () -> {
            FmkLockUtil.getReentrantLock(null);
        }, "null key 应该抛出异常");

        assertThrows(IllegalArgumentException.class, () -> {
            FmkLockUtil.getReentrantLock("");
        }, "空字符串 key 应该抛出异常");

        System.out.println("✅ 空 key 正确抛出异常");
    }

    @Test
    @DisplayName("测试获取重入锁 - 不同 key 返回不同锁")
    void testGetReentrantLock_DifferentKeys() {
        var lock1 = FmkLockUtil.getReentrantLock(TEST_KEY);
        var lock2 = FmkLockUtil.getReentrantLock(TEST_KEY_2);

        assertNotSame(lock1, lock2, "不同 key 应该返回不同的锁实例");
        System.out.println("✅ 不同 key 返回不同的锁实例");
    }

    // ==================== executeLock 测试（有返回值）====================

    @Test
    @DisplayName("测试 executeLock - 正常执行有返回值")
    void testExecuteLock_WithReturn() {
        String result = FmkLockUtil.executeLock(TEST_KEY, () -> {
            return "执行成功";
        });

        assertEquals("执行成功", result);
        System.out.println("✅ executeLock 正常执行并返回结果");
    }

    @Test
    @DisplayName("测试 executeLock - 并发执行保证互斥")
    void testExecuteLock_Concurrency() throws InterruptedException {
        AtomicInteger counter = new AtomicInteger(0);
        int threadCount = 10;
        CountDownLatch latch = new CountDownLatch(threadCount);
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executor.submit(() -> {
                try {
                    FmkLockUtil.executeLock(TEST_KEY, () -> {
                        int current = counter.get();
                        Thread.sleep(5); // 模拟耗时操作
                        counter.set(current + 1);
                        return null;
                    });
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executor.shutdown();

        assertEquals(threadCount, counter.get(), "并发执行应该保证互斥");
        System.out.println("✅ 并发执行 " + threadCount + " 个线程，计数器: " + counter.get());
    }

    @Test
    @DisplayName("测试 executeLock - 异常处理")
    void testExecuteLock_Exception() {
        assertThrows(Error200Exception.class, () -> {
            FmkLockUtil.executeLock(TEST_KEY, () -> {
                throw new RuntimeException("测试异常");
            });
        }, "异常应该被转换为 Error200Exception");
        System.out.println("✅ executeLock 异常处理正确");
    }

    @Test
    @DisplayName("测试 executeLock - 重入锁特性")
    void testExecuteLock_Reentrant() {
        String result = FmkLockUtil.executeLock(TEST_KEY, () -> {
            // 在持有锁的情况下再次获取同一个锁（重入）
            return FmkLockUtil.executeLock(TEST_KEY, () -> {
                return "重入成功";
            });
        });

        assertEquals("重入成功", result);
        System.out.println("✅ ReentrantLock 支持重入");
    }

    // ==================== executeWithLock 无返回值测试 ====================

    @Test
    @DisplayName("测试 executeWithLock - 无返回值正常执行")
    void testExecuteWithLock_Void() {
        AtomicInteger counter = new AtomicInteger(0);

        FmkLockUtil.executeWithLock(TEST_KEY, () -> {
            counter.incrementAndGet();
        });

        assertEquals(1, counter.get());
        System.out.println("✅ executeWithLock(无返回值) 正常执行");
    }

    @Test
    @DisplayName("测试 executeWithLock - 无返回值并发互斥")
    void testExecuteWithLock_VoidConcurrency() throws InterruptedException {
        AtomicInteger counter = new AtomicInteger(0);
        int threadCount = 10;
        CountDownLatch latch = new CountDownLatch(threadCount);
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executor.submit(() -> {
                try {
                    FmkLockUtil.executeWithLock(TEST_KEY, () -> {
                        int current = counter.get();
                        Thread.sleep(5);
                        counter.set(current + 1);
                    });
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executor.shutdown();

        assertEquals(threadCount, counter.get());
        System.out.println("✅ executeWithLock(无返回值) 并发互斥正常");
    }

    // ==================== executeTryLock 测试 ====================

    @Test
    @DisplayName("测试 executeTryLock - 正常获取锁并执行")
    void testExecuteTryLock_Success() {
        String result = FmkLockUtil.executeTryLock(TEST_KEY, () -> {
            return "执行成功";
        });

        assertEquals("执行成功", result);
        System.out.println("✅ executeTryLock 正常获取锁并执行");
    }

    @Test
    @DisplayName("测试 executeTryLock - 超时抛出异常")
    void testExecuteTryLock_Timeout() throws InterruptedException {
        CountDownLatch lockHeldLatch = new CountDownLatch(1);

        // 线程1：持有锁 3 秒
        Thread thread1 = new Thread(() -> {
            try {
                FmkLockUtil.executeLock(TEST_KEY, () -> {
                    lockHeldLatch.countDown();
                    try {
                        Thread.sleep(3000); // 持有锁 3 秒
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    return null;
                });
            } catch (Error200Exception e) {
                log.error("Thread-1 异常: {}", e.getMessage());
            }
        });
        thread1.start();

        // 等待线程1获取到锁
        assertTrue(lockHeldLatch.await(2, TimeUnit.SECONDS), "线程1应该获取到锁");

        // 线程2：尝试获取锁，超时 500ms
        long startTime = System.currentTimeMillis();
        Error200Exception exception = assertThrows(Error200Exception.class, () -> {
            FmkLockUtil.executeTryLock(TEST_KEY, 500, TimeUnit.MILLISECONDS, () -> {
                return "不应该执行到这里";
            });
        }, "超时应该抛出异常");

        long duration = System.currentTimeMillis() - startTime;

        // 验证确实等待了约 500ms
        assertTrue(duration >= 400 && duration < 1000, 
            String.format("应该等待约 500ms，实际: %dms", duration));

        thread1.join();
        System.out.println("✅ executeTryLock 超时正常工作，等待时间: " + duration + "ms");
    }

    @Test
    @DisplayName("测试 executeTryLockWait5S - 默认 5 秒超时")
    void testExecuteTryLockWait5S() {
        String result = FmkLockUtil.executeTryLockWait5S(TEST_KEY, () -> {
            return "默认5秒超时执行成功";
        });

        assertEquals("默认5秒超时执行成功", result);
        System.out.println("✅ executeTryLockWait5S 默认 5 秒超时");
    }

    @Test
    @DisplayName("测试 executeTryLock - 无返回值")
    void testExecuteTryLock_Void() {
        AtomicInteger counter = new AtomicInteger(0);

        FmkLockUtil.executeTryLock(TEST_KEY, () -> {
            counter.incrementAndGet();
        });

        assertEquals(1, counter.get());
        System.out.println("✅ executeTryLock(无返回值) 正常执行");
    }

    @Test
    @DisplayName("测试 executeTryLock - 无返回值超时")
    void testExecuteTryLock_VoidTimeout() throws InterruptedException {
        CountDownLatch lockHeldLatch = new CountDownLatch(1);

        Thread thread1 = new Thread(() -> {
            FmkLockUtil.executeWithLock(TEST_KEY, () -> {
                lockHeldLatch.countDown();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        });
        thread1.start();

        lockHeldLatch.await();

        assertThrows(Error200Exception.class, () -> {
            FmkLockUtil.executeTryLock(TEST_KEY, 500, TimeUnit.MILLISECONDS, () -> {
                System.out.println("不应该执行到这里");
            });
        }, "超时应该抛出异常");

        thread1.join();
        System.out.println("✅ executeTryLock(无返回值) 超时正常工作");
    }

    @Test
    @DisplayName("测试 executeTryLock - 自定义超时时间")
    void testExecuteTryLock_CustomTimeout() {
        String result = FmkLockUtil.executeTryLock(
            TEST_KEY,
            10,
            TimeUnit.SECONDS,
            () -> "自定义超时成功"
        );

        assertEquals("自定义超时成功", result);
        System.out.println("✅ executeTryLock 支持自定义超时时间");
    }

    // ==================== 立即获取锁测试 ====================

    @Test
    @DisplayName("测试 executeTryLock(立即) - 成功获取")
    void testExecuteTryLock_Immediate_Success() {
        String result = FmkLockUtil.executeTryLock(TEST_KEY, () -> {
            return "立即获取成功";
        });

        assertEquals("立即获取成功", result);
        System.out.println("✅ executeTryLock(立即) 没有竞争时获取成功");
    }

    @Test
    @DisplayName("测试 executeTryLock(立即) - 立即失败")
    void testExecuteTryLock_Immediate_Fail() throws InterruptedException {
        CountDownLatch lockHeldLatch = new CountDownLatch(1);

        Thread thread1 = new Thread(() -> {
            FmkLockUtil.executeWithLock(TEST_KEY, () -> {
                lockHeldLatch.countDown();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        });
        thread1.start();

        lockHeldLatch.await();

        // 立即尝试获取锁，应该立即失败
        assertThrows(Error200Exception.class, () -> {
            FmkLockUtil.executeTryLock(TEST_KEY, () -> {
                return "不应该执行到这里";
            });
        }, "锁被占用时应该立即失败");

        thread1.join();
        System.out.println("✅ executeTryLock(立即) 锁被占用时立即失败");
    }

    @Test
    @DisplayName("测试 executeTryLock(立即) - 无返回值")
    void testExecuteTryLock_Immediate_Void() {
        AtomicInteger counter = new AtomicInteger(0);

        FmkLockUtil.executeTryLock(TEST_KEY, () -> {
            counter.incrementAndGet();
        });

        assertEquals(1, counter.get());
        System.out.println("✅ executeTryLock(立即，无返回值) 成功执行");
    }

    // ==================== getLockCount 测试 ====================

    @Test
    @DisplayName("测试 getLockCount - 获取锁数量")
    void testGetLockCount() {
        int initialCount = FmkLockUtil.getLockCount();

        FmkLockUtil.getReentrantLock("count_test_1");
        FmkLockUtil.getReentrantLock("count_test_2");
        FmkLockUtil.getReentrantLock("count_test_3");

        int newCount = FmkLockUtil.getLockCount();

        assertTrue(newCount >= initialCount + 3, 
            String.format("锁数量应该增加 3 个，初始: %d, 当前: %d", initialCount, newCount));
        System.out.println("✅ getLockCount 正常工作，当前锁数量: " + newCount);
    }

    // ==================== 综合场景测试 ====================

    @Test
    @DisplayName("综合测试 - 多线程多 key 并发")
    void testMultiThreadMultiKey() throws InterruptedException {
        int threadCount = 20;
        CountDownLatch latch = new CountDownLatch(threadCount);
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        ConcurrentHashMap<String, AtomicInteger> counters = new ConcurrentHashMap<>();

        counters.put("key1", new AtomicInteger(0));
        counters.put("key2", new AtomicInteger(0));

        for (int i = 0; i < threadCount; i++) {
            String key = i % 2 == 0 ? "key1" : "key2";
            executor.submit(() -> {
                try {
                    FmkLockUtil.executeLock(key, () -> {
                        AtomicInteger counter = counters.get(key);
                        int current = counter.get();
                        Thread.sleep(3);
                        counter.set(current + 1);
                        return null;
                    });
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executor.shutdown();

        assertEquals(10, counters.get("key1").get());
        assertEquals(10, counters.get("key2").get());
        System.out.println("✅ 多线程多 key 并发正常，key1: " + counters.get("key1") + ", key2: " + counters.get("key2"));
    }

    @Test
    @DisplayName("综合测试 - 性能测试 1000 次操作")
    void testPerformance() {
        int iterations = 1000;
        AtomicInteger counter = new AtomicInteger(0);

        long startTime = System.currentTimeMillis();

        for (int i = 0; i < iterations; i++) {
            FmkLockUtil.executeLock("perf_test", () -> {
                counter.incrementAndGet();
                return null;
            });
        }

        long duration = System.currentTimeMillis() - startTime;

        assertEquals(iterations, counter.get());
        assertTrue(duration < 5000, "性能测试应该在 5 秒内完成");
        System.out.println("✅ 性能测试: " + iterations + " 次操作耗时 " + duration + "ms");
    }

    @Test
    @DisplayName("综合测试 - 异常下锁的释放")
    void testLockReleaseOnException() {
        var lock = FmkLockUtil.getReentrantLock(TEST_KEY);

        assertFalse(lock.isHeldByCurrentThread());

        try {
            FmkLockUtil.executeLock(TEST_KEY, () -> {
                assertTrue(lock.isHeldByCurrentThread());
                throw new RuntimeException("测试异常");
            });
        } catch (Error200Exception e) {
            // 预期异常
        }

        assertFalse(lock.isHeldByCurrentThread());
        System.out.println("✅ 异常情况下锁正确释放");
    }
}