package com.example.one_vote_service.common;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@Data
@ToString
@NoArgsConstructor
public class ServiceResponse {
    private String requestId;
    private Date at;
    private ServiceError error;
    private Object data;

    public ServiceResponse(String requestId, Date at, ServiceError error) {
        this.requestId = requestId;
        this.at = at;
        this.error = error;
    }

    public ServiceResponse(String requestId, Date at, ServiceError error, Object data) {
        this.requestId = requestId;
        this.at = at;
        this.error = error;
        this.data = data;
    }

    public ServiceResponse(String requestId, Date at, int code, String message) {
        this.requestId = requestId;
        this.at = at;
        this.error = new ServiceError(code, message);
    }

    public ServiceResponse(String requestId, Object data) {
        this.requestId = requestId;
        this.at = new Date();
        this.error = ServiceErrors.SUCCESS;
        this.data = data;
    }
}
