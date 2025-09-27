package com.cjlabs.web.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.FilterConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;

@Slf4j
@Order(1) // 最高优先级
@Component
public class CorsFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletResponse response = (HttpServletResponse) res;
        HttpServletRequest request = (HttpServletRequest) req;

        String method = request.getMethod();
        String uri = request.getRequestURI();

        Enumeration<String> headerNames = request.getHeaderNames();
        if (headerNames != null) {
            while (headerNames.hasMoreElements()) {
                String headerName = headerNames.nextElement();
                String headerValue = request.getHeader(headerName);
                log.info("CorsFilter|doFilter|Header|{}={}", headerName, headerValue);
            }
        }

        String origin = request.getHeader("Origin");
        String userAgent = request.getHeader("User-Agent");

        log.info("CorsFilter|doFilter|method={}|uri={}|origin={}|userAgent={}",
                method, uri, origin, userAgent);

        // 🔥 强制设置CORS头
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, OPTIONS, HEAD");
        response.setHeader("Access-Control-Allow-Headers", "*");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Credentials", "false");

        // 打印设置的响应头
        log.info("CorsFilter|doFilter|设置CORS头|Allow-Origin={}|Allow-Headers={}",
                response.getHeader("Access-Control-Allow-Origin"),
                response.getHeader("Access-Control-Allow-Headers"));

        // 🔥 对于OPTIONS请求，直接返回200
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            log.info("CorsFilter|doFilter|OPTIONS请求，直接返回200|uri={}", uri);
            response.setStatus(HttpServletResponse.SC_OK);
            // 确保响应头被写入
            response.flushBuffer();
            return;
        }

        log.info("CorsFilter|doFilter|非OPTIONS请求，继续处理|method={}|uri={}", method, uri);
        chain.doFilter(req, res);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("CorsFilter|init|CORS过滤器初始化");
    }

    @Override
    public void destroy() {
        log.info("CorsFilter|destroy|CORS过滤器销毁");
    }
} 