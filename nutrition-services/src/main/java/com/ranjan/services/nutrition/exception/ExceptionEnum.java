package com.ranjan.services.nutrition.exception;

import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.NOT_FOUND;

public enum ExceptionEnum {

    FOOD_NOT_FOUND(NOT_FOUND, "5001", "No food item found for the search criteria");

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
