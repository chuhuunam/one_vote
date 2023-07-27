package com.example.one_vote_service.config.filter;

import com.example.one_vote_service.common.ServiceAttributes;
import com.example.one_vote_service.utils.RandomUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component
@Order(-1)
@Slf4j
public class LoggingRequestFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String requestId = RandomUtils.generateRandomString();
        httpServletRequest.setAttribute(ServiceAttributes.REQUEST_ID, requestId);
        long begin = System.currentTimeMillis();

        filterChain.doFilter(servletRequest, servletResponse);

        String responseInfoBuilder = "[REQUEST_ID]: " + requestId + " - [IN]: " +
                (System.currentTimeMillis() - begin) + " ms";
        log.info("[HTTP RESPONSE] ===> " + responseInfoBuilder);
    }

    private String getRemoteIp(HttpServletRequest httpServletRequest) {
        String remoteIp = httpServletRequest.getHeader("X-FORWARDED-FOR");
        if (StringUtils.isBlank(remoteIp)) {
            return httpServletRequest.getRemoteAddr();
        }
        return remoteIp;
    }
}
