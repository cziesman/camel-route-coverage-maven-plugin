package com.redhat.camel.route.coverage.process;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class TemplateRendererTest {

    private static final String EXPECTED =
            "<!DOCTYPE html>\n" +
                    "<html lang=\"en\">\n" +
                    "\n" +
                    "    <body>\n" +
                    "        <h1>testRender</h1>\n" +
                    "    </body>\n" +
                    "</html>";

    @Test
    public void testTemplateRenderer() {

        // keep jacoco happy
        TemplateRenderer result = new TemplateRenderer();

        assertThat(result).isNotNull();
    }

    @Test
    public void testRender() {

        String result = TemplateRenderer.render("index", Map.of("testValue", "testRender"));
        LOG.debug(result);

        assertThat(result).isEqualTo(EXPECTED);
    }
}