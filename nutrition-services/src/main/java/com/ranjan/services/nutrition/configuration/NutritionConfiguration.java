package com.ranjan.services.nutrition.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class NutritionConfiguration {

    @Value("${nutritionix.base.url}")
    private String nutritionixBaseUrl;

    @Bean
    public WebClient webClient() {
        return WebClient.builder().baseUrl(nutritionixBaseUrl).build();
    }

}
