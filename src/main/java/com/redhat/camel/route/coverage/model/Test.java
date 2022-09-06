package com.redhat.camel.route.coverage.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Test {

    @JsonProperty("class")
    private String clazz;

    private String method;
}