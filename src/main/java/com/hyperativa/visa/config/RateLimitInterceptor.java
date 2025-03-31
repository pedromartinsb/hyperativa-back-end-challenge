package com.hyperativa.visa.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

public class RateLimitInterceptor implements HandlerInterceptor {

    private static final int MAX_REQUESTS_PER_MINUTE = 60;
    private static final String CACHE_CONTROL_HEADER = "Cache-Control";
    private static final String CACHE_CONTROL_VALUE = "public, max-age=60, s-maxage=60";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        response.setHeader(CACHE_CONTROL_HEADER, CACHE_CONTROL_VALUE);
        return true;
    }
} 