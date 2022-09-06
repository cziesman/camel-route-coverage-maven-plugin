package com.redhat.camel.route.coverage.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class Route {

    private String customId;

    private int exchangesTotal;

    private String id;

    private int totalProcessingTime;

    private Components components;

    private Map<String, Object> componentsMap = new HashMap<>();

    @JsonAnyGetter
    public Map<String, Object> getComponentsMap() {

        return componentsMap;
    }

    @JsonAnySetter
    public void setComponent(String name, Object value) {

        componentsMap.put(name, value);
    }
}
