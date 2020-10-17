package com.adobe.project.response;

import java.util.Map;

import com.adobe.project.headers.HttpHeader;
import com.adobe.project.headers.HttpRequestHeader;
import com.adobe.project.headers.HttpResponseHeader;
import com.adobe.project.request.HttpRequest;
import com.adobe.project.request.HttpRequestOperationLine;
import com.google.common.collect.ImmutableMap;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class HttpResponseGeneratorTests {

    @Mock
    private HttpRequestOperationLine httpRequestOperationLine;

    @Mock
    private HttpRequestHeader httpRequestHeader;

    @InjectMocks
    private HttpRequest httpRequest;

    private static final String SERVER_NAME = "Adobe Assessment Server";

    private static final String SERVER_VERSION = "HTTP/1.1";

    @DisplayName("Test get method with a malformed Http Request.")
    @Test
    public void testGetMethodWithEmptyHttpRequestReturnsServerErrorResponse() {

        // given
        doReturn("GET").when(httpRequestOperationLine).getMethod();

        // when
        HttpResponse httpResponse = HttpResponseGenerator.createHttpResponse(httpRequest, "/test");

        // then
        assertThat(httpResponse.getResponseCode()).isEqualTo(ResponseCodes.INTERNAL_ERROR);

    }

    @DisplayName("Test unimplemented method of the server.")
    @Test
    public void testUnimplementedMethodReturnsUnimplementedMethodResponse() {

        // given
        doReturn("TEST").when(httpRequestOperationLine).getMethod();

        // when
        HttpResponse httpResponse = HttpResponseGenerator.createHttpResponse(httpRequest, "/test");

        // then
        assertThat(httpResponse.getVersion()).isEqualTo(SERVER_VERSION);
        assertThat(httpResponse.getResponseCode()).isEqualTo(ResponseCodes.NOT_IMPLEMENTED);
        assertThat(httpResponse.getHttpResponseHeader()).isNotNull();
        assertThat(httpResponse.getHttpResponseHeader().getHeadersMap().get(HttpResponseHeader.SERVER))
            .isEqualTo(SERVER_NAME);
        assertThat(httpResponse.getHttpResponseHeader().getHeadersMap().get(HttpResponseHeader.CONTENT_TYPE))
            .isEqualTo(MimeType.HTML.getMimeTypeFormat());

    }

    @DisplayName("Test GET method on invalid file.")
    @Test
    public void testGetMethodWithInvalidFileReturnsFileNotFoundMethodResponse() {

        // given
        doReturn("GET").when(httpRequestOperationLine).getMethod();
        doReturn("/TEST_PATH").when(httpRequestOperationLine).getPath();

        // when
        HttpResponse httpResponse = HttpResponseGenerator.createHttpResponse(httpRequest, "/test");

        // then
        assertThat(httpResponse.getVersion()).isEqualTo(SERVER_VERSION);
        assertThat(httpResponse.getResponseCode()).isEqualTo(ResponseCodes.NOT_FOUND);
        assertThat(httpResponse.getHttpResponseHeader()).isNotNull();
        assertThat(httpResponse.getHttpResponseHeader().getHeadersMap().get(HttpResponseHeader.SERVER))
            .isEqualTo(SERVER_NAME);
        assertThat(httpResponse.getHttpResponseHeader().getHeadersMap().get(HttpResponseHeader.CONTENT_TYPE))
            .isEqualTo(MimeType.HTML.getMimeTypeFormat());

    }

    @DisplayName("Test GET method on a directory path with a valid file.")
    @Test
    public void testGetMethodWithDirectoryReturnsIndexHtmlFileResponse() {

        // given
        doReturn("GET").when(httpRequestOperationLine).getMethod();
        doReturn("/").when(httpRequestOperationLine).getPath();

        // when
        HttpResponse httpResponse = HttpResponseGenerator.createHttpResponse(httpRequest, "./pages/pages_1");

        // then
        assertThat(httpResponse.getVersion()).isEqualTo(SERVER_VERSION);
        assertThat(httpResponse.getResponseCode()).isEqualTo(ResponseCodes.OK);
        assertThat(httpResponse.getHttpResponseHeader()).isNotNull();
        assertThat(httpResponse.getHttpResponseHeader().getHeadersMap().get(HttpResponseHeader.SERVER))
            .isEqualTo(SERVER_NAME);
        assertThat(httpResponse.getHttpResponseHeader().getHeadersMap().get(HttpResponseHeader.CONTENT_TYPE))
            .isEqualTo(MimeType.HTML.getMimeTypeFormat());

    }

    @DisplayName("Test HEAD method returns OK response in plain mime-type.")
    @Test
    public void testHeadMethodReturnsOKResponse() {

        // given
        doReturn("HEAD").when(httpRequestOperationLine).getMethod();
        doReturn("/TEST_PATH").when(httpRequestOperationLine).getPath();

        // when
        HttpResponse httpResponse = HttpResponseGenerator.createHttpResponse(httpRequest, "/test");

        // then
        assertThat(httpResponse.getVersion()).isEqualTo(SERVER_VERSION);
        assertThat(httpResponse.getResponseCode()).isEqualTo(ResponseCodes.OK);
        assertThat(httpResponse.getContentBody()).isBlank();
        assertThat(httpResponse.getHttpResponseHeader()).isNotNull();
        assertThat(httpResponse.getHttpResponseHeader().getHeadersMap().get(HttpResponseHeader.SERVER))
            .isEqualTo(SERVER_NAME);
        assertThat(httpResponse.getHttpResponseHeader().getHeadersMap().get(HttpResponseHeader.CONTENT_TYPE))
            .isEqualTo(MimeType.PLAIN.getMimeTypeFormat());
        assertThat(Integer.valueOf(httpResponse.getHttpResponseHeader().getHeadersMap().get(HttpResponseHeader.CONTENT_LENGTH)))
            .isEqualTo(0);
    }

    @DisplayName("Test HEAD method returns OK response in html mime-type.")
    @Test
    public void testHeadMethodWithContentTypeReturnsOKResponse() {

        // given
        doReturn("HEAD").when(httpRequestOperationLine).getMethod();
        doReturn("/TEST_PATH.html").when(httpRequestOperationLine).getPath();

        // when
        HttpResponse httpResponse = HttpResponseGenerator.createHttpResponse(httpRequest, "/test");

        // then
        assertThat(httpResponse.getVersion()).isEqualTo(SERVER_VERSION);
        assertThat(httpResponse.getResponseCode()).isEqualTo(ResponseCodes.OK);
        assertThat(httpResponse.getContentBody()).isBlank();
        assertThat(httpResponse.getHttpResponseHeader()).isNotNull();
        assertThat(httpResponse.getHttpResponseHeader().getHeadersMap().get(HttpResponseHeader.SERVER))
            .isEqualTo(SERVER_NAME);
        assertThat(httpResponse.getHttpResponseHeader().getHeadersMap().get(HttpResponseHeader.CONTENT_TYPE))
            .isEqualTo(MimeType.HTML.getMimeTypeFormat());
        assertThat(Integer.valueOf(httpResponse.getHttpResponseHeader().getHeadersMap().get(HttpResponseHeader.CONTENT_LENGTH)))
            .isEqualTo(0);
    }

    @DisplayName("Test keep-alive in the response headers provided in the request header")
    @Test
    public void testExplicitKeepAliveHeaders() {

        // given
        HttpResponseHeader httpResponseHeader = mock(HttpResponseHeader.class);
        final Map<String, String> httpRequestHeadersMap = ImmutableMap.of(
            HttpHeader.CONNECTION, "keep-alive"
        );
        doReturn(httpRequestHeadersMap).when(httpRequestHeader).getHeadersMap();

        // when
        HttpResponseGenerator.addKeepAliveHeaders(httpRequest, httpResponseHeader, 1000, 10);

        // then
        verify(httpResponseHeader, times(1)).setHeaders(HttpResponseHeader.KEEP_ALIVE, String.format("timeout=%d, max=%d", 1, 10));
        verify(httpResponseHeader, times(1)).setHeaders(HttpResponseHeader.CONNECTION, "keep-alive");
        verify(httpResponseHeader, times(2)).setHeaders(anyString(), anyString());
    }

    @DisplayName("Test keep-alive default behaviour")
    @Test
    public void testDefaultKeepAliveBehaviour() {

        // given
        HttpResponseHeader httpResponseHeader = mock(HttpResponseHeader.class);
        final Map<String, String> httpRequestHeadersMap = ImmutableMap.of();
        doReturn(httpRequestHeadersMap).when(httpRequestHeader).getHeadersMap();

        // when
        HttpResponseGenerator.addKeepAliveHeaders(httpRequest, httpResponseHeader, 1000, 10);

        // then
        verify(httpResponseHeader, times(1)).setHeaders(HttpResponseHeader.KEEP_ALIVE, String.format("timeout=%d, max=%d", 1, 10));
        verify(httpResponseHeader, times(1)).setHeaders(HttpResponseHeader.CONNECTION, "keep-alive");
        verify(httpResponseHeader, times(2)).setHeaders(anyString(), anyString());
    }

    @DisplayName("Test keep-alive close behaviour in header")
    @Test
    public void testKeepAliveCloseBehaviour() {

        // given
        HttpResponseHeader httpResponseHeader = mock(HttpResponseHeader.class);
        final Map<String, String> httpRequestHeadersMap = ImmutableMap.of(
            HttpHeader.CONNECTION, "close"
        );
        doReturn(httpRequestHeadersMap).when(httpRequestHeader).getHeadersMap();

        // when
        HttpResponseGenerator.addKeepAliveHeaders(httpRequest, httpResponseHeader, 1000, 10);

        // then

        verify(httpResponseHeader, times(1)).setHeaders(HttpResponseHeader.CONNECTION, "close");
        verify(httpResponseHeader, times(1)).setHeaders(anyString(), anyString());

    }

}
