package com.cjlabs.web.filter;

import com.cjlabs.core.types.strings.FmkSpanId;
import com.cjlabs.core.types.strings.FmkTraceId;
import com.cjlabs.domain.common.FmkConstant;
import com.cjlabs.web.threadlocal.FmkContextInfo;
import com.cjlabs.web.threadlocal.FmkContextUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 链路追踪过滤器
 * 负责生成和管理请求的 TraceId 和 SpanId
 */
@Slf4j
@Component
public class TraceFilter extends OncePerRequestFilter {
    private FmkTraceService fmkTraceService;

    @Autowired
    public void setFmkTraceService(FmkTraceService fmkTraceService) {
        this.fmkTraceService = fmkTraceService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        try {
            // 获取或生成 TraceId
            FmkTraceId traceId = fmkTraceService.getOrGenerateTraceId(request);
            FmkSpanId spanId = fmkTraceService.getOrGenerateSpanId(request);


            // 设置到响应头
            response.setHeader(FmkConstant.HEADER_TRACE_ID, traceId.getValue());
            response.setHeader(FmkConstant.HEADER_SPAN_ID, spanId.getValue());

            // 设置到 MDC
            MDC.put(FmkConstant.MDC_TRACE_ID, traceId.getValue());
            MDC.put(FmkConstant.MDC_SPAN_ID, spanId.getValue());

            // 创建或获取上下文信息
            FmkContextInfo contextInfo = new FmkContextInfo();
            contextInfo.setTraceId(traceId);
            contextInfo.setSpanId(spanId);
            contextInfo.setRequestTime(LocalDateTime.now());
            contextInfo.setRequestUri(request.getRequestURI());

            // 设置到 ThreadLocal
            FmkContextUtil.setContextInfo(contextInfo);

            log.debug("TraceFilter|doFilter|设置 TraceId/SpanId|traceId={} spanId={}", traceId.getValue(), spanId);

            // 继续过滤器链
            filterChain.doFilter(request, response);
        } finally {
            // 清理 ThreadLocal 和 MDC
            FmkContextUtil.clear();
            MDC.remove(FmkConstant.MDC_TRACE_ID);
            MDC.remove(FmkConstant.MDC_SPAN_ID);
        }
    }
}
