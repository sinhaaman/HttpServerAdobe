package com.adobe.project.headers;


import java.util.Map;

import com.google.common.collect.ImmutableMap;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class HttpRequestHeaderTests {

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

    @DisplayName("Test the set header function of the HttpRequestHeader")
    @Test
    public void HttpRequestHeaderSetHeadersTest() {
        // given

        String headerKey = "Host";
        String headerValue = "TestHost";
        HttpRequestHeader httpRequestHeader = new HttpRequestHeader();

        // when
        httpRequestHeader.setHeaders(headerKey, headerValue);

        // then
        assertThat(httpRequestHeader.getHeadersMap().get(headerKey)).isEqualTo(headerValue);

    }

    @DisplayName("Test the constructor of HttpRequest with the map")
    @Test
    public void HttpRequestHeaderMapConstructorTest() {
        // given


        // when
        HttpRequestHeader httpRequestHeader = new HttpRequestHeader(testHeadersMap);

        // then
        assertThat(httpRequestHeader.getHeadersMap().get("Referer")).isEqualTo(testHeadersMap.get("Referer"));

    }

    @DisplayName("Test the toString functionality of the HttpRequest contains all the keys and values of the map")
    @Test
    public void HttpRequestToStringTest() {
        // given


        // when
        HttpRequestHeader httpRequestHeader = new HttpRequestHeader(testHeadersMap);

        // then
        String httpRequestHeaderToString = httpRequestHeader.toString();
        testHeadersMap.entrySet().stream().forEach(entrySet -> {
            assertThat(httpRequestHeaderToString).contains(entrySet.getKey());
            assertThat(httpRequestHeaderToString).contains(entrySet.getValue());
        });
    }

    @DisplayName("Test the toString functionality of the HttpRequest contains all the keys and values of the map in " +
        "an ordered format")
    @Test
    public void HttpRequestToStringFormatTest() {
        // given
        String expectedStringFormat =
            "Accept: TestUserAccept\r\n" +
            "Accept-Encoding: TestEncoding\r\n" +
            "Accept-Language: TestAcceptLanguage\r\n" +
            "Host: TestHost\r\n" +
            "If-Modified-Since: TestModified\r\n" +
            "If-None-Match: TestNoneMatch\r\n" +
            "Referer: TestReferer\r\n" +
            "Upgrade-Insecure-Requests: TestUpgrade\r\n" +
            "User-Agent: TestUserAgent\r\n";

        // when
        HttpRequestHeader httpRequestHeader = new HttpRequestHeader(testHeadersMap);

        // then
        assertThat(httpRequestHeader.toString()).isEqualTo(expectedStringFormat);
    }
}
