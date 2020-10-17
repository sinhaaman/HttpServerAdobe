package com.adobe.project.headers;

import java.util.Map;

import com.google.common.collect.ImmutableMap;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.adobe.project.headers.HttpResponseHeader.*;
import static org.assertj.core.api.Assertions.assertThat;

public class HttpResponseHeaderTests {

    private final Map<String, String> testHeadersMap = ImmutableMap.<String, String>builder()
        .put(ACCESS_CONTROL_ALLOW_ORIGIN, "TestAccessControl")
        .put(CONTENT_ENCODING, "TestContentEncoding")
        .put(CONTENT_LENGTH, "TestContentLength")
        .put(CONTENT_TYPE, "TestContentType")
        .put(ETAG, "TestEtag")
        .put(KEEP_ALIVE, "TestKeepAlive")
        .put(LAST_MODIFIED, "TestLastModified")
        .put(SERVER, "TestServer")
        .put(SET_COOKIE, "TestSetCookie")
        .put(TRANSFER_ENCODING, "TestSetCookie")
        .put(VARY, "TestSetCookie")
        .put(X_BACKEND_SERVER, "TestSetCookie")
        .put(X_CACHE_INFO, "TestSetCookie")
        .put(X_KUMA_REVISION, "TestKumaRevision")
        .put(X_FRAME_OPTIONS, "TestFrameOptions")
        .build();

    @DisplayName("Test the set header function of the HttpResponseHeader")
    @Test
    public void HttpResponseHeaderSetHeadersTest() {
        // given

        String headerValue = "TestAccessControl";
        HttpResponseHeader httpRequestHeader = new HttpResponseHeader();

        // when
        httpRequestHeader.setHeaders(ACCESS_CONTROL_ALLOW_ORIGIN, headerValue);

        // then
        assertThat(httpRequestHeader.getHeadersMap().get(ACCESS_CONTROL_ALLOW_ORIGIN)).isEqualTo(headerValue);

    }

    @DisplayName("Test the constructor of HttpResponse with the map")
    @Test
    public void HttpResponseHeaderMapConstructorTest() {
        // given


        // when
        HttpResponseHeader httpResponseHeader = new HttpResponseHeader(testHeadersMap);

        // then
        assertThat(httpResponseHeader.getHeadersMap().get("Referer")).isEqualTo(testHeadersMap.get("Referer"));

    }

    @DisplayName("Test the toString functionality of the HttpResponse contains all the keys and values of the map")
    @Test
    public void HttpResponseToStringTest() {
        // given


        // when
        HttpResponseHeader httpResponseHeader = new HttpResponseHeader(testHeadersMap);

        // then
        String httpRequestHeaderToString = httpResponseHeader.toString();
        testHeadersMap.entrySet().stream().forEach(entrySet -> {
            assertThat(httpRequestHeaderToString).contains(entrySet.getKey());
            assertThat(httpRequestHeaderToString).contains(entrySet.getValue());
        });
    }

    @DisplayName("Test the toString functionality of the HttpResponse contains all the keys and values of the map i" +
        "n an ordered format")
    @Test
    public void HttpResponseToStringFormatTest() {
        // given
        String expectedStringFormat =
                "Access-Control-Allow-Origin: TestAccessControl" + System.lineSeparator() +
                "Content-Encoding: TestContentEncoding" + System.lineSeparator() +
                "Content-Length: TestContentLength" + System.lineSeparator() +
                "Content-Type: TestContentType" + System.lineSeparator() +
                "Etag: TestEtag" + System.lineSeparator() +
                "Keep-Alive: TestKeepAlive" + System.lineSeparator() +
                "Last-Modified: TestLastModified" + System.lineSeparator() +
                "Server: TestServer" + System.lineSeparator() +
                "Set-Cookie: TestSetCookie" + System.lineSeparator() +
                "Transfer-Encoding: TestSetCookie" + System.lineSeparator() +
                "Vary: TestSetCookie" + System.lineSeparator() +
                "X-Backend-Server: TestSetCookie" + System.lineSeparator() +
                "X-Cache-Info: TestSetCookie" + System.lineSeparator() +
                "X-kuma-revision: TestKumaRevision" + System.lineSeparator() +
                "x-frame-options: TestFrameOptions" + System.lineSeparator() ;

        // when
        HttpResponseHeader httpResponseHeader = new HttpResponseHeader(testHeadersMap);

        // then
        assertThat(httpResponseHeader.toString()).isEqualTo(expectedStringFormat);
    }
}
