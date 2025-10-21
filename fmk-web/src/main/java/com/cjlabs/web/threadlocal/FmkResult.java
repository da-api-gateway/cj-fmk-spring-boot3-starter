package com.cjlabs.web.threadlocal;

import com.cjlabs.core.types.strings.FmkTraceId;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Optional;

/**
 * 统一响应结果类
 */
@Getter
@Setter
@ToString
public class FmkResult<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 状态码
     */
    private int code;

    /**
     * 响应数据
     */
    private T data;

    /**
     * 时间戳
     */
    private long timestamp;

    /**
     * 追踪ID
     */
    private FmkTraceId traceId;

    /**
     * 错误类型
     */
    private String errorType;

    /**
     * 错误键
     */
    private String errorKey;

    // 私有构造函数，强制通过静态方法创建
    private FmkResult(int code, T data) {
        this.code = code;
        this.data = data;
        this.timestamp = System.currentTimeMillis();

        // 自动从上下文获取TraceId
        FmkContextUtil.getTraceId().ifPresent(this::setTraceId);
    }

    private FmkResult(int code, String errorType, String errorKey) {
        this.code = code;
        this.errorType = errorType;
        this.errorKey = errorKey;
        this.timestamp = System.currentTimeMillis();

        // 自动从上下文获取TraceId
        FmkContextUtil.getTraceId().ifPresent(this::setTraceId);
    }

    // ======================== 成功响应 ========================

    /**
     * 成功响应（无数据）
     */
    public static <T> FmkResult<T> success() {
        return new FmkResult<>(200, null);
    }

    /**
     * 成功响应（带数据）
     */
    public static <T> FmkResult<T> success(T data) {
        return new FmkResult<>(200, data);
    }

    /**
     * 成功响应（自定义状态码）
     */
    public static <T> FmkResult<T> success(int code, T data) {
        return new FmkResult<>(code, data);
    }

    // ======================== 失败响应 ========================

    /**
     * 失败响应（带错误码和消息）
     */
    public static <T> FmkResult<T> error(int code, String errorType, String errorKey) {
        return new FmkResult<>(code, errorType, errorKey);
    }

    // ======================== 工具方法 ========================

    /**
     * 判断是否成功
     */
    public boolean checkSuccess() {
        return this.code >= 200 && this.code < 300;
    }

    /**
     * 判断是否失败
     */
    public boolean checkError() {
        return !checkSuccess();
    }

    /**
     * 获取数据，如果成功
     */
    public Optional<T> getData() {
        return checkSuccess() ? Optional.ofNullable(data) : Optional.empty();
    }

    /**
     * 设置TraceId
     */
    public FmkResult<T> withTraceId(FmkTraceId traceId) {
        this.traceId = traceId;
        return this;
    }
}