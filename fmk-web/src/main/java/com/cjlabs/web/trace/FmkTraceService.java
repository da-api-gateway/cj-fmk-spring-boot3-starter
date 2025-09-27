package com.cjlabs.web.trace;

import com.xodo.fmk.common.FmkConstant;
import com.xodo.fmk.id.FmkIdUtil;
import com.xodo.fmk.jdk.basetype.type.FmkTraceId;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Component
public class FmkTraceService {
    private static final String TRACE_PREFIX = "TRACE";

    @Autowired
    private FmkIdUtil fmkIdUtil;

    public FmkTraceId generateTraceId() {
        String traceIdStr = fmkIdUtil.nextIdString(TRACE_PREFIX);
        return FmkTraceId.ofNullable(traceIdStr);
    }

    /**
     * 获取或生成TraceId
     */
    public FmkTraceId getOrGenerateTraceId(HttpServletRequest request) {
        String traceId = request.getHeader(FmkConstant.HEADER_TRACE_ID);
        if (StringUtils.isBlank(traceId)) {
            return generateTraceId();
        }
        return FmkTraceId.ofNullable(traceId);
    }

}
