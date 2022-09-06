package com.redhat.camel.route.coverage.model;

import lombok.Data;

@Data
public class CamelContextRouteCoverage {

    private String id;

    private int exchangesTotal;

    private int totalProcessingTime;

    private Routes routes;
}
