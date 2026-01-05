package com.cjlabs.web;

import com.cjlabs.web.requestinterceptor.FmkContextInterceptor;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Slf4j
@Configuration
@AutoConfigureAfter(FmkWebAutoConfiguration.class)
public class FmkWebMvcConfiguration implements WebMvcConfigurer {

    @Lazy
    @Autowired(required = false)
    private FmkContextInterceptor contextInterceptor;

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
        if (contextInterceptor != null) {
            registry.addInterceptor(contextInterceptor)
                    .addPathPatterns("/**")
                    .excludePathPatterns(EXCLUDE_PATHS)
                    .order(1);
            log.info("FmkWebMvcConfiguration|配置拦截器|excludePaths={}", EXCLUDE_PATHS);
        } else {
            log.warn("FmkWebMvcConfiguration|FmkContextInterceptor未初始化，跳过拦截器注册");
        }
    }
}