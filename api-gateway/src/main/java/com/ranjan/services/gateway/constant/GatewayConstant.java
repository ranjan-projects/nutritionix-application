package com.ranjan.services.gateway.constant;

import com.ranjan.services.gateway.model.AuthSkipEndpoint;

import java.util.List;

import static org.springframework.http.HttpMethod.POST;

public class GatewayConstant {

    public static final List<AuthSkipEndpoint> AUTH_SKIP_ENDPOINTS = List.of(
            AuthSkipEndpoint.builder().path("/api/auth/token").httpMethod(POST).build(),
            AuthSkipEndpoint.builder().path("/api/users").httpMethod(POST).build()
    );

    private GatewayConstant() {
    }
}
