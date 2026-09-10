package com.example.cruds.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
@Slf4j
public class MdcFilter extends OncePerRequestFilter {

    private static final String REQUEST_ID = "requestId";
    private static final String METHOD = "method";
    private static final String URI = "uri";

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        log.info("========== MDC FILTER START ==========");

        String requestId = request.getHeader("X-Request-Id");

        if (requestId == null || requestId.isBlank()) {
            requestId = UUID.randomUUID().toString();
        }

        try {
            MDC.put(REQUEST_ID, requestId);
            MDC.put(METHOD, request.getMethod());
            MDC.put(URI, request.getRequestURI());

            response.setHeader("X-Request-Id", requestId);

            log.info("Request started");

            filterChain.doFilter(request, response);

        } finally {
            MDC.clear();
        }
    }
}