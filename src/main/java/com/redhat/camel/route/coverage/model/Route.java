package com.redhat.camel.route.coverage.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;

import java.util.HashMap;
import java.util.Map;

public class Route {

    private String customId;

    private int exchangesTotal;

    private String id;

    private int totalProcessingTime;

    private Components components;

    private Map<String, Object> componentsMap = new HashMap<>();

    @JsonAnySetter
    public void setComponent(String name, Object value) {

        componentsMap.put(name, value);
    }

    public String getCustomId() {

        return customId;
    }

    public void setCustomId(String customId) {

        this.customId = customId;
    }

    public int getExchangesTotal() {

        return exchangesTotal;
    }

    public void setExchangesTotal(int exchangesTotal) {

        this.exchangesTotal = exchangesTotal;
    }

    public String getId() {

        return id;
    }

    public void setId(String id) {

        this.id = id;
    }

    public int getTotalProcessingTime() {

        return totalProcessingTime;
    }

    public void setTotalProcessingTime(int totalProcessingTime) {

        this.totalProcessingTime = totalProcessingTime;
    }

    public Components getComponents() {

        return components;
    }

    public void setComponents(Components components) {

        this.components = components;
    }

    @JsonAnyGetter
    public Map<String, Object> getComponentsMap() {

        return componentsMap;
    }

    public void setComponentsMap(Map<String, Object> componentsMap) {

        this.componentsMap = componentsMap;
    }

    @Override
    public String toString() {

        return "Route{" +
                "customId='" + customId + '\'' +
                ", exchangesTotal=" + exchangesTotal +
                ", id='" + id + '\'' +
                ", totalProcessingTime=" + totalProcessingTime +
                ", components=" + components +
                ", componentsMap=" + componentsMap +
                '}';
    }
}
