package com.example.one_vote_service.config.filter;

import com.example.one_vote_service.common.ServiceError;
import com.example.one_vote_service.common.ServiceErrors;
import com.example.one_vote_service.common.ServiceResponse;
import com.example.one_vote_service.common.ServiceResponseMessage;
import com.example.one_vote_service.utils.RandomUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@ControllerAdvice
@Slf4j
@Order(0)
public class LoggingResponseBodyFilter implements ResponseBodyAdvice<Object> {
    private final ObjectMapper objectMapper;
    private final HttpServletRequest httpServletRequest;

    public LoggingResponseBodyFilter(@Qualifier("getObjectMapper") ObjectMapper objectMapper,
                                     HttpServletRequest httpServletRequest) {
        this.objectMapper = objectMapper;
        this.httpServletRequest = httpServletRequest;
    }

    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
        return true;
    }

    @SneakyThrows
    @Override
    public Object beforeBodyWrite(Object o, MethodParameter methodParameter, MediaType mediaType,
                                  Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest serverHttpRequest,
                                  ServerHttpResponse serverHttpResponse) {
        String requestId = RandomUtils.generateRandomString();

        if (methodParameter.getContainingClass().isAnnotationPresent(RestController.class)) {
            if (o == null) o = new Object();

            if (o.getClass() != ServiceResponse.class && o.getClass() != ServiceResponseMessage.class) {
                o = new ServiceResponse(requestId, new Date(), ServiceErrors.SUCCESS, o);
            } else if (o.getClass() == ServiceResponseMessage.class) {
                ServiceError errors = new ServiceError(0, ((ServiceResponseMessage) o).getMessage());
                o = new ServiceResponse(requestId, new Date(), errors, null);
            }
        }

        String data = "[REQUEST_ID]: " + requestId + " - " + "[BODY RESPONSE]: " + objectMapper.writeValueAsString(o);
        log.info(data);
        return o;
    }
}
