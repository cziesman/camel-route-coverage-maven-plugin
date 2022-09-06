package com.redhat.camel.route.coverage.model;

import lombok.Data;

@Data
public class TestResult {

    private Test test;

    private CamelContextRouteCoverage camelContextRouteCoverage;
}
