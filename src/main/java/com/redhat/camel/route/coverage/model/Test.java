package com.redhat.camel.route.coverage.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Test {

    @JsonProperty("class")
    private String clazz;

    private String method;

    public String getClazz() {

        return clazz;
    }

    public void setClazz(String clazz) {

        this.clazz = clazz;
    }

    public String getMethod() {

        return method;
    }

    public void setMethod(String method) {

        this.method = method;
    }

    @Override
    public String toString() {

        return "Test{" +
                "clazz='" + clazz + '\'' +
                ", method='" + method + '\'' +
                '}';
    }
}