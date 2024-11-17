package com.ranjan.services.nutrition.controller;

import com.ranjan.services.nutrition.exception.CodedException;
import com.ranjan.services.nutrition.model.NutritionResponse;
import com.ranjan.services.nutrition.service.NutritionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/nutrition")
@Tag(name = "Nutrition Services", description = "Operations related to nutrition")
public class NutritionController {

    @Autowired
    private NutritionService nutritionService;

    @GetMapping("/search/{searchCriteria}")
    @Operation(summary = "Search nutrition", description = "Search nutrition based on search criteria")
    public ResponseEntity<NutritionResponse> searchNutrition(@PathVariable String searchCriteria) {
        return ResponseEntity.ok(nutritionService.searchNutrition(searchCriteria));
    }

    @ExceptionHandler(CodedException.class)
    public ResponseEntity<CodedException> handleCodedException(CodedException ex) {
        return new ResponseEntity<>(ex, ex.getResponseCode());
    }
}
