package com.example.one_vote_service.exception;

import com.example.one_vote_service.common.ServiceError;
import com.example.one_vote_service.common.ServiceErrors;
import lombok.Getter;

public class PermissionException extends RuntimeException {
    @Getter
    private final ServiceError serviceError;

    public PermissionException(String message) {
        super(message);
        serviceError = new ServiceError(ServiceErrors.ERROR.getCode(), message);
    }
}
