package com.ranjan.services.nutrition.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.ToString;

import java.util.LinkedHashMap;
import java.util.Map;

@Data
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Branded {

    public Integer brandType;
    public Integer servingQty;
    public Integer region;
    public String brandName;
    public String servingUnit;
    public String nixItemId;
    public String foodName;
    public String nixBrandId;
    public Integer nfCalories;
    public String brandNameItemName;
    public String locale;
    private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}