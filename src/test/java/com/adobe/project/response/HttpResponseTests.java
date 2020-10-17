package com.adobe.project.response;

import java.util.Map;

import com.adobe.project.headers.HttpResponseHeader;
import com.google.common.collect.ImmutableMap;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.adobe.project.headers.HttpResponseHeader.ACCESS_CONTROL_ALLOW_ORIGIN;
import static com.adobe.project.headers.HttpResponseHeader.CONTENT_ENCODING;
import static com.adobe.project.headers.HttpResponseHeader.CONTENT_LENGTH;
import static com.adobe.project.headers.HttpResponseHeader.CONTENT_TYPE;
import static com.adobe.project.headers.HttpResponseHeader.ETAG;
import static com.adobe.project.headers.HttpResponseHeader.KEEP_ALIVE;
import static com.adobe.project.headers.HttpResponseHeader.LAST_MODIFIED;
import static com.adobe.project.headers.HttpResponseHeader.SERVER;
import static com.adobe.project.headers.HttpResponseHeader.SET_COOKIE;
import static com.adobe.project.headers.HttpResponseHeader.TRANSFER_ENCODING;
import static com.adobe.project.headers.HttpResponseHeader.VARY;
import static com.adobe.project.headers.HttpResponseHeader.X_BACKEND_SERVER;
import static com.adobe.project.headers.HttpResponseHeader.X_CACHE_INFO;
import static com.adobe.project.headers.HttpResponseHeader.X_FRAME_OPTIONS;
import static com.adobe.project.headers.HttpResponseHeader.X_KUMA_REVISION;
import static org.assertj.core.api.Assertions.assertThat;

public class HttpResponseTests {

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

    @DisplayName("Test the construction of the HttpResponse")
    @Test
    public void HttpResponseConstructorTest() {
        // given
        HttpResponseHeader httpResponseHeader = new HttpResponseHeader(testHeadersMap);
        HttpResponse httpResponse = HttpResponse.builder()
            .httpResponseHeader(httpResponseHeader)
            .responseCode(ResponseCodes.OK)
            .contentBody("TEST_BODY")
            .build();

        // when
        assertThat(httpResponse.getContentBody()).isEqualTo("TEST_BODY");
        assertThat(httpResponse.getVersion()).isEqualTo("HTTP/1.1");
        assertThat(httpResponse.getHttpResponseHeader()).isEqualTo(httpResponseHeader);
        assertThat(httpResponse.getResponseCode()).isEqualTo(ResponseCodes.OK);

    }

    @DisplayName("Test the toString method of the HttpResponse")
    @Test
    public void HttpResponseToStringTest() {
        // given
        String expectedStringFormat =
                "HTTP/1.1 200 OK" + System.lineSeparator() +
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
                "x-frame-options: TestFrameOptions" + System.lineSeparator() + System.lineSeparator();
        HttpResponseHeader httpResponseHeader = new HttpResponseHeader(testHeadersMap);
        HttpResponse httpResponse = HttpResponse.builder()
            .httpResponseHeader(httpResponseHeader)
            .responseCode(ResponseCodes.OK)
            .contentBody("TEST_BODY")
            .build();

        // when
        assertThat(httpResponse.toString()).isEqualTo(expectedStringFormat);

    }
}
