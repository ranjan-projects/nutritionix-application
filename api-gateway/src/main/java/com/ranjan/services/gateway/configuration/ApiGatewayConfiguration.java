package com.ranjan.services.gateway.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApiGatewayConfiguration {

    @Value("${filter.api.call.timeout}")
    private int apiCallTimeout;

}
