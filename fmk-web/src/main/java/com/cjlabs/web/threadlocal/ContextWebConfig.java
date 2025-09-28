package com.cjlabs.web.threadlocal;

import com.xodo.fmk.web.requestinterceptor.FmkAuthenticationInterceptor;
import com.xodo.fmk.web.requestinterceptor.FmkContextInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web配置 - 注册上下文拦截器
 */
@Configuration
public class ContextWebConfig implements WebMvcConfigurer {

    @Autowired
    private FmkContextInterceptor fmkContextInterceptor;

    @Autowired
    private FmkAuthenticationInterceptor fmkAuthenticationInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 1. 首先添加上下文拦截器（优先级最高）
        registry.addInterceptor(fmkContextInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(
                    "/static/**", 
                    "/favicon.ico", 
                    "/error",
                    "/actuator/**",
                    "/swagger-ui/**",
                    "/v3/api-docs/**"
                )
                .order(1);

        // 2. 然后添加认证拦截器（在上下文拦截器之后执行）
        registry.addInterceptor(fmkAuthenticationInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(
                    "/static/**", 
                    "/favicon.ico", 
                    "/error",
                    "/actuator/**",
                    "/swagger-ui/**",
                    "/v3/api-docs/**"
                )
                .order(2);
    }
}