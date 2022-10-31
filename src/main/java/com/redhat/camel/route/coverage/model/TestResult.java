package com.redhat.camel.route.coverage.model;

public class TestResult {

    private Test test;

    private CamelContextRouteCoverage camelContextRouteCoverage;

    public Test getTest() {

        return test;
    }

    public void setTest(Test test) {

        this.test = test;
    }

    public CamelContextRouteCoverage getCamelContextRouteCoverage() {

        return camelContextRouteCoverage;
    }

    public void setCamelContextRouteCoverage(CamelContextRouteCoverage camelContextRouteCoverage) {

        this.camelContextRouteCoverage = camelContextRouteCoverage;
    }

    @Override
    public String toString() {

        return "TestResult{" +
                "test=" + test +
                ", camelContextRouteCoverage=" + camelContextRouteCoverage +
                '}';
    }
}
