package com.ranjan.services.gateway.filter.prefilter;

import com.ranjan.services.gateway.exception.CodedException;
import com.ranjan.services.gateway.model.AuthResponse;
import com.ranjan.services.gateway.model.AuthSkipEndpoint;
import io.netty.util.internal.StringUtil;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.*;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static com.ranjan.services.gateway.constant.GatewayConstant.AUTH_SKIP_ENDPOINTS;
import static com.ranjan.services.gateway.enums.ApiGatewayEnum.AUTHENTICATION_FILTER;
import static com.ranjan.services.gateway.exception.ExceptionEnum.INVALID_TOKEN;
import static com.ranjan.services.gateway.exception.ExceptionEnum.UNAUTHORISED_USER;
import static org.apache.hc.client5.http.auth.StandardAuthScheme.BEARER;
import static org.apache.http.HttpHeaders.AUTHORIZATION;
import static org.apache.logging.log4j.util.Strings.isBlank;

@Component
@Log
public class AuthenticationFilter implements GlobalFilter, Ordered {

    @Value("${auth.service.url}")
    private String authServiceUrl;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("Authenticating user " + exchange.getRequest().getBody());

        String path = exchange.getRequest().getPath().value();
        HttpMethod method = exchange.getRequest().getMethod();
        log.info("Request path is " + path);

        AuthSkipEndpoint requestEndpoint = AuthSkipEndpoint.builder().path(path).httpMethod(method).build();
        if (AUTH_SKIP_ENDPOINTS.contains(requestEndpoint) ||
                path.contains("/api/nutrition/search/")) {
            log.info("Path is in the skip list");
            return chain.filter(exchange);
        }

        String token = extractToken(exchange.getRequest().getHeaders());

        log.info("Extracted token is: " + token);
        if (isBlank(token)) {
            throw new CodedException(UNAUTHORISED_USER.getResponseCode(), UNAUTHORISED_USER.getReasonCode(), UNAUTHORISED_USER.getMessage());
        }

        AuthResponse authResponse = validateToken(token);
        ServerHttpRequest request = exchange.getRequest().mutate()
                .header("username", authResponse.getClaims().get("username"))
                .build();

        ServerWebExchange updatedExchange = exchange.mutate()
                .request(request).build();

        log.info("Auth response is: " + authResponse);

        log.info("Pre-filter executed");

        // Continue to the next filter in the chain
        return chain.filter(updatedExchange);
    }

    private AuthResponse validateToken(String token) {
        AuthResponse response;

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set(HttpHeaders.AUTHORIZATION, token);

        HttpEntity<String> entity = new HttpEntity<>(httpHeaders);

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<AuthResponse> exchange = restTemplate.exchange(authServiceUrl + "/api/auth/token/validate", HttpMethod.GET, entity, AuthResponse.class);

        response = exchange.getBody();
        log.info("Auth response is: " + response);

        return response;
    }

    @Override
    public int getOrder() {
        return AUTHENTICATION_FILTER.getOrder();
    }

    private String extractToken(HttpHeaders headers) {
        String bearerToken = headers.get(AUTHORIZATION) != null ? headers.getFirst(AUTHORIZATION) : null;

        log.info("Bearer token is:" + bearerToken);
        if (isBlank(bearerToken) || !bearerToken.startsWith(BEARER + StringUtil.SPACE)) {
            throw new CodedException(INVALID_TOKEN.getResponseCode(), INVALID_TOKEN.getReasonCode(), INVALID_TOKEN.getMessage());
        }
        return bearerToken.substring(BEARER.length()).trim();
    }

    private Mono<Throwable> handleErrorResponse(ClientResponse clientResponse) {
        return clientResponse.bodyToMono(String.class)
                .flatMap(responseBody -> {
                    int statusCode = clientResponse.statusCode().value();
                    // Log the status code and response body
                    System.err.println("Status code: " + statusCode);
                    System.err.println("Response body: " + responseBody);
                    // Return a custom exception or any other handling mechanism
                    return Mono.error(new CodedException(HttpStatus.valueOf(statusCode), null, responseBody));
                });
    }

}
