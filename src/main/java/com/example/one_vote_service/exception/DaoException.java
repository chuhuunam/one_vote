package com.example.one_vote_service.exception;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode(callSuper = true)
public class DaoException extends CustomException {
    @Getter
    private final String requestId;
    @Getter
    private final Integer code;

    public DaoException(String requestId, Integer code, String message) {
        super(message);
        this.requestId = requestId;
        this.code = code;
    }
}
