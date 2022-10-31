package com.redhat.camel.route.coverage.model;

public class CamelContextRouteCoverage {

    private String id;

    private int exchangesTotal;

    private int totalProcessingTime;

    private Routes routes;

    public String getId() {

        return id;
    }

    public void setId(String id) {

        this.id = id;
    }

    public int getExchangesTotal() {

        return exchangesTotal;
    }

    public void setExchangesTotal(int exchangesTotal) {

        this.exchangesTotal = exchangesTotal;
    }

    public int getTotalProcessingTime() {

        return totalProcessingTime;
    }

    public void setTotalProcessingTime(int totalProcessingTime) {

        this.totalProcessingTime = totalProcessingTime;
    }

    public Routes getRoutes() {

        return routes;
    }

    public void setRoutes(Routes routes) {

        this.routes = routes;
    }

    @Override
    public String toString() {

        return "CamelContextRouteCoverage{" +
                "id='" + id + '\'' +
                ", exchangesTotal=" + exchangesTotal +
                ", totalProcessingTime=" + totalProcessingTime +
                ", routes=" + routes +
                '}';
    }
}
