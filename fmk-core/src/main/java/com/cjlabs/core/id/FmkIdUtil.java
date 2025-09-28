package com.cjlabs.core.id;

import com.xodo.fmk.web.util.FmkSpringUtil;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 雪花算法工具类（无机器ID版本）
 * 提供静态方法访问雪花算法功能
 */
@Slf4j
@Component
public class FmkIdUtil {

    /**
     * 获取雪花算法生成器实例
     *
     * @return SnowflakeIdGenerator实例
     */
    private SnowflakeIdGenerator getGenerator() {
        return FmkSpringUtil.getBean(SnowflakeIdGenerator.class);
    }

    /**
     * 生成下一个ID
     *
     * @return 唯一ID
     */
    public long nextId() {
        return getGenerator().nextId();
    }

    /**
     * 批量生成ID
     *
     * @param count 生成数量
     * @return ID数组
     */
    public long[] nextIds(int count) {
        return getGenerator().nextIds(count);
    }

    /**
     * 解析ID信息
     *
     * @param id 要解析的ID
     * @return ID信息
     */
    public SnowflakeIdInfo parseId(long id) {
        return getGenerator().parseId(id);
    }

    /**
     * 获取生成器状态
     *
     * @return 生成器状态信息
     */
    public SnowflakeStatus getStatus() {
        return getGenerator().getStatus();
    }

    /**
     * 验证ID是否有效
     *
     * @param id 要验证的ID
     * @return 是否有效
     */
    public boolean isValidId(long id) {
        return getGenerator().isValidId(id);
    }

    /**
     * 重置统计信息
     */
    public void resetStatistics() {
        getGenerator().resetStatistics();
    }

    /**
     * 获取配置信息
     *
     * @return 配置信息字符串
     */
    public String getConfigInfo() {
        return getGenerator().getConfigInfo();
    }

    /**
     * 获取性能信息
     *
     * @return 性能信息字符串
     */
    public String getPerformanceInfo() {
        return getGenerator().getPerformanceInfo();
    }

    /**
     * 生成ID字符串（用于需要字符串ID的场景）
     *
     * @return ID字符串
     */
    public String nextIdString() {
        return String.valueOf(nextId());
    }

    /**
     * 生成指定前缀的ID字符串
     *
     * @param prefix 前缀
     * @return 带前缀的ID字符串
     */
    public String nextIdString(String prefix) {
        return prefix + nextId();
    }
}