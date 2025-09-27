package com.cjlabs.web.exception;

import com.xodo.fmk.core.enums.IEnumStrV2;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MultiLanguageMessageExceptionEnum implements IEnumStrV2 {
    // 通用成功提示
    GENERAL_OPERATION___SUCCESS(
            "GENERAL_OPERATION",
            "SUCCESS", "处理成功。", "Operation successful."),

    SYSTEM_ERROR___SERVER_ERROR(
            "SYSTEM_ERROR",
            "SERVER_ERROR", "服务器内部错误，请重试。", "Internal server error. Please try again."),

    // 以上是常见的报错
    // 下面是一些，非常不常见的报错

    ;
    private final String type;
    private final String key;
    private final String msgZh;
    private final String msgEn;

}
