package com.ranjan.services.gateway.configuration;

import com.ranjan.services.gateway.exception.CodedException;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.core.annotation.MergedAnnotation;
import org.springframework.core.annotation.MergedAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.reactive.function.server.ServerRequest;

import java.util.HashMap;
import java.util.Map;

import static org.apache.logging.log4j.util.Strings.EMPTY;
import static org.springframework.util.StringUtils.hasText;

@Component
public class CustomErrorAttributes extends DefaultErrorAttributes {

    @Override
    public Map<String, Object> getErrorAttributes(ServerRequest request, ErrorAttributeOptions options) {
        Map<String, Object> defaultErrorAttributes = super.getErrorAttributes(request, options);

        Throwable error = this.getError(request);

        MergedAnnotation<ResponseStatus> responseStatusMergedAnnotation = MergedAnnotations
                .from(error.getClass(), MergedAnnotations.SearchStrategy.TYPE_HIERARCHY).get(ResponseStatus.class);

        HttpStatus status = determineHttpStatus(error, responseStatusMergedAnnotation);

        Map<String, Object> customErrorAttributes = new HashMap<>();
        customErrorAttributes.put("status", status.value());
        customErrorAttributes.put("error", status.getReasonPhrase());
        customErrorAttributes.put("message", determineMessage(error, responseStatusMergedAnnotation));
        customErrorAttributes.put("path", defaultErrorAttributes.get("path"));
        customErrorAttributes.put("timestamp", defaultErrorAttributes.get("timestamp"));

        return customErrorAttributes;
    }

    private HttpStatus determineHttpStatus(Throwable error, MergedAnnotation<ResponseStatus> responseStatusAnnotation) {
        if (error instanceof CodedException) {
            return ((CodedException) error).getResponseCode();
        }
        return responseStatusAnnotation.getValue("code", HttpStatus.class).orElse(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private String determineMessage(Throwable error, MergedAnnotation<ResponseStatus> responseStatusAnnotation) {
        if (error instanceof CodedException) {
            return ((CodedException) error).getMessage();
        }

        String reason = responseStatusAnnotation.getValue("reason", String.class).orElse(EMPTY);
        if (hasText(reason)) {
            return reason;
        }
        return error.getMessage() != null ? error.getMessage() : EMPTY;
    }
}
