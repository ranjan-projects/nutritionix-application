package com.ranjan.services.gateway.exception;

import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

public enum ExceptionEnum {

    UNAUTHORISED_USER(UNAUTHORIZED, "1001", "Unauthorised user"),
    INVALID_TOKEN(BAD_REQUEST, "1002", "Invalid Token");

    private HttpStatus responseCode;
    private String reasonCode;
    private String message;

    ExceptionEnum(HttpStatus responseCode, String reasonCode, String message) {
        this.responseCode = responseCode;
        this.reasonCode = reasonCode;
        this.message = message;
    }

    public HttpStatus getResponseCode() {
        return responseCode;
    }

    public String getReasonCode() {
        return reasonCode;
    }

    public String getMessage() {
        return message;
    }
}
