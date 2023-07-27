package com.example.one_vote_service.exception.handler;

import com.example.one_vote_service.common.ServiceErrors;
import com.example.one_vote_service.common.ServiceResponse;
import com.example.one_vote_service.exception.*;
import com.example.one_vote_service.utils.RandomUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Date;

@ControllerAdvice
@Slf4j
public class GenericExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ServiceResponse> handleException(Exception exception) {
        String requestId = RandomUtils.generateRandomString();
        log.error(String.format("requestId: %s, GenericException: %s", requestId, exception.getMessage()), exception);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ServiceResponse(requestId, new Date(), ServiceErrors.ERROR, exception.getMessage()));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ServiceResponse> handleValidationExceptions(HttpMessageNotReadableException ex) {
        String requestId = RandomUtils.generateRandomString();
        log.error(String.format("requestId: %s, HttpMessageNotReadableException: %s", requestId, ex.getMessage()), ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ServiceResponse(requestId, new Date(), ServiceErrors.BAD_REQUEST, ServiceErrors.BAD_REQUEST.getMessage()));
    }

    @ExceptionHandler(CustomExceptionError.class)
    public ResponseEntity<ServiceResponse> handleValidationCustomExceptionError(CustomException ex) {
        String requestId = RandomUtils.generateRandomString();
        log.warn(String.format("requestId: %s, CustomException: %s", requestId, ex.getMessage()));
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ServiceResponse(requestId, new Date(), ServiceErrors.ERROR.getCode(), ex.getMessage()));
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ServiceResponse> handleValidationExceptions(CustomException ex) {
        String requestId = RandomUtils.generateRandomString();
        log.warn(String.format("requestId: %s, CustomException: %s", requestId, ex.getMessage()));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ServiceResponse(requestId, new Date(), ServiceErrors.BAD_REQUEST.getCode(), ex.getMessage()));
    }

    @ExceptionHandler(AccessDeniedCustomException.class)
    public ResponseEntity<ServiceResponse> handleException(AccessDeniedCustomException ex) {
        String requestId = RandomUtils.generateRandomString();
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(new ServiceResponse(requestId, new Date(), ServiceErrors.FORBIDDEN.getCode(),
                        ex.getMessage()));
    }

    // TODO: Refresh Token hết hạn or không có
    @ExceptionHandler(TokenRefreshException.class)
    public ResponseEntity<ServiceResponse> handleTokenRefreshException(TokenRefreshException e) {
        System.out.println("aaa");
        String requestId = RandomUtils.generateRandomString();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ServiceResponse(requestId, new Date(), ServiceErrors.FORBIDDEN.getCode(), e.getMessage()));
    }

    // TODO: validate
    @ExceptionHandler(BindException.class)
    public ResponseEntity<ServiceResponse> handleBindException(BindException e) {
        String requestId = RandomUtils.generateRandomString();
        log.warn(String.format("requestId: %s, BindException: %s", requestId, e.getBindingResult().getAllErrors().get(0).getDefaultMessage()));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ServiceResponse(requestId, new Date(), ServiceErrors.BAD_REQUEST.getCode(), e.getBindingResult().getAllErrors().get(0).getDefaultMessage()));
    }

    // TODO: permission
    @ExceptionHandler(PermissionException.class)
    public ResponseEntity<ServiceResponse> handlePermissionException(PermissionException e) {
        String requestId = RandomUtils.generateRandomString();
        log.warn(String.format("requestId: %s, PermissionException: %s", requestId, e.getMessage()));
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ServiceResponse(requestId, new Date(), ServiceErrors.BAD_REQUEST.getCode(), e.getMessage()));
    }
}
