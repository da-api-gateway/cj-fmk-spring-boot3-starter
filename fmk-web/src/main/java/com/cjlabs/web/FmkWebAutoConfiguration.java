package com.cjlabs.web;

import com.cjlabs.web.exception.GlobalExceptionHandler;
import com.cjlabs.web.filter.AllowPostOptionsFilter;
import com.cjlabs.web.filter.CorsFilter;
import com.cjlabs.web.filter.FmkTraceService;
import com.cjlabs.web.filter.TraceFilter;
import com.cjlabs.web.json.FmkJacksonUtil;
import com.cjlabs.web.requestinterceptor.FmkContextInterceptor;
import com.cjlabs.web.serializer.BigDecimalSerializer;
import com.cjlabs.web.serializer.EmptyStringToNullDeserializer;
import com.cjlabs.web.serializer.LongToStringSerializer;
import com.cjlabs.web.thread.FmkThreadPoolMonitor;
import com.cjlabs.web.thread.FmkThreadPoolProperties;
import com.cjlabs.web.thread.FmkTtlThreadPoolTaskExecutor;
import com.cjlabs.web.token.FmkTokenProperties;
import com.cjlabs.web.token.IFmkTokenService;
import com.cjlabs.web.util.FmkSpringUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.filter.CommonsRequestLoggingFilter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

@Slf4j
@AutoConfiguration
@EnableConfigurationProperties({FmkThreadPoolProperties.class, FmkTokenProperties.class})
public class FmkWebAutoConfiguration implements WebMvcConfigurer {
    public FmkWebAutoConfiguration() {
        log.info("FmkWebAutoConfiguration|初始化|Fmk Web 模块自动配置加载");
    }

    @Autowired(required = false)
    private IFmkTokenService fmkTokenService;

    /**
     * Spring 上下文工具类
     * 用于在非 Spring 管理的类中获取 Bean
     */
    @Bean
    public FmkSpringUtil fmkSpringUtil(ApplicationContext applicationContext) {
        log.info("FmkWebAutoConfiguration|注册FmkSpringUtil");
        FmkSpringUtil fmkSpringUtil = new FmkSpringUtil();
        try {
            fmkSpringUtil.setApplicationContext(applicationContext);
            log.info("FmkWebAutoConfiguration|FmkSpringUtil ApplicationContext 设置成功");
        } catch (Exception e) {
            log.error("FmkWebAutoConfiguration|FmkSpringUtil ApplicationContext 设置失败", e);
        }
        return fmkSpringUtil;
    }

    /**
     * 全局异常处理器
     */
    @Bean
    public GlobalExceptionHandler<Object> globalExceptionHandler() {
        log.info("FmkWebAutoConfiguration|注册GlobalExceptionHandler");
        return new GlobalExceptionHandler<>();
    }

    /**
     * Trace 服务
     * 用于生成和管理 TraceId、SpanId
     */
    @Bean
    public FmkTraceService fmkTraceService() {
        log.info("FmkWebAutoConfiguration|注册FmkTraceService");
        return new FmkTraceService();
    }

    /**
     * 上下文拦截器
     * 负责设置请求上下文信息（用户信息、客户端信息等）
     */
    @Bean
    public FmkContextInterceptor fmkContextInterceptor() {
        log.info("FmkWebAutoConfiguration|注册FmkContextInterceptor");
        FmkContextInterceptor fmkContextInterceptor = new FmkContextInterceptor();
        if (fmkTokenService != null) {
            fmkContextInterceptor.setFmkTokenService(fmkTokenService);
            log.info("FmkWebAutoConfiguration|Token服务已注入到拦截器");
        } else {
            log.warn("FmkWebAutoConfiguration|Token服务未启用，拦截器将跳过Token验证");
        }
        fmkContextInterceptor.setFmkTokenService(fmkTokenService);
        return fmkContextInterceptor;
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
        log.info("FmkWebAutoConfiguration|配置拦截器|excludePaths={}", EXCLUDE_PATHS);
    }

    /**
     * POST + OPTIONS 请求过滤器
     * 最高优先级，用于处理跨域预检请求
     */
    @Bean
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
    public FilterRegistrationBean<AllowPostOptionsFilter> allowPostOptionsFilterRegistration() {
        FilterRegistrationBean<AllowPostOptionsFilter> registration = new FilterRegistrationBean<>(new AllowPostOptionsFilter());
        registration.setOrder(Integer.MIN_VALUE);
        registration.addUrlPatterns("/*");
        log.info("FmkWebAutoConfiguration|注册AllowPostOptionsFilter|order={}", Integer.MIN_VALUE);
        return registration;
    }

    /**
     * CORS 跨域过滤器
     */
    @Bean
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
    public FilterRegistrationBean<CorsFilter> corsFilterRegistration() {
        FilterRegistrationBean<CorsFilter> registration =
                new FilterRegistrationBean<>(new CorsFilter());
        registration.setOrder(1000);
        registration.addUrlPatterns("/*");
        log.info("FmkWebAutoConfiguration|注册CorsFilter|order=1000");
        return registration;
    }

    /**
     * Trace 过滤器
     * 负责生成和传递 TraceId、SpanId
     */
    @Bean
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
    public FilterRegistrationBean<TraceFilter> traceFilterRegistration(@Autowired FmkTraceService fmkTraceService) {

        TraceFilter filter = new TraceFilter();
        filter.setFmkTraceService(fmkTraceService);

        FilterRegistrationBean<TraceFilter> registration = new FilterRegistrationBean<>(filter);
        registration.setOrder(1001);
        registration.addUrlPatterns("/*");
        log.info("FmkWebAutoConfiguration|注册TraceFilter|order=1001");
        return registration;
    }

    /**
     * 请求日志过滤器
     * 最低优先级，确保在所有过滤器之后执行
     */
    @Bean
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
    public FilterRegistrationBean<CommonsRequestLoggingFilter> logFilterRegistration() {
        CommonsRequestLoggingFilter logFilter = new CommonsRequestLoggingFilter();
        logFilter.setIncludeQueryString(true);
        logFilter.setIncludePayload(true);
        logFilter.setIncludeHeaders(true);
        logFilter.setMaxPayloadLength(10000);
        logFilter.setBeforeMessagePrefix("请求处理前: ");
        logFilter.setAfterMessagePrefix("请求处理后: ");

        FilterRegistrationBean<CommonsRequestLoggingFilter> registration =
                new FilterRegistrationBean<>(logFilter);
        registration.setOrder(Integer.MAX_VALUE);
        registration.addUrlPatterns("/*");
        log.info("FmkWebAutoConfiguration|注册CommonsRequestLoggingFilter|order={}", Integer.MAX_VALUE);
        return registration;
    }

    // ==================== 5. Jackson 序列化配置 ====================

    /**
     * Jackson 序列化定制
     * - Long 转 String（防止 JS 精度丢失）
     * - BigDecimal 格式化
     * - 空字符串转 null
     */
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jacksonCustomizer() {
        return builder -> {
            SimpleModule customModule = new SimpleModule("FmkCustomSerializationModule");
            customModule.addSerializer(Long.class, new LongToStringSerializer());
            customModule.addSerializer(long.class, new LongToStringSerializer());
            customModule.addSerializer(BigDecimal.class, new BigDecimalSerializer());
            customModule.addDeserializer(String.class, new EmptyStringToNullDeserializer());
            builder.modules(customModule);
            log.info("FmkWebAutoConfiguration|配置Jackson序列化器");
        };
    }

    /**
     * ObjectMapper Bean
     * 支持 Java 8 时间类型
     */
    @Bean
    public ObjectMapper objectMapper(Jackson2ObjectMapperBuilder builder) {
        ObjectMapper objectMapper = builder.createXmlMapper(false).build();
        objectMapper.registerModule(new JavaTimeModule());
        log.info("FmkWebAutoConfiguration|注册ObjectMapper");
        return objectMapper;
    }

    // ==================== 6. 线程池配置 ====================

    /**
     * 线程池执行器
     * 支持 TTL（TransmittableThreadLocal）上下文传递
     * 支持监控和指标收集
     */
    @Bean
    @ConditionalOnMissingBean(name = "fmkThreadPoolTaskExecutor")
    public ThreadPoolTaskExecutor fmkThreadPoolTaskExecutor(FmkThreadPoolProperties properties) {
        log.info("FmkWebAutoConfiguration|初始化线程池|properties={}", FmkJacksonUtil.toJson(properties));

        FmkTtlThreadPoolTaskExecutor executor = new FmkTtlThreadPoolTaskExecutor(
                properties.isEnableMonitoring(),
                properties.getMonitoringPeriod());

        executor.setCorePoolSize(properties.getCorePoolSize());
        executor.setMaxPoolSize(properties.getMaxPoolSize());
        executor.setQueueCapacity(properties.getQueueCapacity());
        executor.setKeepAliveSeconds(properties.getKeepAliveSeconds());
        executor.setThreadNamePrefix(properties.getThreadNamePrefix());
        executor.setAllowCoreThreadTimeOut(properties.isAllowCoreThreadTimeOut());
        executor.setRejectedExecutionHandler(
                createRejectionPolicy(properties.getRejectionPolicy(), executor));
        executor.setWaitForTasksToCompleteOnShutdown(
                properties.isWaitForTasksToCompleteOnShutdown());
        executor.setAwaitTerminationSeconds(properties.getAwaitTerminationSeconds());

        executor.initialize();

        if (properties.isEnableMonitoring()) {
            log.info("FmkWebAutoConfiguration|线程池监控已启用|period={}s",
                    properties.getMonitoringPeriod());
            executor.logMetrics();
        }

        return executor;
    }

    /**
     * 创建拒绝策略处理器
     */
    private RejectedExecutionHandler createRejectionPolicy(String rejectionPolicy, FmkTtlThreadPoolTaskExecutor executor) {

        RejectedExecutionHandler handler;

        switch (rejectionPolicy.toUpperCase()) {
            case "ABORT":
                handler = new ThreadPoolExecutor.AbortPolicy();
                log.info("FmkWebAutoConfiguration|线程池拒绝策略|AbortPolicy");
                break;
            case "DISCARD":
                handler = new ThreadPoolExecutor.DiscardPolicy();
                log.info("FmkWebAutoConfiguration|线程池拒绝策略|DiscardPolicy");
                break;
            case "DISCARD_OLDEST":
                handler = new ThreadPoolExecutor.DiscardOldestPolicy();
                log.info("FmkWebAutoConfiguration|线程池拒绝策略|DiscardOldestPolicy");
                break;
            case "CALLER_RUNS":
            default:
                handler = new ThreadPoolExecutor.CallerRunsPolicy();
                log.info("FmkWebAutoConfiguration|线程池拒绝策略|CallerRunsPolicy");
                break;
        }

        return new FmkTtlThreadPoolTaskExecutor.MonitoredRejectedExecutionHandler(
                handler, executor);
    }

    /**
     * 线程池监控服务
     */
    @Bean
    @ConditionalOnMissingBean(FmkThreadPoolMonitor.class)
    public FmkThreadPoolMonitor threadPoolMonitor(@Qualifier("fmkThreadPoolTaskExecutor") ThreadPoolTaskExecutor executor) {
        log.info("FmkWebAutoConfiguration|注册FmkThreadPoolMonitor");
        return new FmkThreadPoolMonitor(executor);
    }

    // ==================== 7. Token 模块配置 ====================

    /**
     * Token 模块启用配置
     * 当 fmk.token.enabled=true 时启用（默认启用）
     */
    @Configuration
    @ConditionalOnProperty(name = "fmk.token.enabled", havingValue = "true", matchIfMissing = true)
    public static class TokenEnabledConfiguration {
        public TokenEnabledConfiguration() {
            log.info("FmkWebAutoConfiguration|Token功能已启用");
        }
    }

    /**
     * Token Redis 存储配置
     * 当 fmk.token.type=redis 时启用
     */
    @Configuration
    @ConditionalOnProperty(name = "fmk.token.type", havingValue = "redis")
    public static class TokenRedisConfiguration {
        public TokenRedisConfiguration() {
            log.info("FmkWebAutoConfiguration|Token Redis存储已启用");
        }
    }

    /**
     * Token 内存存储配置
     * 当 fmk.token.type=memory 时启用（默认）
     */
    @Configuration
    @ConditionalOnProperty(name = "fmk.token.type", havingValue = "memory", matchIfMissing = true)
    public static class TokenMemoryConfiguration {
        public TokenMemoryConfiguration() {
            log.info("FmkWebAutoConfiguration|Token内存存储已启用");
        }
    }

}
