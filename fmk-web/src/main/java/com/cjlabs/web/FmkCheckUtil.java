package com.cjlabs.web;

import com.xodo.fmk.jdk.basetype.type.FmkUserId;
import com.xodo.fmk.web.exception.DcxjCommonException;
import com.xodo.fmk.web.exception.DcxjDbMsgKeyException;
import com.xodo.fmk.web.exception.ExceptionDbInterface;

import java.util.Objects;
import java.util.Optional;

public class FmkCheckUtil {
    /**
     * 🔥 新增：获取当前用户ID
     */
    public static FmkUserId checkLogin() {
        Optional<FmkUserId> userIdOptional = FmkContextUtil.getUserId();
        if (userIdOptional.isEmpty()) {
            throwDcxjCommonException(
                    ExceptionDbInterface.AUTHENTICATION_USER.AUTHENTICATION_USER,
                    ExceptionDbInterface.AUTHENTICATION_USER.MsgValue.TOKEN_EXPIRED);
        }
        return userIdOptional.get();
    }

    public static void checkInput(boolean flag) {
        checkInput(flag, "入参", "Input value");
    }

    /**
     * 检查数值范围
     *
     * @param flag        最小值
     * @param fieldNameZh 字段中文名
     * @param fieldNameEn 字段英文名
     * @throws DcxjCommonException 数值超出范围时抛出
     */
    public static void checkInput(boolean flag, String fieldNameZh, String fieldNameEn) {
        if (flag) {
            throwDcxjCommonException(fieldNameZh + "不能为空", fieldNameEn + " cannot be null");
        }
    }

    /**
     * 检查数值范围
     *
     * @param flag  最小值
     * @param zhMsg 字段中文名
     * @param enMsg 字段英文名
     * @throws DcxjCommonException 数值超出范围时抛出
     */
    public static void checkDateIsTrue(boolean flag, String zhMsg, String enMsg) {
        if (flag) {
            throwDcxjCommonException(zhMsg, enMsg);
        }
    }

    /**
     * 检查数值范围
     *
     * @param value       数值
     * @param min         最小值
     * @param max         最大值
     * @param fieldNameZh 字段中文名
     * @param fieldNameEn 字段英文名
     * @throws DcxjCommonException 数值超出范围时抛出
     */
    public static void checkNumberRange(Number value, Number min, Number max, String fieldNameZh, String fieldNameEn) {
        if (Objects.isNull(value)) {
            return; // 空值不做范围检查
        }

        double val = value.doubleValue();
        double minVal = min.doubleValue();
        double maxVal = max.doubleValue();

        if (val < minVal || val > maxVal) {
            String zhMsg = String.format("%s必须在%s到%s之间", fieldNameZh, min, max);
            String enMsg = String.format("%s must be between %s and %s", fieldNameEn, min, max);
            throwDcxjCommonException(zhMsg, enMsg);
        }
    }

    /**
     * 抛出通用业务异常
     *
     * @param zhMsg 中文消息
     * @param enMsg 英文消息
     */
    public static void throwDcxjCommonException(String zhMsg, String enMsg) {
        throw new DcxjCommonException(zhMsg, enMsg);
    }

    /**
     * 抛出通用业务异常
     *
     * @param msgKey  数据库
     * @param msgType 类型
     */
    public static void throwDcxjException(String msgKey, String msgType) {
        throw new DcxjDbMsgKeyException(msgKey, msgType);
    }
}
