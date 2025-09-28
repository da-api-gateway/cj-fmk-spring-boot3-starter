package com.cjlabs.web.threadlocal;

import com.xodo.fmk.common.LanguageEnum;
import com.xodo.fmk.jdk.basetype.type.FmkTraceId;
import com.xodo.fmk.web.FmkContextUtil;
import com.xodo.fmk.web.exception.MultiLanguageMessageExceptionEnum;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class FmkResult<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    private int code;
    private String msg;
    private T data;
    private long timestamp;
    private FmkTraceId traceId;

    // 私有构造函数，强制通过静态方法创建
    private FmkResult(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
        this.timestamp = System.currentTimeMillis();
    }

    // ======================== 成功响应 ========================

    /**
     * 成功响应（无数据）
     */
    public static <T> FmkResult<T> success() {
        LanguageEnum languageEnum = FmkContextUtil.getCurrentLanguageCode();
        if (LanguageEnum.ZH.equals(languageEnum)) {
            return new FmkResult<>(
                    200,
                    MultiLanguageMessageExceptionEnum.GENERAL_OPERATION___SUCCESS.getMsgZh(),
                    null);
        } else {
            return new FmkResult<>(
                    200,
                    MultiLanguageMessageExceptionEnum.GENERAL_OPERATION___SUCCESS.getMsgEn(),
                    null);
        }
    }

    /**
     * 成功响应（带数据）
     */
    public static <T> FmkResult<T> success(T data) {
        LanguageEnum languageEnum = FmkContextUtil.getCurrentLanguageCode();
        if (LanguageEnum.ZH.equals(languageEnum)) {
            return new FmkResult<>(
                    200,
                    MultiLanguageMessageExceptionEnum.GENERAL_OPERATION___SUCCESS.getMsgZh(),
                    data);
        } else {
            return new FmkResult<>(
                    200,
                    MultiLanguageMessageExceptionEnum.GENERAL_OPERATION___SUCCESS.getMsgEn(),
                    data);
        }
    }

    // ======================== 失败响应 ========================

    /**
     * 失败响应（带错误码和消息）
     */
    public static <T> FmkResult<T> error(String msg) {
        return new FmkResult<>(500, msg, null);
    }

    /**
     * 失败响应（带错误码和消息）
     */
    public static <T> FmkResult<T> error(int code, String msg) {
        return new FmkResult<>(code, msg, null);
    }

    //
    // /**
    //  * 失败响应（带错误码和消息）
    //  */
    // public static <T> FmkResult<T> error(int code, String messageValue) {
    //     return new FmkResult<>(code, "ERROR", messageValue, null);
    // }
    //
    // /**
    //  * 失败响应（带错误码、消息键、消息和数据）
    //  */
    // public static <T> FmkResult<T> error(int code, String messageKey, String messageValue, T data) {
    //     return new FmkResult<>(code, messageKey, messageValue, data);
    // }

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
     * 链式调用设置数据
     */
    public FmkResult<T> withData(T data) {
        this.data = data;
        return this;
    }

    /**
     * 链式调用设置消息
     */
    public FmkResult<T> withMessage(String msg) {
        this.msg = msg;
        return this;
    }


}