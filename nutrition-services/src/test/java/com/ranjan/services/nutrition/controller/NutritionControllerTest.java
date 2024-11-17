package com.ranjan.services.nutrition.controller;

import com.ranjan.services.nutrition.exception.CodedException;
import com.ranjan.services.nutrition.model.Common;
import com.ranjan.services.nutrition.model.NutritionResponse;
import com.ranjan.services.nutrition.service.NutritionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class NutritionControllerTest {

    @InjectMocks
    private NutritionController nutritionController;

    @Mock
    private NutritionService nutritionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize mocks
    }

    @Test
    void testSearchNutrition_Success() {
        // Arrange
        String searchCriteria = "apple";
        NutritionResponse mockResponse = new NutritionResponse();
        Common common = new Common();
        common.setFoodName("Apple");

        mockResponse.setCommon(List.of(common));
        when(nutritionService.searchNutrition(searchCriteria)).thenReturn(mockResponse);

        // Act
        ResponseEntity<NutritionResponse> responseEntity = nutritionController.searchNutrition(searchCriteria);

        // Assert
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Apple", responseEntity.getBody().getCommon().get(0).getFoodName());

        verify(nutritionService, times(1)).searchNutrition(searchCriteria);
    }

    @Test
    void testSearchNutrition_Failure() {
        // Arrange
        String searchCriteria = "invalid_food";
        CodedException exception = new CodedException(HttpStatus.NOT_FOUND, "404", "Food not found");

        when(nutritionService.searchNutrition(searchCriteria)).thenThrow(exception);

        // Act
        CodedException thrownException = assertThrows(CodedException.class, () -> {
            nutritionController.searchNutrition(searchCriteria);
        });

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, thrownException.getResponseCode());
        assertEquals("404", thrownException.getReasonCode());
        assertEquals("Food not found", thrownException.getMessage());

        verify(nutritionService, times(1)).searchNutrition(searchCriteria);
    }

    @Test
    void testHandleCodedException() {
        // Arrange
        CodedException exception = new CodedException(HttpStatus.BAD_REQUEST, "400", "Bad request");

        // Act
        ResponseEntity<CodedException> responseEntity = nutritionController.handleCodedException(exception);

        // Assert
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("400", responseEntity.getBody().getReasonCode());
        assertEquals("Bad request", responseEntity.getBody().getMessage());
    }
}
