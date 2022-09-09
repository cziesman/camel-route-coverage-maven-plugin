package com.redhat.camel.route.coverage.process;

import com.fasterxml.jackson.core.JsonProcessingException;

public class TestJsonProcessingException extends JsonProcessingException {

    private static final String DONTCARE = "dontcare";

    public TestJsonProcessingException() {

        super(DONTCARE);
    }
}