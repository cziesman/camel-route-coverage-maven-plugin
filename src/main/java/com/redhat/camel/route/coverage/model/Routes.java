package com.redhat.camel.route.coverage.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class Routes {

    @JsonProperty("route")
    private List<Route> routeList;
}
