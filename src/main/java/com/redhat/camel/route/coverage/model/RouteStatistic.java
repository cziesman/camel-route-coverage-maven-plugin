package com.redhat.camel.route.coverage.model;

import com.redhat.camel.route.coverage.map.MultiValueMap;

public class RouteStatistic {

    private String id;

    private int totalEips;

    private int totalEipsTested;

    private int totalProcessingTime;

    private int coverage;

    private boolean totalEipsInitialized;

    private MultiValueMap<Integer, EipStatistic> eipStatisticMap;

    public String getId() {

        return id;
    }

    public void setId(String id) {

        this.id = id;
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

    public boolean isTotalEipsInitialized() {

        return totalEipsInitialized;
    }

    public void setTotalEipsInitialized(boolean totalEipsInitialized) {

        this.totalEipsInitialized = totalEipsInitialized;
    }

    public MultiValueMap<Integer, EipStatistic> getEipStatisticMap() {

        return eipStatisticMap;
    }

    public void setEipStatisticMap(MultiValueMap<Integer, EipStatistic> eipStatisticMap) {

        this.eipStatisticMap = eipStatisticMap;
    }

    @Override
    public String toString() {

        return "RouteStatistic{" +
                "id='" + id + '\'' +
                ", totalEips=" + totalEips +
                ", totalEipsTested=" + totalEipsTested +
                ", totalProcessingTime=" + totalProcessingTime +
                ", coverage=" + coverage +
                ", totalEipsInitialized=" + totalEipsInitialized +
                ", eipStatisticMap=" + eipStatisticMap +
                '}';
    }
}
