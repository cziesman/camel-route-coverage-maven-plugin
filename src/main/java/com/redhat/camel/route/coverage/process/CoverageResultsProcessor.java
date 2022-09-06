package com.redhat.camel.route.coverage.process;

import com.redhat.camel.route.coverage.map.LinkedMultiValueMap;
import com.redhat.camel.route.coverage.map.MultiValueMap;
import com.redhat.camel.route.coverage.model.Components;
import com.redhat.camel.route.coverage.model.EipStatistic;
import com.redhat.camel.route.coverage.model.Route;
import com.redhat.camel.route.coverage.model.RouteStatistic;
import com.redhat.camel.route.coverage.model.RouteTotalsStatistic;
import com.redhat.camel.route.coverage.model.TestResult;
import com.redhat.camel.route.coverage.model.EipAttribute;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class CoverageResultsProcessor {

    private static final String DETAILS_FILE = "/details.html";

    private static final String INDEX_FILE = "/index.html";

    private static final String REST = "rest";

    private static final String TARGET = "target";

    private static final String CAMEL_ROUTE_COVERAGE = "camel-route-coverage";

    private static final String ROUTE_COVERAGE_REPORT = "route-coverage-report";

    private static final String FROM = "from";

    private static final String URI = "uri";

    private final Map<String, Route> routeMap = new TreeMap<>();

    private final Map<String, RouteStatistic> routeStatisticMap = new TreeMap<>();

    private final List<TestResult> testResults = new ArrayList<>();

    private final RouteTotalsStatistic routeTotalsStatistic = new RouteTotalsStatistic();

    private final XmlToCamelRouteCoverageConverter converter = new XmlToCamelRouteCoverageConverter();

    private final TestResultService testResultService = new TestResultService();

    public Integer generateReport(final String projectPath) throws IOException {

        String project = FileUtil.getLastElementOfPath(projectPath);
        String inputPath = Paths.get(projectPath, TARGET, CAMEL_ROUTE_COVERAGE).toString();
        String outputPath = Paths.get(projectPath, TARGET, ROUTE_COVERAGE_REPORT).toString();

        Path outputPathAsPath = Paths.get(outputPath);
        if (!Files.exists(outputPathAsPath)) {
            Files.createDirectory(outputPathAsPath);
        }

        parseAllTestResults(inputPath);

        if (testResults.size() > 0) {

            gatherBestRouteCoverages();

            squashDuplicateRoutes();

            String indexFile = generateRouteStatistics(project, outputPath);

            generateEipStatistics();

            generateHtml(outputPath);

            generateExcel(outputPath);

            System.out.printf("\n%d routes found. Statistics available at %s\n%n", routeStatisticMap.size(), indexFile);
        } else {
            System.out.printf("\nNo routes found. \n%n");
        }

        return 0;
    }

    protected void parseAllTestResults(final String inputPath) throws IOException {

        Set<String> testInputs = FileUtil.filesInDirectory(inputPath);

        for (String inputFile : testInputs) {
            TestResult testResult = parseTestResult(inputFile);
            testResults.add(testResult);
        }
    }

    protected TestResult parseTestResult(final String inputFile) throws IOException {

        String fileAsString = FileUtil.readFile(inputFile);

        TestResult testResult = converter.convert(fileAsString);

        assert testResult != null;

        return testResultService.getResultsForTest(testResult);
    }

    protected void generateEipStatistics() {

        // generate the statistics for each EIP within a route
        for (Route route : routeMap.values()) {

            MultiValueMap<Integer, EipStatistic> eipStatisticMap = new LinkedMultiValueMap<>();

            Components components = route.getComponents();
            Map<String, List<EipAttribute>> eipAttributesMap = components.getAttributeMap();

            eipAttributesMap.forEach((key, eipAttributes) -> {

                // 'rest' is a route attribute, not an EIP, so it doesn't make sense to include it
                if (!key.equals(REST)) {

                    LOG.trace(key + "::" + eipAttributes);

                    eipAttributes.forEach(eipAttribute -> {

                        EipStatistic eipStatistic =
                                EipStatistic.builder()
                                        .id(key)
                                        .tested(eipAttribute.getExchangesTotal() > 0)
                                        .totalProcessingTime(eipAttribute.getTotalProcessingTime())
                                        .properties(eipAttribute.getProperties())
                                        .build();
                        eipStatisticMap.add(eipAttribute.getIndex(), eipStatistic);
                    });
                }
            });

            RouteStatistic routeStatistic = routeStatisticMap.get(route.getId());
            routeStatistic.setEipStatisticMap(eipStatisticMap);
            routeStatisticMap.put(route.getId(), routeStatistic);
        }

        routeStatisticMap.values().forEach(routeStatistic -> LOG.debug(routeStatistic.toString()));
    }

    protected void generateExcel(final String outputPath) throws IOException {

        ExcelWriter writer = new ExcelWriter(routeStatisticMap.values(), outputPath);
        writer.write();
    }

    protected void generateHtml(final String outputPath) throws IOException {

        for (RouteStatistic routeStatistic : routeStatisticMap.values()) {
            writeDetailsAsHtml(routeStatistic, outputPath);
        }
    }

    protected void gatherBestRouteCoverages() {

        // get a de-duplicated list of the routes
        testResults.forEach(testResult -> {

            List<Route> routeList = testResult.getCamelContextRouteCoverage().getRoutes().getRouteList();
            routeList.forEach(route -> {

                String routeId = route.getId();

                Route mappedRoute = routeMap.get(routeId);
                if (mappedRoute == null) {
                    // if the route only appears once, this will handle it
                    routeMap.put(routeId, route);
                    mappedRoute = routeMap.get(routeId);
                }

                // if the route appears multiple times in the test results,
                // look for the route with the best coverage of EIPs
                try {
                    if (route.getExchangesTotal() > mappedRoute.getExchangesTotal()) {
                        routeMap.put(routeId, route);
                    }
                } catch (Throwable t) {
                    LOG.info(t.getClass().toString());
                    LOG.info(String.format("routeID: %s", routeId));
                    LOG.info(String.format("route: %s", route != null ? route.toString() : "null"));
                    LOG.info(String.format("mappedRoute: %s", mappedRoute != null ? mappedRoute.toString() : "null"));
                }
            });
        });
    }

    protected void squashDuplicateRoutes() {

        Map<String, String> squashMap = new TreeMap<>();

        // create a map using the from URI as the key to eliminate duplicates
        routeMap.forEach((key, route) -> {
            Map<String, Object> from = (Map<String, Object>) route.getComponentsMap().get(FROM);
            String uri = from.get(URI).toString();
            squashMap.put(uri, key);
        });

        // use the de-duplicated URIs to create a route map with unique routes, regardless of the ID
        Map<String, Route> squashedRouteMap = new TreeMap<>();
        squashMap.forEach((key, value) -> squashedRouteMap.put(value, routeMap.get(value)));

        routeMap.clear();
        routeMap.putAll(squashedRouteMap);
    }

    protected String generateRouteStatistics(final String project, final String outputPath) throws IOException {

        // generate the statistics for each route
        routeMap.values().forEach(route -> {

            String routeId = route.getId();
            RouteStatistic routeStatistic = getRouteStatistic(routeId);

            routeStatistic = update(route, routeStatistic);

            routeStatisticMap.put(routeId, routeStatistic);

            addToRouteTotals(routeStatistic);
        });

        return writeReportIndex(project, outputPath);
    }

    protected void addToRouteTotals(RouteStatistic routeStatistic) {

        routeTotalsStatistic.incrementTotalEips(routeStatistic.getTotalEips());
        routeTotalsStatistic.incrementTotalEipsTested(routeStatistic.getTotalEipsTested());
        routeTotalsStatistic.incrementTotalProcessingTime(routeStatistic.getTotalProcessingTime());
    }

    protected RouteStatistic getRouteStatistic(String routeId) {

        RouteStatistic routeStatistic;
        if (!routeStatisticMap.containsKey(routeId)) {
            routeStatistic = RouteStatistic.builder().id(routeId).build();
            routeStatisticMap.put(routeId, routeStatistic);
        } else {
            routeStatistic = routeStatisticMap.get(routeId);
        }

        return routeStatistic;
    }

    protected RouteStatistic update(final Route route, final RouteStatistic routeStatistic) {

        AtomicInteger totalEips = new AtomicInteger(routeStatistic.getTotalEips());
        AtomicInteger totalEipsTested = new AtomicInteger(routeStatistic.getTotalEipsTested());
        AtomicInteger totalProcessingTime = new AtomicInteger(routeStatistic.getTotalProcessingTime());
        route.getComponents().getAttributeMap().values().forEach(eipAttributes -> {
            if (!routeStatistic.isTotalEipsInitialized()) {
                // prevent adding the route multiple times
                totalEips.getAndAdd(eipAttributes.size());
            }

            eipAttributes.forEach(eipAttribute -> {
                totalEipsTested.getAndAdd(eipAttribute.getExchangesTotal());
                totalProcessingTime.getAndAdd(eipAttribute.getTotalProcessingTime());
            });
        });

        // this is a hack because of some weird calculation bug that I cannot find
        if (totalEipsTested.get() > totalEips.get()) {
            totalEipsTested.set(totalEips.get());
        }
        int coverage = 0;
        if (totalEips.get() > 0) {
            coverage = (100 * totalEipsTested.get()) / totalEips.get();
        }

        return RouteStatistic.builder()
                .id(route.getId())
                .totalEips(totalEips.get())
                .totalEipsTested(totalEipsTested.get())
                .totalProcessingTime(totalProcessingTime.get())
                .coverage(coverage)
                .totalEipsInitialized(true)
                .build();
    }

    protected void writeDetailsAsHtml(final RouteStatistic routeStatistic, final String outputPath) throws IOException {

        Map<String, Object> data = new HashMap<>();
        data.put("route", routeStatistic);
        data.put("eips", routeStatistic.getEipStatisticMap().entrySet());

        String rendered = TemplateUtil.render(DETAILS_FILE, data);
        FileUtil.write(rendered, routeStatistic.getId(), outputPath);
    }

    protected String writeReportIndex(final String project, final String outputPath) throws IOException {

        Map<String, Object> data = new HashMap<>();
        data.put("project", project);
        data.put("routes", routeStatisticMap.values());
        data.put("totals", routeTotalsStatistic);

        String rendered = TemplateUtil.render(INDEX_FILE, data);

        return FileUtil.write(rendered, "index", outputPath);
    }
}
