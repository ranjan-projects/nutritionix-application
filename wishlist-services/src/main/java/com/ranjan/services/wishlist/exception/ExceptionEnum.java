package com.ranjan.services.wishlist.exception;

import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;

public enum ExceptionEnum {

    WISHLIST_NOT_FOUND(NOT_FOUND, "2001", "Wishlist not found"),
    WISHLIST_ALREADY_EXIST(CONFLICT, "2002", "Wishlist already exist");

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
