package com.adobe.project.request;

import java.util.Map;

import com.adobe.project.headers.HttpRequestHeader;
import com.google.common.collect.ImmutableMap;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class HttpRequestTests {

    private final Map<String, String> testHeadersMap = ImmutableMap.<String, String>builder()
        .put("Host", "TestHost")
        .put("User-Agent", "TestUserAgent")
        .put("Accept", "TestUserAccept")
        .put("Accept-Language", "TestAcceptLanguage")
        .put("Accept-Encoding", "TestEncoding")
        .put("Referer", "TestReferer")
        .put("Upgrade-Insecure-Requests", "TestUpgrade")
        .put("If-Modified-Since", "TestModified")
        .put("If-None-Match", "TestNoneMatch")
        .build();

    @DisplayName("Test the construction of the HttpRequest")
    @Test
    public void HttpRequestConstructionTest() {
        // given
        HttpRequestOperationLine httpRequestOperationLine = HttpRequestOperationLine.builder()
            .method("TEST_METHOD")
            .path("TEST_PATH")
            .version("TEST_VERSION")
            .build();

        HttpRequestHeader httpRequestHeader = new HttpRequestHeader(testHeadersMap);

        // when
        HttpRequest httpRequest = HttpRequest.builder()
            .httpRequestOperationLine(httpRequestOperationLine)
            .httpRequestHeader(httpRequestHeader)
            .build();

        //then
        assertThat(httpRequest.getHttpRequestOperationLine()).isEqualTo(httpRequestOperationLine);
        assertThat(httpRequest.getHttpRequestHeader()).isEqualTo(httpRequestHeader);
        assertThat(httpRequest.getHttpRequestHeader().getHeadersMap()).isEqualTo(testHeadersMap);
    }
}
