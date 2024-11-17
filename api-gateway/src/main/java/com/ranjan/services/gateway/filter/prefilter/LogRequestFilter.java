package com.ranjan.services.gateway.filter.prefilter;

import lombok.extern.java.Log;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static com.ranjan.services.gateway.enums.ApiGatewayEnum.LOG_REQUEST_FILTER;

@Component
@Log
public class LogRequestFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        log.info("Server has received a request: " + exchange.getRequest().getBody());
        // Add a custom header to the request
        exchange.getRequest().mutate().header("X-Pre-Filter", "Pre Filter Applied").build();
        log.info("Pre-filter executed");

        // Continue to the next filter in the chain
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return LOG_REQUEST_FILTER.getOrder();
    }
}
