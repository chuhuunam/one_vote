package com.example.one_vote_service.exception;

import com.example.one_vote_service.common.ServiceError;
import lombok.Getter;

public class BindException extends RuntimeException {
    @Getter
    private final ServiceError serviceError;

    public BindException(ServiceError serviceError) {
        super(serviceError.getMessage());
        this.serviceError = serviceError;
    }
}
