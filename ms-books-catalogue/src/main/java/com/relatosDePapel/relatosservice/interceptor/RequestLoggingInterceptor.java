package com.relatosDePapel.relatosservice.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Enumeration;

@Component
public class RequestLoggingInterceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(RequestLoggingInterceptor.class);

    @Value("${server.port}")
    private String serverPort;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        request.setAttribute("startTime", System.currentTimeMillis());

        StringBuilder params = new StringBuilder();
        Enumeration<String> paramNames = request.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String paramName = paramNames.nextElement();
            String paramValue = request.getParameter(paramName);
            params.append(paramName).append("=").append(paramValue).append("&");
        }

        String queryString = params.length() > 0 ? params.substring(0, params.length() - 1) : "none";

        log.info("📘 [CATALOGUE:{}] {} {} | Params: {}",
            serverPort,
            request.getMethod(),
            request.getRequestURI(),
            queryString
        );

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        long startTime = (Long) request.getAttribute("startTime");
        long duration = System.currentTimeMillis() - startTime;

        log.info("✅ [CATALOGUE:{}] {} {} | Status: {} | Duration: {}ms",
            serverPort,
            request.getMethod(),
            request.getRequestURI(),
            response.getStatus(),
            duration
        );
    }
}
