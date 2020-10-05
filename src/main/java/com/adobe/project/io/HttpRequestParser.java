package com.adobe.project.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import com.adobe.project.headers.HttpRequestHeader;
import com.adobe.project.request.HttpRequest;
import com.adobe.project.request.HttpRequestOperationLine;
import lombok.extern.slf4j.Slf4j;

/**
 * Holds the input stream and keeps listening to it. This class has a blocking call. Once the inputstream receives data,
 * it parses the data and creates a HttpRequest to be sent to the caller.
 * e.g. {@link com.adobe.project.handler.HttpConnectionHandler} uses the HttpRequestParser and the parseHttpRequest
 * method.
 */

@Slf4j
public class HttpRequestParser {

    private final InputStream inputStream;

    private static final String SPACE_SEPARATOR = " ";

    private static final String COLON_SEPARATOR = ":";

    public HttpRequestParser(final InputStream inputStream) {
        this.inputStream = inputStream;
    }

    /**
     * Listens to the input stream for incoming requests, parses it and convert it a {@link HttpRequest}.
     * @return A parsed {@link HttpRequest} from the {@link InputStream}
     * @throws IOException In case there is an issue with the buffer or socket.
     */
    public HttpRequest parseHttpRequest() throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        try {
            String httpOperationLineInput;
            while((httpOperationLineInput = bufferedReader.readLine()) == null);
            HttpRequestOperationLine httpRequestOperationLine = parseHttpRequestOperationLine(httpOperationLineInput);

            List<String> headers = new ArrayList<>();
            if (bufferedReader.ready()) {
                String line = bufferedReader.readLine();
                while (line != null && !"".equals(line)) {
                    headers.add(line);
                    line = bufferedReader.readLine();
                }
            }
            HttpRequestHeader httpRequestHeader = parseHttpRequestHeader(headers);
            return HttpRequest.builder()
                .httpRequestOperationLine(httpRequestOperationLine)
                .httpRequestHeader(httpRequestHeader)
                .build();
        } catch (SocketTimeoutException e) {
            throw e;
        } catch (Exception e) {
            log.error("Request parsing error. " + e.getMessage());
            throw e;
        }
    }

    private HttpRequestHeader parseHttpRequestHeader(final List<String> headers) {
        Map<String, String> headersMap = new HashMap<>();
        headers.stream()
            .filter(Objects::nonNull)
            .filter(s -> !s.isEmpty())
            .map(s -> s.split(COLON_SEPARATOR, 2))
            .filter(s -> s.length == 2)
            .forEach(s -> headersMap.put(s[0].trim(), s[1].trim()));

        return new HttpRequestHeader(headersMap);
    }

    private HttpRequestOperationLine parseHttpRequestOperationLine(final String input) {
        return Optional.ofNullable(input)
            .map(s -> s.split(SPACE_SEPARATOR, 3))
            .filter(s -> s.length == 3)
            .map(s -> HttpRequestOperationLine.builder()
                .method(s[0].toUpperCase().trim())
                .path(s[1].trim())
                .version(s[2].trim())
                .build())
            .orElse(HttpRequestOperationLine.builder().build());
    }
}
