package com.redhat.camel.route.coverage.model;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Components {

    private Map<String, List<EipAttribute>> attributeMap = new HashMap<>();

    @JsonAnySetter
    public void setAttribute(String key, Object value) throws JsonProcessingException {

        List<EipAttribute> listValue;

        ObjectMapper objectMapper = new ObjectMapper().enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        if (value instanceof String) {
            EipAttribute eipAttribute = objectMapper.readValue(String.format("{\"%s\":\"%s\"}", key, value), EipAttribute.class);
            listValue = Collections.singletonList(eipAttribute);
        } else if (!(value instanceof List)) {
            String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(value);
            EipAttribute eipAttribute = objectMapper.readValue(json, EipAttribute.class);
            listValue = Collections.singletonList(eipAttribute);
        } else {
            String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(value);
            listValue = objectMapper.readValue(json, new TypeReference<List<EipAttribute>>() {
            });
        }

        attributeMap.put(key, listValue);
    }

    public Map<String, List<EipAttribute>> getAttributeMap() {

        return attributeMap;
    }

    public void setAttributeMap(Map<String, List<EipAttribute>> attributeMap) {

        this.attributeMap = attributeMap;
    }

    @Override
    public String toString() {

        return "Components{" +
                "attributeMap=" + attributeMap +
                '}';
    }
}
