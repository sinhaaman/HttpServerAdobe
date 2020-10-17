package com.adobe.project.request;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class HttpRequestOperationLineTests {

    @DisplayName("Test the expected format of the toString method of HttpRequestOperationLine")
    @Test
    public void httpRequestOperationLineToStringTest() {
        // given

        String expectedOperationLineStringFormat = "TEST_METHOD TEST_PATH TEST_VERSION";

        // when
        HttpRequestOperationLine httpRequestOperationLine = HttpRequestOperationLine.builder()
            .method("TEST_METHOD")
            .path("TEST_PATH")
            .version("TEST_VERSION")
            .build();

        // then
        assertThat(httpRequestOperationLine.getMethod()).isEqualTo("TEST_METHOD");
        assertThat(httpRequestOperationLine.getPath()).isEqualTo("TEST_PATH");
        assertThat(httpRequestOperationLine.getVersion()).isEqualTo("TEST_VERSION");
        assertThat(httpRequestOperationLine.toString()).isEqualTo(expectedOperationLineStringFormat);

    }
}
