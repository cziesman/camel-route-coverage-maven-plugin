package com.redhat.camel.route.coverage.model;

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

    public int getTotalEips() {

        return totalEips;
    }

    public void setTotalEips(int totalEips) {

        this.totalEips = totalEips;
    }

    public int getTotalEipsTested() {

        return totalEipsTested;
    }

    public void setTotalEipsTested(int totalEipsTested) {

        this.totalEipsTested = totalEipsTested;
    }

    public int getTotalProcessingTime() {

        return totalProcessingTime;
    }

    public void setTotalProcessingTime(int totalProcessingTime) {

        this.totalProcessingTime = totalProcessingTime;
    }

    public int getCoverage() {

        return coverage;
    }

    public void setCoverage(int coverage) {

        this.coverage = coverage;
    }

    @Override
    public String toString() {

        return "RouteTotalsStatistic{" +
                "totalEips=" + totalEips +
                ", totalEipsTested=" + totalEipsTested +
                ", totalProcessingTime=" + totalProcessingTime +
                ", coverage=" + coverage +
                '}';
    }
}
