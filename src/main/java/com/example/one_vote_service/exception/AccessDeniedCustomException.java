package com.example.one_vote_service.exception;

import com.example.one_vote_service.common.ServiceError;
import lombok.Getter;

public class AccessDeniedCustomException extends RuntimeException {
    @Getter
    private final ServiceError serviceError;

    public AccessDeniedCustomException(ServiceError serviceError) {
        super(serviceError.getMessage());
        this.serviceError = serviceError;
    }
}
