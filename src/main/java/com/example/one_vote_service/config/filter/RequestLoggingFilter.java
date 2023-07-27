package com.example.one_vote_service.config.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
@Order(-1)
@Slf4j
public class RequestLoggingFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;

        // Ghi lại thông tin request
        String requestURI = request.getRequestURI();
        String method = request.getMethod();

        String requestBody = StreamUtils.copyToString(request.getInputStream(), StandardCharsets.UTF_8);

        log.info("API Request - [Method]: " + method + " , [URI]: " + requestURI + " , [REQUEST BODY]: " + requestBody);

        RequestWrapper customRequest = new RequestWrapper(request, requestBody);
        chain.doFilter(customRequest, servletResponse);

    }
}