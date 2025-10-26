package com.cjlabs.web;

import com.cjlabs.web.filter.AllowPostOptionsFilter;
import com.cjlabs.web.filter.CorsFilter;
import com.cjlabs.web.filter.FmkTraceService;
import com.cjlabs.web.filter.TraceFilter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

@Configuration
public class FilterConfig {

    /**
     * 创建 FmkTraceService Bean
     */
    @Bean
    public FmkTraceService fmkTraceService() {
        return new FmkTraceService();
    }

    /**
     * 仅允许 POST + OPTIONS 请求过滤器
     */
    @Bean
    public FilterRegistrationBean<AllowPostOptionsFilter> allowPostOptionsFilterRegistration() {
        FilterRegistrationBean<AllowPostOptionsFilter> registration = new FilterRegistrationBean<>(new AllowPostOptionsFilter());
        // 在这里处理一下，最高优先级
        registration.setOrder(Integer.MIN_VALUE);
        registration.addUrlPatterns("/*");
        return registration;
    }

    /**
     * CORS 过滤器
     */
    @Bean
    public FilterRegistrationBean<CorsFilter> corsFilterRegistration() {
        FilterRegistrationBean<CorsFilter> registration = new FilterRegistrationBean<>(new CorsFilter());
        registration.setOrder(1000);
        registration.addUrlPatterns("/*");
        return registration;
    }

    /**
     * Trace 过滤器
     */
    @Bean
    public FilterRegistrationBean<TraceFilter> traceFilterRegistration(@Autowired FmkTraceService fmkTraceService) {
        TraceFilter filter = new TraceFilter();
        filter.setFmkTraceService(fmkTraceService); // 注入服务
        FilterRegistrationBean<TraceFilter> registration = new FilterRegistrationBean<>(filter);
        registration.setOrder(1001);
        registration.addUrlPatterns("/*");
        return registration;
    }

    /**
     * 请求日志过滤器
     */
    @Bean
    public FilterRegistrationBean<CommonsRequestLoggingFilter> logFilterRegistration() {
        CommonsRequestLoggingFilter logFilter = new CommonsRequestLoggingFilter();
        logFilter.setIncludeQueryString(true);
        logFilter.setIncludePayload(true);
        logFilter.setIncludeHeaders(true);
        logFilter.setMaxPayloadLength(10000);
        logFilter.setBeforeMessagePrefix("请求处理前: ");
        logFilter.setAfterMessagePrefix("请求处理后: ");

        // 放在最后，保证 TraceFilter 先设置 MDC
        FilterRegistrationBean<CommonsRequestLoggingFilter> registration = new FilterRegistrationBean<>(logFilter);
        registration.setOrder(Integer.MAX_VALUE);
        registration.addUrlPatterns("/*");
        return registration;
    }
}
