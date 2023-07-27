package com.example.one_vote_service.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.util.Date;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ResponseEntity {
    private String requestId;
    private String at;
    private ServiceError error;
    private Object data;

    public ResponseEntity(String requestId, Date at, ServiceError error, Object data) {
        this.requestId = requestId;
        this.at = DateFormatUtils.format(at, ServiceConstant.DATETIME_FORMAT);
        this.error = error;
        this.data = data;
    }

    public ResponseEntity(String requestId, Date at, ServiceError error, Object data, Exception ex) {
        this.requestId = requestId;
        this.at = DateFormatUtils.format(at, ServiceConstant.DATETIME_FORMAT);
        this.error = error;
        this.data = data;
    }

    public ResponseEntity(String requestId, Date at, ServiceError error) {
        this.requestId = requestId;
        this.at = DateFormatUtils.format(at, ServiceConstant.DATETIME_FORMAT);
        this.error = error;
    }

    public ResponseEntity(String requestId, Object data) {
        this.requestId = requestId;
        this.at = DateFormatUtils.format(new Date(), ServiceConstant.DATETIME_FORMAT);
        this.error = ServiceErrors.SUCCESS;
        this.data = data;
    }

    public ResponseEntity(String requestId, Date at, ServiceError error, Exception ex) {
        this.requestId = requestId;
        this.at = DateFormatUtils.format(at, ServiceConstant.DATETIME_FORMAT);
        this.error = error;
        this.data = (ex == null ? null : ex.getMessage());
    }

    public ResponseEntity(String requestId, Date at, Integer code, String message) {
        this.requestId = requestId;
        this.at = DateFormatUtils.format(at, ServiceConstant.DATETIME_FORMAT);
        this.error = new ServiceError(code, message);
    }

    public ResponseEntity(String requestId, Date at, Integer code, String message, Exception ex) {
        this.requestId = requestId;
        this.at = DateFormatUtils.format(at, ServiceConstant.DATETIME_FORMAT);
        this.error = new ServiceError(code, message);
        this.data = (ex == null ? null : ex.getMessage());
    }

    public ResponseEntity(String requestId, Date at, Integer code, String message, Object data) {
        this.requestId = requestId;
        this.at = DateFormatUtils.format(at, ServiceConstant.DATETIME_FORMAT);
        this.error = new ServiceError(code, message);
        this.data = data;
    }

    public ResponseEntity(String requestId, Date at, Integer code, String message, Object data, Exception ex) {
        this.requestId = requestId;
        this.at = DateFormatUtils.format(at, ServiceConstant.DATETIME_FORMAT);
        this.error = new ServiceError(code, message);
        this.data = data;
    }


}
