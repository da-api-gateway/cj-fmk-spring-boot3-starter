package com.cjlabs.web.filter;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * CORS跨域过滤器
 * 处理跨域请求，设置CORS响应头
 */
@Slf4j
public class CorsFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletResponse response = (HttpServletResponse) res;
        HttpServletRequest request = (HttpServletRequest) req;

        String method = request.getMethod();
        String uri = request.getRequestURI();
        String origin = request.getHeader("Origin");
        String userAgent = request.getHeader("User-Agent");

        if (log.isDebugEnabled()) {
            log.debug("CorsFilter|doFilter|请求信息|method={}|uri={}|origin={}|userAgent={}",
                    method, uri, origin, userAgent);
        }

        // 设置CORS头
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD");
        response.setHeader("Access-Control-Allow-Headers", "*");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Credentials", "false");

        // 对于OPTIONS请求，直接返回200
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            if (log.isDebugEnabled()) {
                log.debug("CorsFilter|doFilter|OPTIONS预检请求，直接返回200|uri={}", uri);
            }
            response.setStatus(HttpServletResponse.SC_OK);
            response.flushBuffer();
            return;
        }

        // 非OPTIONS请求，继续处理
        chain.doFilter(req, res);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("CorsFilter|init|初始化");
    }

    @Override
    public void destroy() {
        log.info("CorsFilter|destroy|销毁");
    }
}