package com.example.one_vote_service.exception;

import com.example.one_vote_service.common.ServiceError;
import com.example.one_vote_service.common.ServiceErrors;
import lombok.Getter;

public class TokenRefreshException extends RuntimeException {
    @Getter
    private final ServiceError serviceError;

    public TokenRefreshException(String message) {
        super(message);
        serviceError = new ServiceError(ServiceErrors.ERROR.getCode(), message);
    }
}
