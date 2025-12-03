package com.cjlabs.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ClientTypeEnum implements IEnumStr {

    // ADMIN("ADMIN", "后台管理控制台"),
    WEB("WEB", "PC 浏览器"),
    MOBILE_WEB("MOBILE_WEB", "移动端 H5 / 小程序"),
    ANDROID("ANDROID", "Android 原生客户端"),
    IOS("IOS", "iOS 原生客户端"),
    DESKTOP("DESKTOP", "桌面客户端/工具"),
    API("API", "第三方 API 调用"),
    // SERVICE("SERVICE", "内部服务/批处理任务"),
    // THIRD_PARTY("THIRD_PARTY", "外部合作渠道"),

    ;

    private final String code;
    private final String msg;

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMsg() {
        return msg;
    }
}