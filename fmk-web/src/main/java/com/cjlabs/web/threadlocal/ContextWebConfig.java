package com.cjlabs.web.threadlocal;

import com.cjlabs.web.requestinterceptor.FmkContextInterceptor;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * Web配置 - 注册上下文拦截器
 */
@Configuration
public class ContextWebConfig implements WebMvcConfigurer {

    @Autowired
    private FmkContextInterceptor fmkContextInterceptor;

    // 排除路径列表
    private static final List<String> EXCLUDE_PATHS = Lists.newArrayList(
            "/static/**",
            "/favicon.ico",
            "/error",
            "/actuator/**",
            "/swagger-ui/**",
            "/v3/api-docs/**"
    );

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 1. 首先添加上下文拦截器（优先级最高）
        registry.addInterceptor(fmkContextInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(EXCLUDE_PATHS)
                .order(1);
    }
}