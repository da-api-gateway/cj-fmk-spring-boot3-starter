package com.cjlabs.web.filter;

import com.cjlabs.core.types.strings.FmkSpanId;
import com.cjlabs.core.types.strings.FmkTraceId;
import com.cjlabs.domain.common.FmkConstant;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class FmkTraceService {
    public FmkTraceId generateTraceId() {
        return FmkTraceId.generate();
    }

    public FmkSpanId generateSpanId() {
        return FmkSpanId.generate();
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

    /**
     * 获取或生成 SpanId
     */
    public FmkSpanId getOrGenerateSpanId(HttpServletRequest request) {
        String spanId = request.getHeader(FmkConstant.HEADER_SPAN_ID);
        if (StringUtils.isBlank(spanId)) {
            return generateSpanId();
        }
        return FmkSpanId.ofNullable(spanId);
    }

}
