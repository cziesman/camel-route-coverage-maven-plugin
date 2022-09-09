package com.redhat.camel.route.coverage.process;

import com.redhat.camel.route.coverage.model.TestResult;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.spy;

@Slf4j
public class TestResultParserTest {

    @Test
    public void testTestResultParser() {

        // keep jacoco happy
        TestResultParser result = new TestResultParser();

        assertThat(result).isNotNull();
    }

    @Test
    public void testParse() {

        TestResultParser parser = new TestResultParser();

        TestResult result = parser.parse(TestUtil.testResult());
        LOG.debug("{}", result);

        assertAll(
                () -> assertThat(result).isNotNull(),
                () -> assertThat(result.getCamelContextRouteCoverage().getRoutes().getRouteList().get(0).getComponents()).isNotNull());
    }

    @Test
    public void testParseException() {

        TestResultParser spy = spy(new TestResultParser());

        Mockito
                .doAnswer(invocation -> {
                    throw new TestJsonProcessingException();
                })
                .when(spy).objectMapper();

        TestResult result = spy.parse(TestUtil.testResult());
        LOG.debug("{}", result);

        assertAll(
                () -> assertThat(result).isNotNull(),
                () -> assertThat(result.getCamelContextRouteCoverage().getRoutes().getRouteList().get(0).getComponents()).isNull());
    }
}

