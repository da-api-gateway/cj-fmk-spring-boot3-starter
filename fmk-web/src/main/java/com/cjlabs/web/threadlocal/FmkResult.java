package com.cjlabs.web.threadlocal;

import com.cjlabs.core.types.strings.FmkTraceId;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class FmkResult<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    private int code;

    private T data;

    private long timestamp;

    private FmkTraceId traceId;

    private String errorType;

    private String errorKey;

    // 私有构造函数，强制通过静态方法创建
    private FmkResult(int code, T data) {
        this.code = code;
        this.data = data;
        this.timestamp = System.currentTimeMillis();
    }

    private FmkResult(int code, String errorType, String errorKey) {
        this.code = code;
        this.errorType = errorType;
        this.errorKey = errorKey;
        this.timestamp = System.currentTimeMillis();
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
        return new FmkResult<>(
                200,
                data);
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

}