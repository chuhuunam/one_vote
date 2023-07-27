package com.example.one_vote_service.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class CustomExceptionError extends RuntimeException {
    public CustomExceptionError(String message) {
        super(message);
    }
}
