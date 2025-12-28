package com.cjlabs.web.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Set;

public class AllowPostOptionsFilter extends OncePerRequestFilter {

    // 允许的方法集合
    private static final Set<String> ALLOWED_METHODS = Set.of("POST", "OPTIONS");
    // GET / PUT / DELETE / PATCH / HEAD 等都会返回 405

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String method = request.getMethod().toUpperCase();

        if (!ALLOWED_METHODS.contains(method)) {
            response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED); // 405
            response.getWriter().write(method + " method is not allowed");
            // 不继续执行 Filter 链
            return;
        }

        // POST 或 OPTIONS 放行
        filterChain.doFilter(request, response);
    }
}
