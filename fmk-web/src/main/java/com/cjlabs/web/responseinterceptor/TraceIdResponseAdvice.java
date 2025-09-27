package com.cjlabs.web.responseinterceptor;

import com.xodo.fmk.core.FmkResult;
import com.xodo.fmk.jdk.basetype.type.FmkTraceId;
import com.xodo.fmk.web.FmkContextUtil;
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
 * TraceId响应处理器 - 统一为FmkResult添加traceId
 */
@Slf4j
@ControllerAdvice
public class TraceIdResponseAdvice implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        // 只处理FmkResult类型的响应
        return FmkResult.class.isAssignableFrom(returnType.getParameterType());
    }

    @Override
    public Object beforeBodyWrite(Object body,
                                  MethodParameter returnType,
                                  MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request,
                                  ServerHttpResponse response) {

        if (body instanceof FmkResult<?>) {
            FmkResult<?> fmkResult = (FmkResult<?>) body;
            try {
                // 🔥 从上下文获取traceId并设置到响应结果中
                Optional<FmkTraceId> traceIdOptional = FmkContextUtil.getTraceId();
                if (traceIdOptional.isPresent()) {
                    FmkTraceId fmkTraceId = traceIdOptional.get();
                    fmkResult.setTraceId(fmkTraceId);
                    log.info("TraceIdResponseAdvice|设置traceId到响应结果: {}", fmkTraceId);
                }
            } catch (Exception e) {
                log.warn("TraceIdResponseAdvice|设置traceId失败: {}", e.getMessage());
            }
        }

        return body;
    }
}