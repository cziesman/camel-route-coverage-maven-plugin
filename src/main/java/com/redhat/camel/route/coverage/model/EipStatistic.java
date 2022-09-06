package com.redhat.camel.route.coverage.model;

import lombok.Builder;
import lombok.Data;

import java.util.Properties;

@Data
@Builder
public class EipStatistic {

    private String id;

    private boolean tested;

    private int totalProcessingTime;

    private Properties properties = new Properties();
}
