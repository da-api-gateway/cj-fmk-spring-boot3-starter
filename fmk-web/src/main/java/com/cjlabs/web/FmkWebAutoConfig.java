package com.cjlabs.web;

import com.cjlabs.web.exception.GlobalExceptionHandler;
import com.cjlabs.web.requestinterceptor.FmkContextInterceptor;

import com.google.common.collect.Lists;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Component
@Configuration
public class FmkWebAutoConfig implements WebMvcConfigurer {

    @Bean
    public FmkContextInterceptor fmkContextInterceptor() {
        return new FmkContextInterceptor();
    }

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
        // 添加上下文拦截器（优先级最高）
        registry.addInterceptor(fmkContextInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns(EXCLUDE_PATHS)
                .order(1);
    }

    @Bean
    public GlobalExceptionHandler<Object> globalExceptionHandler() {
        return new GlobalExceptionHandler<>();
    }
}
