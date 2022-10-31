package com.redhat.camel.route.coverage.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Routes {

    @JsonProperty("route")
    private List<Route> routeList;

    public List<Route> getRouteList() {

        return routeList;
    }

    public void setRouteList(List<Route> routeList) {

        this.routeList = routeList;
    }

    @Override
    public String toString() {

        return "Routes{" +
                "routeList=" + routeList +
                '}';
    }
}
