package com.ranjan.services.nutrition.service;

import com.ranjan.services.nutrition.exception.CodedException;
import com.ranjan.services.nutrition.model.Branded;
import com.ranjan.services.nutrition.model.NutritionResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.function.Function;

import static com.ranjan.services.nutrition.exception.ExceptionEnum.FOOD_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class NutritionServiceImplTest {

    @InjectMocks
    private NutritionServiceImpl nutritionService;

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @Mock
    private RestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize mocks
    }

    @Test
    void testSearchNutrition_Success() {
        // Arrange
        String food = "apple";
        NutritionResponse mockResponse = new NutritionResponse();

        Branded branded = new Branded();
        branded.setFoodName("Apple Juice");

        mockResponse.setBranded(List.of(branded)); // Simulate a valid response

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(Function.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.header(anyString(), anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(NutritionResponse.class)).thenReturn(Mono.just(mockResponse));

        // Act
        NutritionResponse result = nutritionService.searchNutrition(food);

        // Assert
        assertNotNull(result);
        assertFalse(result.getBranded().isEmpty());
        assertEquals("Apple Juice", result.getBranded().get(0).getFoodName());

        verify(webClient, times(1)).get();
    }

    @Test
    void testSearchNutrition_Failure() {
        // Arrange
        String food = "invalid_food";
        NutritionResponse mockResponse = new NutritionResponse();
        mockResponse.setBranded(List.of()); // Empty response, simulating no food found

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(Function.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.header(anyString(), anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(NutritionResponse.class)).thenReturn(Mono.just(mockResponse));

        // Act & Assert
        CodedException exception = assertThrows(CodedException.class, () -> nutritionService.searchNutrition(food));
        assertEquals(FOOD_NOT_FOUND.getResponseCode(), exception.getResponseCode());
        assertEquals(FOOD_NOT_FOUND.getReasonCode(), exception.getReasonCode());
        assertEquals(FOOD_NOT_FOUND.getMessage(), exception.getMessage());

        verify(webClient, times(1)).get();
    }

}
