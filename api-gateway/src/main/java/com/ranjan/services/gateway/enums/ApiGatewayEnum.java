package com.ranjan.services.gateway.enums;

public enum ApiGatewayEnum {

    LOG_REQUEST_FILTER(1),
    CORRELATION_ID_FILTER(2),
    AUTHENTICATION_FILTER(3),
    AUTHORISATION_FILTER(4),

    LOG_RESPONSE_FILTER(0),
    MODIFY_RESPONSE_FILTER(-1);


    private int order;

    ApiGatewayEnum(int order) {
        this.order = order;
    }

    public int getOrder() {
        return order;
    }
}
