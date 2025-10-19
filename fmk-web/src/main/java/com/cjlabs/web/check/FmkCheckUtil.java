package com.cjlabs.web.check;

import com.cjlabs.core.types.longs.FmkUserId;
import com.cjlabs.web.exception.BusinessException;
import com.cjlabs.web.exception.BusinessExceptionEnum;
import com.cjlabs.web.exception.SystemException;
import com.cjlabs.web.exception.SystemExceptionEnum;
import com.cjlabs.web.exception.ValidationException;
import com.cjlabs.web.exception.ValidationExceptionEnum;
import com.cjlabs.web.threadlocal.FmkContextUtil;

/**
 * 通用验证检查和异常抛出工具类
 */
public class FmkCheckUtil {
    /**
     * 检查用户是否已登录并返回用户ID
     *
     * @return 当前用户ID
     * @throws BusinessException 如果用户未登录
     */
    public static FmkUserId checkLogin() {
        return FmkContextUtil.getUserId()
                .orElseThrow(() -> new BusinessException(BusinessExceptionEnum.UNAUTHORIZED));
    }

    /**
     * 检查条件是否为真，如果为真则抛出ValidationException
     *
     * @param condition 要检查的条件
     */
    public static void checkInput(boolean condition) {
        if (condition) {
            throwValidation(ValidationExceptionEnum.INVALID_PARAMETER);
        }
    }

    /**
     * 检查条件是否为真，如果为真则抛出指定的ValidationException
     *
     * @param condition     要检查的条件
     * @param exceptionEnum 要使用的异常枚举
     */
    public static void checkInput(boolean condition, ValidationExceptionEnum exceptionEnum) {
        if (condition) {
            throwValidation(exceptionEnum);
        }
    }

    /**
     * 检查条件是否为真，如果为真则抛出ValidationException
     *
     * @param condition 要检查的条件
     */
    public static void checkData(boolean condition) {
        if (condition) {
            throwValidation(ValidationExceptionEnum.INVALID_PARAMETER);
        }
    }

    /**
     * 检查条件是否为真，如果为真则抛出指定的ValidationException
     *
     * @param condition     要检查的条件
     * @param exceptionEnum 要使用的异常枚举
     */
    public static void checkData(boolean condition, ValidationExceptionEnum exceptionEnum) {
        if (condition) {
            throwValidation(exceptionEnum);
        }
    }

    /**
     * 抛出指定的SystemException
     *
     * @param exceptionEnum 要使用的异常枚举
     */
    public static void throwSystem(SystemExceptionEnum exceptionEnum) {
        throw new SystemException(exceptionEnum);
    }

    /**
     * 有条件地抛出指定的SystemException
     *
     * @param condition     要检查的条件
     * @param exceptionEnum 要使用的异常枚举
     */
    public static void throwSystem(boolean condition, SystemExceptionEnum exceptionEnum) {
        if (condition) {
            throw new SystemException(exceptionEnum);
        }
    }

    /**
     * 抛出指定的BusinessException
     *
     * @param exceptionEnum 要使用的异常枚举
     */
    public static void throwBusiness(BusinessExceptionEnum exceptionEnum) {
        throw new BusinessException(exceptionEnum);
    }

    /**
     * 有条件地抛出指定的BusinessException
     *
     * @param condition     要检查的条件
     * @param exceptionEnum 要使用的异常枚举
     */
    public static void throwBusiness(boolean condition, BusinessExceptionEnum exceptionEnum) {
        if (condition) {
            throw new BusinessException(exceptionEnum);
        }
    }

    /**
     * 抛出指定的ValidationException
     *
     * @param exceptionEnum 要使用的异常枚举
     */
    public static void throwValidation(ValidationExceptionEnum exceptionEnum) {
        throw new ValidationException(exceptionEnum);
    }

    /**
     * 有条件地抛出指定的ValidationException
     *
     * @param condition     要检查的条件
     * @param exceptionEnum 要使用的异常枚举
     */
    public static void throwValidation(boolean condition, ValidationExceptionEnum exceptionEnum) {
        if (condition) {
            throw new ValidationException(exceptionEnum);
        }
    }
}