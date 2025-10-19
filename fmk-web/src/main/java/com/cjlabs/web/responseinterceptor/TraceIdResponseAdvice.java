package com.cjlabs.web.responseinterceptor;

import com.cjlabs.core.types.strings.FmkTraceId;
import com.cjlabs.domain.common.FmkConstant;
import com.cjlabs.web.threadlocal.FmkContextUtil;
import com.cjlabs.web.threadlocal.FmkResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.Optional;

/**
 * TraceId响应处理器
 * 统一为FmkResult类型的响应添加traceId，确保客户端可以获取到链路追踪ID
 */
@Slf4j
@ControllerAdvice
public class TraceIdResponseAdvice implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        // 只处理FmkResult类型的响应
        return returnType.getParameterType() != null &&
                FmkResult.class.isAssignableFrom(returnType.getParameterType());
    }

    @Override
    public Object beforeBodyWrite(Object body,
                                  MethodParameter returnType,
                                  MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request,
                                  ServerHttpResponse response) {
        // 快速检查，避免不必要的处理
        if (!(body instanceof FmkResult<?>)) {
            return body;
        }

        FmkResult<?> fmkResult = (FmkResult<?>) body;

        try {
            // 从上下文获取traceId并设置到响应结果中
            FmkContextUtil.getTraceId().ifPresent(traceId -> {
                fmkResult.setTraceId(traceId);

                // 同时设置到HTTP响应头，确保客户端可以从头信息中获取
                response.getHeaders().add(FmkConstant.HEADER_TRACE_ID, traceId.getValue());

                if (log.isDebugEnabled()) {
                    log.debug("TraceIdResponseAdvice|设置traceId|value={}", traceId.getValue());
                }
            });
        } catch (Exception e) {
            log.warn("TraceIdResponseAdvice|设置traceId失败|error={}", e.getMessage());
        }

        return fmkResult;
    }
}