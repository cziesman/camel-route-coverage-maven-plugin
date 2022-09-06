package com.redhat.camel.route.coverage.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Properties;

@Data
@Slf4j
public class EipAttribute implements Comparable<EipAttribute> {

    private String id;

    private int exchangesTotal;

    private int index;

    private int totalProcessingTime;

    private Properties properties = new Properties();

    @JsonAnyGetter
    public Properties getProperties() {

        return properties;
    }

    @JsonAnySetter
    public void setProperty(String key, Object value) {

        LOG.trace(key + "::" + value);
        properties.put(key, value);
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
}
