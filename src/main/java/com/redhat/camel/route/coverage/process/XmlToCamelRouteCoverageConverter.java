package com.redhat.camel.route.coverage.process;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.redhat.camel.route.coverage.model.TestResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class XmlToCamelRouteCoverageConverter {

    private static final Logger LOG = LoggerFactory.getLogger(XmlToCamelRouteCoverageConverter.class);

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
