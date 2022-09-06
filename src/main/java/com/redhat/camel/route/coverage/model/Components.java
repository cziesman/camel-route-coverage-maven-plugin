package com.redhat.camel.route.coverage.model;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Slf4j
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
}
