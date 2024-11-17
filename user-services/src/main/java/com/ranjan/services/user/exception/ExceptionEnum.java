package com.ranjan.services.user.exception;

import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;

public enum ExceptionEnum {

    USER_NOT_FOUND(NOT_FOUND, "1001", "It looks like you're not registered with us. Please register yourself to continue."),
    USER_ALREADY_EXIST(CONFLICT, "1002", "Our records show that you're already registered. Please login to access your account.");

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
