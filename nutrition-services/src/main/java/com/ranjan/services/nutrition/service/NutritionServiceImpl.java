package com.ranjan.services.nutrition.service;

import com.ranjan.services.nutrition.exception.CodedException;
import com.ranjan.services.nutrition.model.NutritionResponse;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import static com.ranjan.services.nutrition.exception.ExceptionEnum.FOOD_NOT_FOUND;
import static org.springframework.util.CollectionUtils.isEmpty;

@Service
@Log
public class NutritionServiceImpl implements NutritionService {

    @Autowired
    private WebClient webClient;

    @Override
    public NutritionResponse searchNutrition(String food) {
        WebClient.ResponseSpec response = webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/search/instant")
                        .queryParam("query", food).build())
                .header("x-app-id", "32b36ce5")
                .header("x-app-key", "6d1dda011c4d5bb6b71bb5e940c982e1")
                .retrieve();

        NutritionResponse nutritionResponse = response.bodyToMono(NutritionResponse.class).block();
        log.info("Server response is: " + nutritionResponse);

        if (isEmpty(nutritionResponse.getBranded())) {
            throw new CodedException(FOOD_NOT_FOUND.getResponseCode(), FOOD_NOT_FOUND.getReasonCode(), FOOD_NOT_FOUND.getMessage());
        }
        return nutritionResponse;
    }

    private String response(String food) {
        String response = null;

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("x-app-id", "32b36ce5");
        httpHeaders.set("x-app-key", "6d1dda011c4d5bb6b71bb5e940c982e1");

        HttpEntity<String> entity = new HttpEntity<>(httpHeaders);

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<String> exchange = restTemplate.exchange("https://trackapi.nutritionix.com/v2/search/instant/?query=" + food, HttpMethod.GET, entity, String.class);

        response = exchange.getBody();
        log.info("Final response is: " + response);

        return response;
    }


}
