package com.redhat.camel.route.coverage.process;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.redhat.camel.route.coverage.model.TestResult;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
public class XmlToCamelRouteCoverageConverter {

    private final ObjectMapper OBJECT_MAPPER = new ObjectMapper().enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);

    private final XmlMapper XML_MAPPER = new XmlMapper();

    public TestResult convert(String source) {

        Map<String, Object> map;

        try {
            map = XML_MAPPER.readValue(source, Map.class);

            String json = OBJECT_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(map);

            return OBJECT_MAPPER.readValue(json, TestResult.class);
        } catch (JsonProcessingException ex) {
            LOG.error(ex.getMessage(), ex);

            return null;
        }
    }
}
