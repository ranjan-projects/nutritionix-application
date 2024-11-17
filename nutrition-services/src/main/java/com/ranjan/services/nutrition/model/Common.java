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
public class Common {

    public Object commonType;
    public Integer servingQty;
    public String servingUnit;
    public Photo photo;
    public String tagId;
    public String tagName;
    public String foodName;
    public String locale;
    private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}