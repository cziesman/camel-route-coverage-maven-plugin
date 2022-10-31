package com.redhat.camel.route.coverage.model;

import java.util.Properties;

public class EipStatistic {

    private String id;

    private boolean tested;

    private int totalProcessingTime;

    private Properties properties = new Properties();

    public String getId() {

        return id;
    }

    public void setId(String id) {

        this.id = id;
    }

    public boolean isTested() {

        return tested;
    }

    public void setTested(boolean tested) {

        this.tested = tested;
    }

    public int getTotalProcessingTime() {

        return totalProcessingTime;
    }

    public void setTotalProcessingTime(int totalProcessingTime) {

        this.totalProcessingTime = totalProcessingTime;
    }

    public Properties getProperties() {

        return properties;
    }

    public void setProperties(Properties properties) {

        this.properties = properties;
    }

    @Override
    public String toString() {

        return "EipStatistic{" +
                "id='" + id + '\'' +
                ", tested=" + tested +
                ", totalProcessingTime=" + totalProcessingTime +
                ", properties=" + properties +
                '}';
    }
}
