package com.cjlabs.core.id;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("FmkSnowflakeIdGenerator Tests")
class FmkSnowflakeIdGeneratorTest {

    private FmkSnowflakeIdGenerator idGenerator;
    private final long START_TIMESTAMP = 1721433600000L; // 2025-07-20 00:00:00 UTC+0

    @BeforeEach
    void setUp() {
        idGenerator = new FmkSnowflakeIdGenerator();
    }

    @Nested
    @DisplayName("Basic ID Generation Tests")
    class BasicIdGenerationTests {

        @Test
        @DisplayName("nextId should generate unique IDs")
        void nextIdShouldGenerateUniqueIds() {
            // 生成多个ID并检查唯一性
            int count = 1000;
            Set<Long> idSet = new HashSet<>(count);

            for (int i = 0; i < count; i++) {
                long id = idGenerator.nextId();
                assertTrue(id > 0, "ID应该是正数");
                assertTrue(idSet.add(id), "ID应该是唯一的: " + id);
            }

            assertEquals(count, idSet.size(), "应该生成" + count + "个唯一ID");
        }

        @Test
        @DisplayName("nextIds should generate multiple unique IDs")
        void nextIdsShouldGenerateMultipleUniqueIds() {
            int count = 1000;
            long[] ids = idGenerator.nextIds(count);

            assertEquals(count, ids.length, "应该生成指定数量的ID");

            Set<Long> idSet = new HashSet<>(count);
            for (long id : ids) {
                assertTrue(id > 0, "ID应该是正数");
                assertTrue(idSet.add(id), "ID应该是唯一的");
            }

            assertEquals(count, idSet.size(), "应该生成" + count + "个唯一ID");
        }

        @Test
        @DisplayName("nextIds should throw exception when count is not positive")
        void nextIdsShouldThrowExceptionWhenCountIsNotPositive() {
            assertThrows(IllegalArgumentException.class, () -> idGenerator.nextIds(0));
            assertThrows(IllegalArgumentException.class, () -> idGenerator.nextIds(-1));
        }
    }

    @Nested
    @DisplayName("ID Structure Tests")
    class IdStructureTests {

        @Test
        @DisplayName("parseId should correctly parse generated ID")
        void parseIdShouldCorrectlyParseGeneratedId() {
            long id = idGenerator.nextId();
            SnowflakeIdInfo info = idGenerator.parseId(id);

            assertEquals(id, info.getId(), "解析的ID应该与原始ID相同");
            assertTrue(info.getTimestamp() >= START_TIMESTAMP, "时间戳应该大于或等于起始时间戳");
            assertTrue(info.getTimestamp() <= System.currentTimeMillis(), "时间戳应该小于或等于当前时间");
            assertTrue(info.getSequence() >= 0, "序列号应该大于或等于0");
            assertTrue(info.getSequence() <= 4194303, "序列号应该小于或等于4194303 (2^22-1)");
            assertNotNull(info.getFormattedTime(), "格式化的时间戳不应为空");
        }

        @Test
        @DisplayName("isValidId should validate IDs correctly")
        void isValidIdShouldValidateIdsCorrectly() {
            long validId = idGenerator.nextId();
            assertTrue(idGenerator.isValidId(validId), "生成的ID应该是有效的");

            // 无效ID - 负数
            assertFalse(idGenerator.isValidId(-1), "负数ID应该是无效的");

            // 无效ID - 时间戳过早
            long invalidId = 0L; // 时间戳部分为0，早于START_TIMESTAMP
            assertFalse(idGenerator.isValidId(invalidId), "时间戳过早的ID应该是无效的");

            // 无效ID - 时间戳过晚
            try (MockedStatic<System> mockedSystem = Mockito.mockStatic(System.class)) {
                mockedSystem.when(System::currentTimeMillis).thenReturn(START_TIMESTAMP);

                // 创建一个时间戳在当前时间之后的ID
                long futureId = (((START_TIMESTAMP - START_TIMESTAMP + 1000) << 22)); // 时间戳比当前时间晚1秒
                assertFalse(idGenerator.isValidId(futureId), "时间戳在未来的ID应该是无效的");
            }
        }
    }

    @Nested
    @DisplayName("Clock Backward Tests")
    class ClockBackwardTests {

        @Test
        @DisplayName("should handle clock backward within tolerance")
        void shouldHandleClockBackwardWithinTolerance() throws Exception {
            // 创建一个有5ms时钟回拨容忍度的生成器
            FmkSnowflakeIdGenerator generator = new FmkSnowflakeIdGenerator(5);

            // 使用反射设置lastTimestamp
            Field lastTimestampField = FmkSnowflakeIdGenerator.class.getDeclaredField("lastTimestamp");
            lastTimestampField.setAccessible(true);

            long currentTime = System.currentTimeMillis();
            lastTimestampField.set(generator, currentTime);

            // 模拟时钟回拨3ms (在容忍范围内)
            try (MockedStatic<System> mockedSystem = Mockito.mockStatic(System.class)) {
                mockedSystem.when(System::currentTimeMillis)
                        .thenReturn(currentTime - 3) // 第一次调用，时钟回拨3ms
                        .thenReturn(currentTime + 1); // 第二次调用，时钟恢复正常

                // 应该成功生成ID，不抛出异常
                assertDoesNotThrow(FmkSnowflakeIdGenerator::nextId);

                // 验证状态
                SnowflakeStatus status = generator.getStatus();
                assertEquals(1, status.getClockBackwardCount(), "时钟回拨计数应该增加");
            }
        }

        @Test
        @DisplayName("should throw exception when clock backward exceeds tolerance")
        void shouldThrowExceptionWhenClockBackwardExceedsTolerance() throws Exception {
            // 创建一个有5ms时钟回拨容忍度的生成器
            FmkSnowflakeIdGenerator generator = new FmkSnowflakeIdGenerator(5);

            // 使用反射设置lastTimestamp
            Field lastTimestampField = FmkSnowflakeIdGenerator.class.getDeclaredField("lastTimestamp");
            lastTimestampField.setAccessible(true);

            long currentTime = System.currentTimeMillis();
            lastTimestampField.set(generator, currentTime);

            // 模拟时钟回拨10ms (超出容忍范围)
            try (MockedStatic<System> mockedSystem = Mockito.mockStatic(System.class)) {
                mockedSystem.when(System::currentTimeMillis).thenReturn(currentTime - 10);

                // 应该抛出异常
                RuntimeException exception = assertThrows(RuntimeException.class, FmkSnowflakeIdGenerator::nextId);
                assertTrue(exception.getMessage().contains("时钟回拨超过容忍时间"), "异常消息应该提及时钟回拨");
            }
        }
    }

    @Nested
    @DisplayName("Sequence Overflow Tests")
    class SequenceOverflowTests {

        @Test
        @DisplayName("should wait for next millisecond when sequence overflows")
        void shouldWaitForNextMillisecondWhenSequenceOverflows() throws Exception {
            // 使用反射设置sequence和lastTimestamp
            Field sequenceField = FmkSnowflakeIdGenerator.class.getDeclaredField("sequence");
            sequenceField.setAccessible(true);

            Field lastTimestampField = FmkSnowflakeIdGenerator.class.getDeclaredField("lastTimestamp");
            lastTimestampField.setAccessible(true);

            long currentTime = System.currentTimeMillis();
            lastTimestampField.set(idGenerator, currentTime);
            sequenceField.set(idGenerator, 4194303L); // MAX_SEQUENCE

            // 模拟时间流逝
            try (MockedStatic<System> mockedSystem = Mockito.mockStatic(System.class)) {
                mockedSystem.when(System::currentTimeMillis)
                        .thenReturn(currentTime) // 第一次调用，与lastTimestamp相同
                        .thenReturn(currentTime + 1); // 第二次调用，时间前进1ms

                // 生成ID，应该等待下一毫秒
                long id = idGenerator.nextId();

                // 验证ID结构
                SnowflakeIdInfo info = idGenerator.parseId(id);
                assertEquals(currentTime + 1, info.getTimestamp(), "时间戳应该前进到下一毫秒");
                assertEquals(0, info.getSequence(), "序列号应该重置为0");

                // 验证状态
                SnowflakeStatus status = idGenerator.getStatus();
                assertEquals(1, status.getWaitCount(), "等待计数应该增加");
            }
        }
    }

    @Nested
    @DisplayName("Concurrency Tests")
    class ConcurrencyTests {

        @Test
        @DisplayName("should generate unique IDs in multi-threaded environment")
        void shouldGenerateUniqueIdsInMultiThreadedEnvironment() throws InterruptedException {
            int threadCount = 10;
            int idsPerThread = 1000;
            int totalIds = threadCount * idsPerThread;

            Set<Long> idSet = new HashSet<>(totalIds);
            CountDownLatch latch = new CountDownLatch(threadCount);
            AtomicBoolean duplicateFound = new AtomicBoolean(false);

            ExecutorService executor = Executors.newFixedThreadPool(threadCount);

            for (int i = 0; i < threadCount; i++) {
                executor.submit(() -> {
                    try {
                        for (int j = 0; j < idsPerThread; j++) {
                            long id = idGenerator.nextId();
                            synchronized (idSet) {
                                if (!idSet.add(id)) {
                                    duplicateFound.set(true);
                                }
                            }
                        }
                    } finally {
                        latch.countDown();
                    }
                });
            }

            assertTrue(latch.await(10, TimeUnit.SECONDS), "所有线程应该在超时前完成");
            executor.shutdown();

            assertFalse(duplicateFound.get(), "不应该生成重复的ID");
            assertEquals(totalIds, idSet.size(), "应该生成" + totalIds + "个唯一ID");
        }
    }

    @Nested
    @DisplayName("Status and Statistics Tests")
    class StatusAndStatisticsTests {

        @Test
        @DisplayName("getStatus should return correct status")
        void getStatusShouldReturnCorrectStatus() {
            // 生成一些ID
            for (int i = 0; i < 10; i++) {
                idGenerator.nextId();
            }

            SnowflakeStatus status = idGenerator.getStatus();

            assertEquals(10, status.getTotalGenerated(), "总生成计数应该正确");
            assertTrue(status.getLastTimestamp() > 0, "最后时间戳应该大于0");
            assertNotNull(status.getLastGeneratedTime(), "格式化的最后时间戳不应为空");
            assertTrue(status.getCurrentTimestamp() > 0, "当前时间戳应该大于0");
            assertNotNull(status.getCurrentTime(), "格式化的当前时间戳不应为空");
            assertEquals(5, status.getClockBackwardToleranceMs(), "时钟回拨容忍时间应该是默认值5ms");
        }

        @Test
        @DisplayName("resetStatistics should reset counters")
        void resetStatisticsShouldResetCounters() throws Exception {
            // 使用反射直接设置计数器
            Field totalGeneratedField = FmkSnowflakeIdGenerator.class.getDeclaredField("totalGenerated");
            totalGeneratedField.setAccessible(true);
            totalGeneratedField.get(idGenerator).getClass().getMethod("set", long.class).invoke(totalGeneratedField.get(idGenerator), 100L);

            Field clockBackwardCountField = FmkSnowflakeIdGenerator.class.getDeclaredField("clockBackwardCount");
            clockBackwardCountField.setAccessible(true);
            clockBackwardCountField.get(idGenerator).getClass().getMethod("set", long.class).invoke(clockBackwardCountField.get(idGenerator), 5L);

            Field waitCountField = FmkSnowflakeIdGenerator.class.getDeclaredField("waitCount");
            waitCountField.setAccessible(true);
            waitCountField.get(idGenerator).getClass().getMethod("set", long.class).invoke(waitCountField.get(idGenerator), 10L);

            // 重置统计信息
            idGenerator.resetStatistics();

            // 验证计数器已重置
            SnowflakeStatus status = idGenerator.getStatus();
            assertEquals(0, status.getTotalGenerated(), "总生成计数应该重置为0");
            assertEquals(0, status.getClockBackwardCount(), "时钟回拨计数应该重置为0");
            assertEquals(0, status.getWaitCount(), "等待计数应该重置为0");
        }
    }

    @Nested
    @DisplayName("Configuration Tests")
    class ConfigurationTests {

        @Test
        @DisplayName("constructor should set clock backward tolerance")
        void constructorShouldSetClockBackwardTolerance() {
            FmkSnowflakeIdGenerator generator1 = new FmkSnowflakeIdGenerator(10);
            assertEquals(10, generator1.getStatus().getClockBackwardToleranceMs(), "应该设置指定的时钟回拨容忍时间");

            FmkSnowflakeIdGenerator generator2 = new FmkSnowflakeIdGenerator(0);
            assertEquals(0, generator2.getStatus().getClockBackwardToleranceMs(), "应该允许0ms的时钟回拨容忍时间");

            FmkSnowflakeIdGenerator generator3 = new FmkSnowflakeIdGenerator(-5);
            assertEquals(0, generator3.getStatus().getClockBackwardToleranceMs(), "负数应该被转换为0");
        }

        @Test
        @DisplayName("getConfigInfo should return configuration information")
        void getConfigInfoShouldReturnConfigurationInformation() {
            String configInfo = idGenerator.getConfigInfo();

            assertTrue(configInfo.contains("起始时间="), "配置信息应该包含起始时间");
            assertTrue(configInfo.contains("时钟回拨容忍=5ms"), "配置信息应该包含时钟回拨容忍时间");
            assertTrue(configInfo.contains("时间戳位数=41"), "配置信息应该包含时间戳位数");
            assertTrue(configInfo.contains("序列号位数=22"), "配置信息应该包含序列号位数");
            assertTrue(configInfo.contains("最大序列号=4194303"), "配置信息应该包含最大序列号");
        }

        @Test
        @DisplayName("getPerformanceInfo should return performance information")
        void getPerformanceInfoShouldReturnPerformanceInformation() {
            String performanceInfo = idGenerator.getPerformanceInfo();

            assertTrue(performanceInfo.contains("每毫秒最多4194304个ID"), "性能信息应该包含每毫秒ID数");
            assertTrue(performanceInfo.contains("每秒最多4194304000个ID"), "性能信息应该包含每秒ID数");
        }
    }

    @Nested
    @DisplayName("Edge Case Tests")
    class EdgeCaseTests {

        @Test
        @DisplayName("should throw exception when timestamp exceeds max value")
        void shouldThrowExceptionWhenTimestampExceedsMaxValue() throws Exception {
            // 使用反射设置lastTimestamp
            Field lastTimestampField = FmkSnowflakeIdGenerator.class.getDeclaredField("lastTimestamp");
            lastTimestampField.setAccessible(true);

            // 设置一个非常大的时间戳，超过41位能表示的范围
            long maxTimestamp = START_TIMESTAMP + (1L << 41);

            // 模拟时间流逝到最大值之后
            try (MockedStatic<System> mockedSystem = Mockito.mockStatic(System.class)) {
                mockedSystem.when(System::currentTimeMillis).thenReturn(maxTimestamp);

                // 应该抛出异常
                RuntimeException exception = assertThrows(RuntimeException.class, FmkSnowflakeIdGenerator::nextId);
                assertTrue(exception.getMessage().contains("时间戳超过最大值"), "异常消息应该提及时间戳超过最大值");
            }
        }

        @Test
        @DisplayName("formatTimestamp should format timestamp correctly")
        void formatTimestampShouldFormatTimestampCorrectly() throws Exception {
            // 使用反射访问私有方法
            java.lang.reflect.Method formatTimestampMethod = FmkSnowflakeIdGenerator.class.getDeclaredMethod("formatTimestamp", long.class);
            formatTimestampMethod.setAccessible(true);

            // 测试特定时间戳的格式化
            long timestamp = START_TIMESTAMP; // 2025-07-20 00:00:00 UTC
            String formatted = (String) formatTimestampMethod.invoke(idGenerator, timestamp);

            // 使用相同的格式手动格式化进行比较
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
                    .withZone(ZoneOffset.UTC);
            String expected = formatter.format(Instant.ofEpochMilli(timestamp)) + " UTC";

            assertEquals(expected, formatted, "时间戳格式化应该正确");
        }
    }
}