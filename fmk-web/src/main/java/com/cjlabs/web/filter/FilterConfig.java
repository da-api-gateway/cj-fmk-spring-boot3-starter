package com.cjlabs.web.filter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

/**
 * 过滤器配置类
 * 配置过滤器的注册顺序和参数
 */
@Configuration
public class FilterConfig {

    /**
     * 创建 CORS 过滤器
     */
    @Bean
    public CorsFilter corsFilter() {
        return new CorsFilter();
    }

    /**
     * 创建 FmkTraceService
     */
    @Bean
    public FmkTraceService fmkTraceService() {
        return new FmkTraceService();
    }

    /**
     * 创建 Trace 过滤器
     */
    @Bean
    public TraceFilter traceFilter(FmkTraceService fmkTraceService) {
        return new TraceFilter(fmkTraceService);
    }

    // /**
    //  * 创建 CORS 过滤器
    //  */
    // @Bean
    // public TraceFilter traceFilter() {
    //     return new TraceFilter();
    // }

    /**
     * 请求日志过滤器
     */
    @Bean
    public CommonsRequestLoggingFilter logFilter() {
        CommonsRequestLoggingFilter filter = new CommonsRequestLoggingFilter();
        filter.setIncludeQueryString(true);  // 打印 query
        filter.setIncludePayload(true);      // 打印请求体
        filter.setIncludeHeaders(true);      // 打印请求头
        filter.setMaxPayloadLength(10000);   // Body 最大长度
        filter.setBeforeMessagePrefix("请求处理前: ");
        filter.setAfterMessagePrefix("请求处理后: ");
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

    /**
     * 注册 Trace 过滤器
     */
    @Bean
    public FilterRegistrationBean<TraceFilter> traceFilterRegistration(TraceFilter traceFilter) {
        FilterRegistrationBean<TraceFilter> registration = new FilterRegistrationBean<>(traceFilter);
        registration.setOrder(2); // 第二优先级，在CORS之后
        registration.addUrlPatterns("/*"); // 所有路径
        return registration;
    }
}