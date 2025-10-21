package com.cjlabs.web.filter;

import com.cjlabs.core.types.strings.FmkTraceId;
import com.cjlabs.domain.common.FmkConstant;
import com.cjlabs.web.threadlocal.FmkContextInfo;
import com.cjlabs.web.threadlocal.FmkContextUtil;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * 链路追踪过滤器
 * 负责生成和管理请求的TraceId，并将其设置到ThreadLocal和响应头中
 */
@Slf4j
public class TraceFilter implements Filter {
    @Autowired
    private FmkTraceService fmkTraceService;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("TraceFilter|init|初始化");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        try {
            // 获取或生成TraceId
            FmkTraceId traceId = fmkTraceService.getOrGenerateTraceId(request);

            // 设置到响应头
            response.setHeader(FmkConstant.HEADER_TRACE_ID, traceId.getValue());

            // 设置到MDC用于日志
            MDC.put(FmkConstant.MDC_TRACE_ID, traceId.getValue());

            // 创建或获取上下文信息
            FmkContextInfo contextInfo = new FmkContextInfo();
            contextInfo.setTraceId(traceId);
            contextInfo.setRequestTime(LocalDateTime.now());
            contextInfo.setRequestUri(request.getRequestURI());

            // 设置到ThreadLocal
            FmkContextUtil.setContextInfo(contextInfo);

            log.debug("TraceFilter|doFilter|设置TraceId|traceId={}", traceId.getValue());

            // 继续过滤器链
            filterChain.doFilter(request, response);
        } finally {
            // 清理ThreadLocal和MDC
            FmkContextUtil.clear();
            MDC.remove(FmkConstant.MDC_TRACE_ID);
            MDC.remove(FmkConstant.MDC_SPAN_ID);
        }
    }

    @Override
    public void destroy() {
        log.info("TraceFilter|destroy|销毁");
    }
}