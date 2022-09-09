package com.redhat.camel.route.coverage.process;

import com.redhat.camel.route.coverage.model.CamelContextRouteCoverage;
import com.redhat.camel.route.coverage.model.Route;
import com.redhat.camel.route.coverage.model.Routes;
import com.redhat.camel.route.coverage.model.Test;
import com.redhat.camel.route.coverage.model.TestResult;

import java.util.Collections;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

public class TestUtil {

    public static TestResult testResult() {

        TestResult result = new TestResult();

        result.setTest(test());
        result.setCamelContextRouteCoverage(camelContextRouteCoverage());

        return result;
    }

    public static Test test() {

        Test result = new Test();

        result.setClazz("some_class");
        result.setMethod("some_method");

        return result;
    }

    public static CamelContextRouteCoverage camelContextRouteCoverage() {

        CamelContextRouteCoverage result = new CamelContextRouteCoverage();

        result.setExchangesTotal(3);
        result.setId("some_route_coverage");
        result.setTotalProcessingTime(10);
        result.setRoutes(routes());

        return result;
    }

    public static Routes routes() {

        Routes result = new Routes();

        result.setRouteList(Collections.singletonList(route()));

        return result;
    }

    public static Route route() {

        Route result = new Route();

        result.setCustomId("true");
        result.setId("greetings-route");
        result.setExchangesTotal(3);
        result.setTotalProcessingTime(15);
        result.setComponentsMap(componentsMap());

        return result;
    }

    public static Map<String, Object> componentsMap() {

        Map<String, Object> result = new TreeMap<>();

        Properties properties = new Properties();
        properties.put("uri", "direct:greetings");
        properties.put("index", 0);
        properties.put("exchangesTotal", 1);
        properties.put("totalProcessingTime", 15);
        result.put("from", properties);

        properties = new Properties();
        properties.put("id", "setBody2");
        properties.put("constant", "Hello from Cloud Cuckoo Camel Land!");
        properties.put("index", 24);
        properties.put("exchangesTotal", 1);
        properties.put("totalProcessingTime", 6);
        result.put("setBody", properties);

        properties = new Properties();
        properties.put("id", "to7");
        properties.put("uri", "file:target/output");
        properties.put("index", 25);
        properties.put("exchangesTotal", 1);
        properties.put("totalProcessingTime", 4);
        result.put("to", properties);

        return result;
    }
}
