package com.redhat.camel.route.coverage.process;

import com.redhat.camel.route.coverage.model.Route;
import com.redhat.camel.route.coverage.model.RouteStatistic;
import com.redhat.camel.route.coverage.model.TestResult;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class CoverageResultsProcessorTest {

    private static final Logger LOG = LoggerFactory.getLogger(CoverageResultsProcessorTest.class);

    private static final String TARGET = "target";

    private static final String CAMEL_ROUTE_COVERAGE = "camel-route-coverage";

    private static final String ROUTE_COVERAGE_REPORT = "route-coverage-report";

    private static final String SRC = "src";

    private static final String TEST = "test";

    private static final String RESOURCES = "resources";

    private static final String GREETINGS_FILE_ROUTE_UNIT_TEST_XML = "com.example.GreetingsFileRouteUnitTest.xml";

    private static final String DONTCARE = "dontcare";

    private static final String INDEX_HTML = "index.html";

    private static final String GREETINGS_ROUTE = "greetings-route";

    private static final String INDEX_XLSX = "index.xlsx";

    @Spy
    private CoverageResultsProcessor processor;

    @Spy
    private XmlToCamelRouteCoverageConverter converter;

    @TempDir
    private File temporaryDirectory;

    @Test
    public void testCoverageResultsProcessor() {

        // keep jacoco happy
        assertThat(processor).isNotNull();
    }

    @Test
    public void testGenerateReport() throws IOException {

        String outputPath = projectOutputPath();
        Path outputPathAsPath = Paths.get(outputPath);
        if (!Files.exists(outputPathAsPath)) {
            Files.createDirectories(outputPathAsPath);
        }

        processor.generateReport(projectPath(), outputPath);

        assertAll(
                () -> assertThat(Files.exists(Paths.get(projectOutputPath()))).isTrue(),
                () -> assertThat(Files.exists(Paths.get(projectOutputIndexFile()))).isTrue(),
                () -> assertThat(Files.exists(Paths.get(projectOutputExcelFile()))).isTrue()
        );
    }

    @Test
    public void testParseAllTestResults() throws IOException, IllegalAccessException {

        Mockito
                .doReturn(TestUtil.testResult())
                .when(processor).parseTestResult(any(String.class));

        processor.parseAllTestResults(inputPath());

        @SuppressWarnings("unchecked")
        List<TestResult> result = (List<TestResult>) FieldUtils.readDeclaredField(processor, "testResults", true);

        assertAll(
                () -> assertThat(result).isNotNull(),
                () -> assertThat(result.isEmpty()).isFalse(),
                () -> assertThat(result.size()).isEqualTo(1),
                () -> assertThat(result.get(0).getTest()).isNotNull(),
                () -> assertThat(result.get(0).getTest().getClazz()).isEqualTo("some_class"),
                () -> assertThat(result.get(0).getTest().getMethod()).isEqualTo("some_method"),
                () -> assertThat(result.get(0).getCamelContextRouteCoverage()).isNotNull(),
                () -> assertThat(result.get(0).getCamelContextRouteCoverage().getRoutes().getRouteList()).isNotEmpty()
        );
    }

    @Test
    public void testParseTestResult() throws IOException {

        TestResult result = processor.parseTestResult(inputFile());

        assertAll(
                () -> assertThat(result).isNotNull(),
                () -> assertThat(result.getTest()).isNotNull(),
                () -> assertThat(result.getTest().getClazz()).isEqualTo("com.example.GreetingsFileRouteUnitTest"),
                () -> assertThat(result.getTest().getMethod()).isEqualTo("whenSendBody_thenGreetingReceivedSuccessfully"),
                () -> assertThat(result.getCamelContextRouteCoverage()).isNotNull(),
                () -> assertThat(result.getCamelContextRouteCoverage().getRoutes().getRouteList()).isNotEmpty()
        );
    }

    @Test
    public void testParseTestResultNull() throws IllegalAccessException {

        FieldUtils.writeDeclaredField(processor, "xmlToCamelRouteCoverageConverter", converter, true);

        Mockito
                .doReturn(null)
                .when(converter).convert(any(String.class));

        Assertions.assertThrows(AssertionError.class, () -> processor.parseTestResult(inputFile()));
    }

    @Test
    public void testGenerateEipStatistics() throws IllegalAccessException, IOException {

        Mockito
                .doReturn(outputPath())
                .when(processor).writeReportIndex(any(String.class), any(String.class));

        @SuppressWarnings("unchecked")
        Map<String, RouteStatistic> routeStatisticMap =
                (Map<String, RouteStatistic>) FieldUtils.readDeclaredField(processor, "routeStatisticMap", true);

        assertAll(
                () -> assertThat(routeStatisticMap).isNotNull(),
                () -> assertThat(routeStatisticMap).isEmpty()
        );

        processor.parseAllTestResults(inputPath());
        processor.gatherBestRouteCoverages();
        processor.generateRouteStatistics("test project", outputPath());
        processor.generateEipStatistics();

        assertAll(
                () -> assertThat(routeStatisticMap).isNotNull(),
                () -> assertThat(routeStatisticMap).isNotEmpty()
        );

        RouteStatistic result = routeStatisticMap.get(GREETINGS_ROUTE);

        assertAll(
                () -> assertThat(result).isNotNull(),
                () -> assertThat(result.getEipStatisticMap()).isNotNull(),
                () -> assertThat(result.getEipStatisticMap().size()).isEqualTo(3)
        );
    }

    @Test
    public void testGenerateExcel() {

    }

    @Test
    public void testGenerateHtml() {

    }

    @Test
    public void testGatherBestRouteCoverages() throws IllegalAccessException, IOException {

        @SuppressWarnings("unchecked")
        List<TestResult> testResults = (List<TestResult>) FieldUtils.readDeclaredField(processor, "testResults", true);
        @SuppressWarnings("unchecked")
        Map<String, Route> result = (Map<String, Route>) FieldUtils.readDeclaredField(processor, "routeMap", true);

        processor.parseAllTestResults(inputPath());
        testResults.add(TestUtil.testResult());

        assertAll(
                () -> assertThat(result).isNotNull(),
                () -> assertThat(result.size()).isEqualTo(0)
        );

        processor.gatherBestRouteCoverages();
        assertAll(
                () -> assertThat(result).isNotNull(),
                () -> assertThat(result.size()).isEqualTo(1)
        );
    }

    @Test
    public void testSquashDuplicateRoutes() throws IllegalAccessException {

        @SuppressWarnings("unchecked")
        Map<String, Route> result = (Map<String, Route>) FieldUtils.readDeclaredField(processor, "routeMap", true);

        result.clear();
        result.put("route1", TestUtil.route());
        result.put("route2", TestUtil.route());

        assertAll(
                () -> assertThat(result).isNotNull(),
                () -> assertThat(result.size()).isEqualTo(2)
        );

        processor.squashDuplicateRoutes();

        assertAll(
                () -> assertThat(result).isNotNull(),
                () -> assertThat(result.size()).isEqualTo(1)
        );
    }

    @Test
    public void testGenerateRouteStatistics() throws IllegalAccessException, IOException {

        Mockito
                .doReturn(outputPath())
                .when(processor).writeReportIndex(any(String.class), any(String.class));

        @SuppressWarnings("unchecked")
        Map<String, Route> routeMap = (Map<String, Route>) FieldUtils.readDeclaredField(processor, "routeMap", true);

        processor.parseAllTestResults(inputPath());
        processor.gatherBestRouteCoverages();

        assertAll(
                () -> assertThat(routeMap).isNotNull(),
                () -> assertThat(routeMap.size()).isEqualTo(1)
        );

        String result = processor.generateRouteStatistics("test project", outputPath());
        LOG.debug(result);

        assertAll(
                () -> assertThat(result).isNotBlank(),
                () -> assertThat(result).isEqualTo(outputPath())
        );
    }

    @Test
    public void testAddToRouteTotals() {

    }

    @Test
    public void testGetRouteStatistic() throws IllegalAccessException {

        @SuppressWarnings("unchecked")
        Map<String, RouteStatistic> routeStatisticMap = (Map<String, RouteStatistic>) FieldUtils.readDeclaredField(processor, "routeStatisticMap", true);

        assertAll(
                () -> assertThat(routeStatisticMap).isNotNull(),
                () -> assertThat(routeStatisticMap.size()).isEqualTo(0)
        );

        RouteStatistic result = processor.getRouteStatistic("test");

        RouteStatistic finalResult = result;
        assertAll(
                () -> assertThat(finalResult).isNotNull(),
                () -> assertThat(finalResult.getId()).isEqualTo("test"),
                () -> assertThat(routeStatisticMap).isNotNull(),
                () -> assertThat(routeStatisticMap.size()).isEqualTo(1)
        );

        result = processor.getRouteStatistic("test");
        RouteStatistic finalResult2 = result;

        assertAll(
                () -> assertThat(finalResult2).isNotNull(),
                () -> assertThat(finalResult2.getId()).isEqualTo("test"),
                () -> assertThat(routeStatisticMap).isNotNull(),
                () -> assertThat(routeStatisticMap.size()).isEqualTo(1)
        );
    }

    @Test
    public void testRecalculate() {

    }

    @Test
    public void testWriteDetailsAsHtml() throws IllegalAccessException, IOException {

        Map<String, RouteStatistic> routeStatisticMap
                = (Map<String, RouteStatistic>) FieldUtils.readDeclaredField(processor, "routeStatisticMap", true);

        String outputPath = outputPath();
        Path outputPathAsPath = Paths.get(outputPath);
        if (!Files.exists(outputPathAsPath)) {
            Files.createDirectories(outputPathAsPath);
        }

        processor.parseAllTestResults(inputPath());
        processor.gatherBestRouteCoverages();
        processor.generateRouteStatistics("test project", outputPath());
        processor.generateEipStatistics();

        LOG.debug("{}", routeStatisticMap);

        RouteStatistic routeStatistic = processor.getRouteStatistic(GREETINGS_ROUTE);

        assertAll(
                () -> assertThat(routeStatistic).isNotNull(),
                () -> assertThat(routeStatistic.getEipStatisticMap()).isNotNull(),
                () -> assertThat(routeStatistic.getEipStatisticMap().size()).isEqualTo(3)
        );

        processor.writeDetailsAsHtml(routeStatistic, outputPath());
    }

    @Test
    public void testWriteReportIndex() throws IOException {

        String outputPath = outputPath();
        Path outputPathAsPath = Paths.get(outputPath);
        if (!Files.exists(outputPathAsPath)) {
            Files.createDirectories(outputPathAsPath);
        }

        String result = processor.writeReportIndex("test-project", outputPath);

        assertAll(
                () -> assertThat(result).isNotBlank(),
                () -> assertThat(result).isEqualTo(outputFile())
        );
    }

    private String projectPath() {

        return Paths.get(SRC, TEST, RESOURCES).toString();
    }

    private String projectOutputPath() {

        return Paths.get(temporaryDirectory.getPath(), TARGET, ROUTE_COVERAGE_REPORT).toString();
    }

    private String projectOutputIndexFile() {

        return Paths.get(temporaryDirectory.getPath(), TARGET, ROUTE_COVERAGE_REPORT, INDEX_HTML).toString();
    }

    private String projectOutputExcelFile() {

        return Paths.get(temporaryDirectory.getPath(), TARGET, ROUTE_COVERAGE_REPORT, INDEX_XLSX).toString();
    }

    private String inputPath() {

        return Paths.get(SRC, TEST, RESOURCES, TARGET, CAMEL_ROUTE_COVERAGE).toString();
    }

    private String inputFile() {

        return Paths.get(SRC, TEST, RESOURCES, TARGET, CAMEL_ROUTE_COVERAGE, GREETINGS_FILE_ROUTE_UNIT_TEST_XML).toString();
    }

    private String outputPath() {

        return Paths.get(temporaryDirectory.getPath(), TARGET, ROUTE_COVERAGE_REPORT).toString();
    }

    private String outputFile() {

        return Paths.get(temporaryDirectory.getPath(), TARGET, ROUTE_COVERAGE_REPORT, INDEX_HTML).toString();
    }
}