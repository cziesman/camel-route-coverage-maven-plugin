package com.redhat.camel.route.coverage.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class EipAttribute implements Comparable<EipAttribute> {

    private static final Logger LOG = LoggerFactory.getLogger(EipAttribute.class);

    private String id;

    private int exchangesTotal;

    private int index;

    private int totalProcessingTime;

    private Properties properties = new Properties();

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

    public int getIndex() {

        return index;
    }

    public void setIndex(int index) {

        this.index = index;
    }

    public int getTotalProcessingTime() {

        return totalProcessingTime;
    }

    public void setTotalProcessingTime(int totalProcessingTime) {

        this.totalProcessingTime = totalProcessingTime;
    }

    @JsonAnySetter
    public void setProperty(String key, Object value) {

        LOG.trace(key + "::" + value);
        properties.put(key, value);
    }

    @JsonAnyGetter
    public Properties getProperties() {

        return properties;
    }

    public void setProperties(Properties properties) {

        this.properties = properties;
    }

    @Override
    public int compareTo(EipAttribute o) {

        if (o == null) {
            return 1;
        }
        return index - o.index;
    }

    @Override
    public boolean equals(Object o) {

        if (o == this) {
            return true;
        }

        if (!(o instanceof EipAttribute)) {
            return false;
        }

        return id.equals(((EipAttribute) o).id);
    }

    @Override
    public String toString() {

        return "EipAttribute{" +
                "id='" + id + '\'' +
                ", exchangesTotal=" + exchangesTotal +
                ", index=" + index +
                ", totalProcessingTime=" + totalProcessingTime +
                ", properties=" + properties +
                '}';
    }
}
