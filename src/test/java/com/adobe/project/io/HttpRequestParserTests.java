package com.adobe.project.io;

import java.io.IOException;
import java.io.InputStream;

import com.adobe.project.request.HttpRequest;
import com.adobe.project.request.HttpServerSupportedMethods;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class HttpRequestParserTests {

    @DisplayName("Parse HTTP request without headers")
    @Test
    public void testParseHttpRequestWithoutHeaders() throws IOException {

        // given
        InputStream inputStream =
            IOUtils.toInputStream("GET / HTTP/1.1", "UTF-8");
        HttpRequestParser httpRequestParser = new HttpRequestParser(inputStream);

        // when
        HttpRequest httpRequest = httpRequestParser.parseHttpRequest();

        // then
        assertThat(httpRequest.getHttpRequestOperationLine().getMethod())
            .isEqualTo(HttpServerSupportedMethods.GET.getValue());
        assertThat(httpRequest.getHttpRequestOperationLine().getPath()).isEqualTo("/");
        assertThat(httpRequest.getHttpRequestOperationLine().getVersion()).isEqualTo("HTTP/1.1");
        assertThat(httpRequest.getHttpRequestHeader().getHeadersMap()).isEmpty();
    }

    @DisplayName("Parse HTTP request with headers")
    @Test
    public void testParseHttpRequestWithHeaders() throws IOException {

        // given
        InputStream stubInputStream =
            IOUtils.toInputStream(
                "GET / HTTP/1.1 \n"
                + "Host : TestHost",
                "UTF-8");
        HttpRequestParser httpRequestParser = new HttpRequestParser(stubInputStream);

        // when
        HttpRequest httpRequest = httpRequestParser.parseHttpRequest();

        // then
        assertThat(httpRequest.getHttpRequestOperationLine().getMethod())
            .isEqualTo(HttpServerSupportedMethods.GET.getValue());
        assertThat(httpRequest.getHttpRequestOperationLine().getPath()).isEqualTo("/");
        assertThat(httpRequest.getHttpRequestOperationLine().getVersion()).isEqualTo("HTTP/1.1");
        assertThat(httpRequest.getHttpRequestHeader().getHeadersMap().get("Host")).isEqualTo("TestHost");
    }

    @DisplayName("Parse malformed HTTP request with headers")
    @Test
    public void testParseMalformedHttpRequest() throws IOException {

        // given
        InputStream stubInputStream =
            IOUtils.toInputStream(
                "Test \n"
                + "Test \n"
                + "Test \n"
                , "UTF-8");
        HttpRequestParser httpRequestParser = new HttpRequestParser(stubInputStream);

        // when
        HttpRequest httpRequest = httpRequestParser.parseHttpRequest();

        // then
        assertThat(httpRequest.getHttpRequestOperationLine().getMethod()).isNull();
        assertThat(httpRequest.getHttpRequestOperationLine().getPath()).isNull();
        assertThat(httpRequest.getHttpRequestOperationLine().getVersion()).isNull();
        assertThat(httpRequest.getHttpRequestHeader().getHeadersMap()).isEmpty();
    }
}
