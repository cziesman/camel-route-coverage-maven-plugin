package com.redhat.camel.route.coverage.model;

import lombok.Data;

@Data
public class RouteTotalsStatistic {

    private int totalEips;

    private int totalEipsTested;

    private int totalProcessingTime;

    private int coverage;

    public void incrementTotalEips(int totalEips) {

        this.totalEips += totalEips;
        calculateCoverage();
    }

    public void incrementTotalEipsTested(int totalEipsTested) {

        this.totalEipsTested += totalEipsTested;
        calculateCoverage();
    }

    public void incrementTotalProcessingTime(int processingTime) {

        totalProcessingTime += processingTime;
    }

    protected void calculateCoverage() {

        if (totalEips > 0) {
            coverage = (100 * totalEipsTested) / totalEips;
        }
    }
}
