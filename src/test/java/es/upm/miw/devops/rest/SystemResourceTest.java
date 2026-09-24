package es.upm.miw.devops.rest;

import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

class SystemResourceTest {

    @Test
    void generateBadgeContainsLabelAndValue() {
        SystemResource resource = new SystemResource();

        String badge = resource.generateBadge("Render", "v1.0");

        assertThat(badge)
                .contains("<svg")
                .contains("Render")
                .contains("v1.0")
                .endsWith("</svg>\n");
    }

    @Test
    void applicationInfoContainsApplicationEndpoints() {
        SystemResource resource = new SystemResource();
        ReflectionTestUtils.setField(resource, "artifact", "test-artifact");
        ReflectionTestUtils.setField(resource, "version", "1.0");
        ReflectionTestUtils.setField(resource, "build", "today");

        assertThat(resource.applicationInfo())
                .contains("test-artifact::1.0::today")
                .contains("/version-badge")
                .contains("/actuator/info")
                .contains("/swagger-ui.html");
    }

    @Test
    void generatedBadgeEndpointReturnsSvgBytes() {
        SystemResource resource = new SystemResource();

        ReflectionTestUtils.setField(resource, "version", "1.0");
        ReflectionTestUtils.setField(resource, "hosting", "Docker");

        String badge = new String(
                resource.generateBadge(),
                StandardCharsets.UTF_8
        );

        assertThat(badge)
                .contains("<svg")
                .contains("Docker")
                .contains("v1.0");
    }
}
