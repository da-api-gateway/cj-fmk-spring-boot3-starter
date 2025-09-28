package com.cjlabs.domain.types.base;

import org.apache.commons.lang3.StringUtils;

/**
 * 字符串类型的基础类型
 */
public abstract class BaseStringType<S extends BaseStringType<S>> extends BaseType<String, S> {

    protected BaseStringType(String value) {
        super(value);
    }

    /**
     * 检查字符串是否为空
     */
    public boolean isEmpty() {
        return StringUtils.isEmpty(value);
    }

    /**
     * 检查字符串是否为空白
     */
    public boolean isBlank() {
        return StringUtils.isBlank(value);
    }

    /**
     * 获取字符串长度
     */
    public int length() {
        return value != null ? value.length() : 0;
    }

    /**
     * 转换为小写
     */
    public S toLowerCase() {
        return value != null ? newInstance(value.toLowerCase()) : null;
    }

    /**
     * 转换为大写
     */
    public S toUpperCase() {
        return value != null ? newInstance(value.toUpperCase()) : null;
    }

    /**
     * 截取子字符串
     */
    public S substring(int beginIndex) {
        return value != null ? newInstance(value.substring(beginIndex)) : null;
    }

    /**
     * 截取子字符串
     */
    public S substring(int beginIndex, int endIndex) {
        return value != null ? newInstance(value.substring(beginIndex, endIndex)) : null;
    }

    /**
     * 检查是否包含指定子串
     */
    public boolean contains(String subStr) {
        return value != null && value.contains(subStr);
    }

    /**
     * 替换字符串
     */
    public S replace(String target, String replacement) {
        return value != null ? newInstance(value.replace(target, replacement)) : null;
    }

    @Override
    public String toString() {
        return value;
    }
}