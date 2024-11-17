package com.ranjan.services.gateway.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GatewayFailureResponse {

    private LocalDateTime timestamp;
    private String correlationId;
    private String message;
    private int responseCode;

}
