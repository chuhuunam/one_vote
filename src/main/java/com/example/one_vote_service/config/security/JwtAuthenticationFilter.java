package com.example.one_vote_service.config.security;

import com.example.one_vote_service.common.ServiceResponse;
import com.example.one_vote_service.config.constant.MessageCode;
import com.example.one_vote_service.exception.AccessDeniedCustomException;
import com.example.one_vote_service.service.AuthService;
import com.example.one_vote_service.utils.RandomUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

@Component
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenProvider tokenProvider;
    private final ObjectMapper objectMapper;
    private final AuthService authService;

    public JwtAuthenticationFilter(
            JwtTokenProvider tokenProvider,
            @Qualifier("getObjectMapper") ObjectMapper objectMapper,
            AuthService authService
    ) {
        this.tokenProvider = tokenProvider;
        this.objectMapper = objectMapper;
        this.authService = authService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String requestId = RandomUtils.generateRandomString();

        String jwt = getJwtFromRequest(request);

        if (isPermitted(request) && !StringUtils.hasText(jwt)) {
            filterChain.doFilter(request, response);
            return;
        }

        AccountDetail accountDetail;
        try {
            if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
                Integer userId = tokenProvider.getUserId(jwt);
                accountDetail = authService.findById(userId, jwt);
            } else {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.setCharacterEncoding("UTF-8");
                response.setContentType("application/json");
                response.getWriter().println(objectMapper.writeValueAsString(
                        new ServiceResponse(requestId, new Date(), HttpStatus.UNAUTHORIZED.value(), MessageCode.LOGIN_NOT_UNAUTHORIZED))
                );
                return;
            }
        } catch (AccessDeniedCustomException ex) {
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            response.getWriter().println(
                    objectMapper.writeValueAsString(
                            new ServiceResponse(
                                    requestId,
                                    new Date(),
                                    HttpStatus.FORBIDDEN.value(),
                                    ex.getMessage()
                            )
                    )
            );
            return;
        } catch (Exception ex) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            response.getWriter().println(objectMapper.writeValueAsString(
                    new ServiceResponse(requestId, new Date(), HttpStatus.UNAUTHORIZED.value(), MessageCode.LOGIN_NOT_UNAUTHORIZED))
            );
            return;
        }

        if (accountDetail == null) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            response.getWriter().println(objectMapper.writeValueAsString(
                    new ServiceResponse(requestId, new Date(), HttpStatus.FORBIDDEN.value(), MessageCode.LOGIN_NOT_UNAUTHORIZED))
            );
            return;
        }

        log.debug(String.format("requestId: %s, authentication principal: id: %s, userName: %s",
                requestId, accountDetail.getId(), accountDetail.getPhone()));

        UsernamePasswordAuthenticationToken
                authentication = new UsernamePasswordAuthenticationToken(accountDetail, null, accountDetail.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }

    private boolean isPermitted(HttpServletRequest request) {
        return request.getServletPath().contains("swagger")
                || request.getServletPath().contains("/auth/login")
                || request.getServletPath().contains("/auth/register")
                || request.getServletPath().contains("/auth/refresh-token")
                || request.getServletPath().contains("/file/content")
                || request.getServletPath().contains("/swagger")
                || request.getServletPath().contains("/api-docs")
                || request.getServletPath().contains("/files/*")
                || request.getServletPath().contains("/oauth2/token");
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}

