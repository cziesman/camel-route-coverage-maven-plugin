package com.redhat.camel.route.coverage.process;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.redhat.camel.route.coverage.model.CamelContextRouteCoverage;
import com.redhat.camel.route.coverage.model.Components;
import com.redhat.camel.route.coverage.model.Route;
import com.redhat.camel.route.coverage.model.Routes;
import com.redhat.camel.route.coverage.model.TestResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

public class TestResultParser {

    private static final Logger LOG = LoggerFactory.getLogger(TestResultParser.class);

    private static final String FORMAT = "rte=%s, tot-ex=%d, test=%s";

    private final ObjectMapper objectMapper = new ObjectMapper().enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);

    public TestResult parse(TestResult testResult) {

        CamelContextRouteCoverage camelContextRouteCoverage = testResult.getCamelContextRouteCoverage();

        Routes routes = camelContextRouteCoverage.getRoutes();

        List<Route> routeList = routes.getRouteList();

        routeList.forEach(route -> {

            route.setComponents(components(route));
        });

        LOG.debug(String.format(FORMAT, camelContextRouteCoverage.getId(), camelContextRouteCoverage.getExchangesTotal(), testResult.getTest().getClazz()));

        routeList.forEach(route -> {

            LOG.trace(String.format(FORMAT, route.getId(), route.getExchangesTotal(), testResult.getTest().getMethod()));
        });

        return testResult;
    }

    protected Components components(Route route) {

        Components components = null;

        try {
            Map<String, Object> componentsMap = route.getComponentsMap();
            String asString = objectMapper().writeValueAsString(componentsMap);
            components = objectMapper().readValue(asString, Components.class);
        } catch (JsonProcessingException e) {
            LOG.error(e.getMessage(), e);
        }

        return components;
    }

    /*
     * Provides a convenience method to facilitate unit testing.
     */
    protected ObjectMapper objectMapper() {

        return objectMapper;
    }
}
