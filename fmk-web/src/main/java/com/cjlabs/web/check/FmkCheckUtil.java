package com.cjlabs.web.check;

import com.cjlabs.core.types.longs.FmkUserId;
import com.cjlabs.domain.exception.Error200Exception;
import com.cjlabs.domain.exception.Error200ExceptionEnum;
import com.cjlabs.domain.exception.Error500Exception;
import com.cjlabs.domain.exception.Error500ExceptionEnum;
import com.cjlabs.domain.exception.Error400Exception;
import com.cjlabs.domain.exception.Error400ExceptionEnum;
import com.cjlabs.web.threadlocal.FmkContextUtil;

/**
 * 通用验证检查和异常抛出工具类
 */
public class FmkCheckUtil {
    /**
     * 检查用户是否已登录并返回用户ID
     *
     * @return 当前用户ID
     * @throws Error200Exception 如果用户未登录
     */
    public static FmkUserId checkLogin() {
        return FmkContextUtil.getUserId()
                .orElseThrow(() -> new Error400Exception(Error400ExceptionEnum.UNAUTHORIZED));
    }

    /**
     * 检查条件是否为真，如果为真则抛出ValidationException
     *
     * @param condition 要检查的条件
     */
    public static void checkInput(boolean condition) {
        if (condition) {
            throw200Error(Error200ExceptionEnum.INVALID_PARAMETER);
        }
    }

    /**
     * 抛出指定的SystemException
     *
     * @param exceptionEnum 要使用的异常枚举
     */
    public static void throw500Error(Error500ExceptionEnum exceptionEnum) {
        throw new Error500Exception(exceptionEnum);
    }

    /**
     * 有条件地抛出指定的SystemException
     *
     * @param condition     要检查的条件
     * @param exceptionEnum 要使用的异常枚举
     */
    public static void throw500Error(boolean condition, Error500ExceptionEnum exceptionEnum) {
        if (condition) {
            throw new Error500Exception(exceptionEnum);
        }
    }

    /**
     * 抛出指定的BusinessException
     *
     * @param exceptionEnum 要使用的异常枚举
     */
    public static void throw200Error(Error200ExceptionEnum exceptionEnum) {
        throw new Error200Exception(exceptionEnum);
    }

    /**
     * 有条件地抛出指定的BusinessException
     *
     * @param condition     要检查的条件
     * @param exceptionEnum 要使用的异常枚举
     */
    public static void throw200Error(boolean condition, Error200ExceptionEnum exceptionEnum) {
        if (condition) {
            throw new Error200Exception(exceptionEnum);
        }
    }

    /**
     * 抛出指定的ValidationException
     *
     * @param exceptionEnum 要使用的异常枚举
     */
    public static void throw400Error(Error400ExceptionEnum exceptionEnum) {
        throw new Error400Exception(exceptionEnum);
    }

    /**
     * 有条件地抛出指定的ValidationException
     *
     * @param condition     要检查的条件
     * @param exceptionEnum 要使用的异常枚举
     */
    public static void throw400Error(boolean condition, Error400ExceptionEnum exceptionEnum) {
        if (condition) {
            throw new Error400Exception(exceptionEnum);
        }
    }
}