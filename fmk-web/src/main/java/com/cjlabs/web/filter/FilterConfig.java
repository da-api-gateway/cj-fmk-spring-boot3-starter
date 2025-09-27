package com.cjlabs.web.filter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

@Configuration
public class FilterConfig {

    @Bean
    public CommonsRequestLoggingFilter logFilter() {
        CommonsRequestLoggingFilter filter = new CommonsRequestLoggingFilter();
        filter.setIncludeQueryString(true);  // 打印 query
        filter.setIncludePayload(true);      // 打印请求体
        filter.setIncludeHeaders(true);      // 打印请求头
        filter.setMaxPayloadLength(10000);   // Body 最大长度
        filter.setBeforeMessagePrefix("BEFORE REQUEST : ");
        filter.setAfterMessagePrefix("AFTER REQUEST : ");
        return filter;
    }

    /**
     * 确保 CORS 过滤器最先执行
     */
    @Bean
    public FilterRegistrationBean<CorsFilter> corsFilterRegistration(CorsFilter corsFilter) {
        FilterRegistrationBean<CorsFilter> registration = new FilterRegistrationBean<>(corsFilter);
        registration.setOrder(1); // 最高优先级
        registration.addUrlPatterns("/*"); // 所有路径
        return registration;
    }
}
