package com.adobe.project.response;

import java.net.URI;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Optional;

import com.adobe.project.headers.HttpHeader;
import com.adobe.project.headers.HttpResponseHeader;
import com.adobe.project.request.HttpRequest;
import com.adobe.project.request.HttpServerSupportedMethods;
import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;
import static io.vavr.API.$;
import static io.vavr.API.Case;
import static io.vavr.API.Match;
import static io.vavr.Predicates.instanceOf;

/**
 * @author <a href="mailto:amansinh@gmail.com">Aman Sinha</a>
 *
 * This is a static class which creates a HttpResponse for a given {@link HttpRequest}. There is a switching logic inside the class
 * which reads the operation in {@link HttpRequest} and then produces the {@link HttpResponse}.
 */

@Slf4j
public final class HttpResponseGenerator {

    private static final String DEFAULT_FILE = "/index.html";
    private static final String FILE_NOT_FOUND = "/page_404.html";
    private static final String METHOD_NOT_SUPPORTED = "/not_supported.html";
    private static final String RESPONSE_SERVER_NAME = "Adobe Assessment Server";

    private HttpResponseGenerator() {

    }

    public static HttpResponse createHttpResponse(final HttpRequest httpRequest, final String webRoot) {

        return Try.of(() -> {
            HttpServerSupportedMethods method = HttpServerSupportedMethods.valueOf(httpRequest.getHttpRequestOperationLine().getMethod());
            URI requestedURI = URI.create(httpRequest.getHttpRequestOperationLine().getPath());
            return responseSelector(method, requestedURI, webRoot);
        }).recover(e -> Match(e).of(
            Case($(instanceOf(IllegalArgumentException.class)), createUnimplementedHttpResponse(webRoot)),
            Case($(instanceOf(NullPointerException.class)), createServerErrorResponse())
        )).get();

    }

    private static HttpResponse responseSelector(HttpServerSupportedMethods method, URI requestURI, String webRoot) {

        switch (method) {
            case GET:
                return createGetHttpResponse(webRoot, requestURI.getPath());
            case HEAD:
                return createHeadHttpResponse(requestURI.getPath(), Optional.empty());
            default:
                return createUnimplementedHttpResponse(webRoot);
        }

    }

    private static HttpResponse createGetHttpResponse(String webRoot, String fileRequested) {
        String fileLocation = webRoot + fileRequested;
        if (fileLocation.endsWith("/")) {
            fileLocation += DEFAULT_FILE;
        }
        final Path filePath = Paths.get(fileLocation);
        log.info("Request to get the file at: " + filePath.toString());
        return Try.of(() -> {
            String fileData = new String(Files.readAllBytes(filePath));
            return createHeadHttpResponse(filePath.toString(), Optional.of(fileData));
        }).recover(e -> Match(e).of(
            Case($(instanceOf(NoSuchFileException.class)), create404HttpResponse(webRoot, filePath))
        )).get();

    }

    private static HttpResponse createHeadHttpResponse(String fileLocation, Optional<String> fileData) {

        String contentType = getContentType(fileLocation);

        HttpResponseHeader httpResponseHeader = new HttpResponseHeader();
        httpResponseHeader.setHeaders(HttpResponseHeader.SERVER, RESPONSE_SERVER_NAME);
        httpResponseHeader.setHeaders(HttpHeader.DATE, (new Date()).toString());
        httpResponseHeader.setHeaders(HttpResponseHeader.CONTENT_TYPE, contentType);
        httpResponseHeader.setHeaders(HttpResponseHeader.CONTENT_LENGTH, String.valueOf(fileData.orElse("").length()));


        return HttpResponse.builder()
            .responseCode(ResponseCodes.OK)
            .contentBody(fileData.orElse(""))
            .httpResponseHeader(httpResponseHeader)
            .build();
    }

    private static HttpResponse createUnimplementedHttpResponse(final String webRoot) {

        String fileData = Try.of(
            ()-> new String(Files.readAllBytes(Paths.get(webRoot + METHOD_NOT_SUPPORTED)))
        ).recover(e -> Match(e).of(
            Case($(instanceOf(NoSuchFileException.class)), "Method not supported")
        )).get();

        HttpResponseHeader httpResponseHeader = setErrorResponseHeaders(fileData);

        return HttpResponse.builder()
            .responseCode(ResponseCodes.NOT_IMPLEMENTED)
            .contentBody(fileData)
            .httpResponseHeader(httpResponseHeader)
            .build();
    }

    private static HttpResponseHeader setErrorResponseHeaders(String fileData) {
        HttpResponseHeader httpResponseHeader = new HttpResponseHeader();
        httpResponseHeader.setHeaders(HttpResponseHeader.SERVER, RESPONSE_SERVER_NAME);
        httpResponseHeader.setHeaders(HttpResponseHeader.DATE, (new Date()).toString());
        httpResponseHeader.setHeaders(HttpResponseHeader.CONTENT_TYPE, MimeType.HTML.getMimeTypeFormat());
        httpResponseHeader.setHeaders(HttpResponseHeader.CONTENT_LENGTH, String.valueOf(fileData.length()));
        return httpResponseHeader;
    }

    private static HttpResponse create404HttpResponse(final String webRoot, Path filePath) {

        log.info("Requested file not found: " + filePath.toString());

        final Path fileNotFoundPath = Paths.get(webRoot + FILE_NOT_FOUND);

        log.info("Accessing the file " + fileNotFoundPath.toString() + " to return contents");

        String fileData = Try.of(
            ()-> new String(Files.readAllBytes(fileNotFoundPath))
        ).recover(e -> Match(e).of(
            Case($(instanceOf(NoSuchFileException.class)), "File not found")
        )).get();

        HttpResponseHeader httpResponseHeader = setErrorResponseHeaders(fileData);

        return HttpResponse.builder()
            .responseCode(ResponseCodes.NOT_FOUND)
            .contentBody(fileData)
            .httpResponseHeader(httpResponseHeader)
            .build();
    }

    private static HttpResponse createServerErrorResponse() {

        HttpResponseHeader httpResponseHeader = new HttpResponseHeader();
        httpResponseHeader.setHeaders(HttpResponseHeader.SERVER, RESPONSE_SERVER_NAME);
        httpResponseHeader.setHeaders(HttpResponseHeader.DATE, (new Date()).toString());
        httpResponseHeader.setHeaders(HttpResponseHeader.CONTENT_TYPE, MimeType.PLAIN.getMimeTypeFormat());
        httpResponseHeader.setHeaders(HttpResponseHeader.CONTENT_LENGTH, String.valueOf("".length()));


        return HttpResponse.builder()
            .responseCode(ResponseCodes.INTERNAL_ERROR)
            .contentBody("")
            .httpResponseHeader(httpResponseHeader)
            .build();
    }

    private static String getContentType(String fileRequested) {
        if (fileRequested.endsWith(".htm")  ||  fileRequested.endsWith(".html"))
            return MimeType.HTML.getMimeTypeFormat();
        else
            return MimeType.PLAIN.getMimeTypeFormat();
    }

    public static void addKeepAliveHeaders(HttpRequest httpRequest, HttpResponseHeader httpResponseHeader, int timeoutMillis, int maxRequests) {
        boolean keepAlive = httpRequest.getHttpRequestHeader().getHeadersMap()
            .getOrDefault(HttpHeader.CONNECTION, "keep-alive").toLowerCase().equals("keep-alive");
        if (keepAlive) {
            httpResponseHeader.setHeaders(HttpResponseHeader.KEEP_ALIVE, String.format("timeout=%d, max=%d", timeoutMillis/1000, maxRequests));
        }
        httpResponseHeader.setHeaders(HttpHeader.CONNECTION, httpRequest.getHttpRequestHeader()
            .getHeadersMap().getOrDefault(HttpHeader.CONNECTION, "keep-alive"));
    }
}
