package com.redhat.camel.route.coverage.model;

import com.redhat.camel.route.coverage.map.MultiValueMap;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class RouteStatistic {

    private String id;

    private int totalEips;

    private int totalEipsTested;

    private int totalProcessingTime;

    private int coverage;

    private boolean totalEipsInitialized;

    private MultiValueMap<Integer, EipStatistic> eipStatisticMap;
}
