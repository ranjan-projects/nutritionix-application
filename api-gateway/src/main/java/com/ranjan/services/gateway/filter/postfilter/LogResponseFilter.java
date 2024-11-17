package com.ranjan.services.gateway.filter.postfilter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static com.ranjan.services.gateway.enums.ApiGatewayEnum.LOG_RESPONSE_FILTER;

@Component
public class LogResponseFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // Continue to the next filter in the chain, and then execute after the response is returned
        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            // Add a custom header to the response
            exchange.getResponse().getHeaders().add("X-Post-Filter", "Post Filter Applied");
            System.out.println("Post-filter executed");
        }));
    }

    @Override
    public int getOrder() {
        return LOG_RESPONSE_FILTER.getOrder();
    }
}
