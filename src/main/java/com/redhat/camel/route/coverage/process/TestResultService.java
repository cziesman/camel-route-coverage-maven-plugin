package com.redhat.camel.route.coverage.process;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.redhat.camel.route.coverage.model.CamelContextRouteCoverage;
import com.redhat.camel.route.coverage.model.Components;
import com.redhat.camel.route.coverage.model.Route;
import com.redhat.camel.route.coverage.model.TestResult;
import com.redhat.camel.route.coverage.model.Routes;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

@Slf4j
public class TestResultService {

    private static final String FORMAT = "rte=%s, tot-ex=%d, test=%s";

    private final ObjectMapper objectMapper = new ObjectMapper().enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);

    public TestResult getResultsForTest(TestResult testResult) {

        CamelContextRouteCoverage camelContextRouteCoverage = testResult.getCamelContextRouteCoverage();

        Routes routes = camelContextRouteCoverage.getRoutes();

        List<Route> routeList = routes.getRouteList();

        routeList.forEach(route -> {

            Map<String, Object> componentsMap = route.getComponentsMap();

            try {
                String asString = objectMapper.writeValueAsString(componentsMap);
                Components components = objectMapper.readValue(asString, Components.class);
                route.setComponents(components);
            } catch (JsonProcessingException e) {
                LOG.error(e.getMessage(), e);
            }
        });

        LOG.debug(String.format(FORMAT, camelContextRouteCoverage.getId(), camelContextRouteCoverage.getExchangesTotal(), testResult.getTest().getClazz()));

        routeList.forEach(route -> {

            LOG.trace(String.format(FORMAT, route.getId(), route.getExchangesTotal(), testResult.getTest().getMethod()));
        });

        return testResult;
    }
}
