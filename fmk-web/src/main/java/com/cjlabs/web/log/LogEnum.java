package com.cjlabs.web.log;

import com.xodo.fmk.core.enums.IEnumStr;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum LogEnum implements IEnumStr {

    ADMIN__USER_UPDATE_PLATFORM_INFO("ADMIN__USER_UPDATE_PLATFORM_INFO", "管理后台，修改用户第三方平台信息"),
    ADMIN__USER_SAVE_PLATFORM_INFO("ADMIN__USER_SAVE_PLATFORM_INFO", "管理后台，新增用户第三方平台信息"),
    ADMIN__USER_UPDATE_USER_POINT("ADMIN__USER_UPDATE_USER_POINT", "管理后台，修改用户积分"),
    ADMIN__ACTIVITY_UPDATE_ACTIVITY("ADMIN__ACTIVITY_UPDATE_ACTIVITY", "管理后台，修改活动信息"),
    ADMIN__POINT_EXCHANGE_UPDATE_APPROVE("ADMIN__POINT_EXCHANGE_UPDATE_APPROVE", "管理后台，审核积分兑换"),

    USER__POINT_EXCHANGE_CREATE("USER__POINT_EXCHANGE_CREATE", "用户，创建积分兑换申请"),


    ;

    private final String code;
    private final String msg;
}
