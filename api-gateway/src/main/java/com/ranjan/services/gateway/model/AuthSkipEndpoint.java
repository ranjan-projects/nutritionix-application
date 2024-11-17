package com.ranjan.services.gateway.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpMethod;

import java.util.Objects;

@Data
@Builder
public class AuthSkipEndpoint {

    private String path;
    private HttpMethod httpMethod;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuthSkipEndpoint that = (AuthSkipEndpoint) o;
        return Objects.equals(path, that.path) && Objects.equals(httpMethod, that.httpMethod);
    }

    @Override
    public int hashCode() {
        return Objects.hash(path, httpMethod);
    }
}
