package com.cjlabs.core.common;

import com.xodo.business.common.user.enums.DeviceTypeEnum;

public interface FmkConstant {

    Long SYSTEM_USER = 0L;

    String SYMBOL_UNDERLINE = "_";

    String API_PREFIX = "/xodo";

    /**
     * traceId
     */
    String HEADER_TRACE_ID = "X-Trace-Id";
    /**
     * 用户的设备信息
     */
    String HEADER_DEVICE_ID = "X-Device-Id";

    /**
     * 用户token，可以拿到用户的一些信息
     */
    String HEADER_USER_TOKEN = "X-Token";
    /**
     * 用户的设备信息
     */
    String HEADER_DEVICE_VERSION = "X-Device-Version";
    /**
     * {@link DeviceTypeEnum}
     */
    String HEADER_DEVICE_TYPE = "X-Device_Type";

    String HEADER_DEVICE_LANGUAGE = "X-Accept-Language";

    String HEADER_USER_AGENT = "User-Agent";


    String HEADER_REFERER = "Referer";


    /**
     * traceId
     */
    String MDC_TRACE_ID = "X-Trace-Id";
    String MDC_USER_ID = "X-User-Id";

}
