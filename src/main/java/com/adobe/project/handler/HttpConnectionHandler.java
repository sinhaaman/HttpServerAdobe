package com.adobe.project.handler;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;

import com.adobe.project.io.HttpRequestParser;
import com.adobe.project.request.HttpRequest;
import com.adobe.project.response.HttpResponse;
import com.adobe.project.response.HttpResponseGenerator;
import lombok.extern.slf4j.Slf4j;

import static com.adobe.project.headers.HttpHeader.CONNECTION;
import static com.adobe.project.response.HttpResponseGenerator.addKeepAliveHeaders;
import static com.adobe.project.response.HttpResponseGenerator.createHttpResponse;
import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * @author <a href="mailto:amansinh@gmail.com">Aman Sinha</a>
 *
 * This is the connection handler class which reads the request to the given port using the {@link HttpRequestParser}
 * and then return the appropriate response to the server using the {@link HttpResponseGenerator}.
 *
 * This handler also has keep-alive functionality with the max-request and socket read-timeout.
 */

@Slf4j
public class HttpConnectionHandler implements Runnable {

    private final Socket socket;

    private boolean keepAlive = true;

    private static final int keepAliveTimeoutInMillis = 10000;

    private static final int maxRequests = 5;

    private final String directoryPath;

    public HttpConnectionHandler(final Socket socket, final String directoryPath) {
        this.socket = socket;
        this.directoryPath = directoryPath;
    }

    @Override
    public void run() {
        handleConnection();
        closeSocket();
    }

    private void handleConnection() {
        try (InputStream inputStream = socket.getInputStream();
             OutputStream outputStream = socket.getOutputStream()) {
            HttpRequestParser requestReader = new HttpRequestParser(inputStream);
            int requests = 0;

            while (keepAlive && (requests < maxRequests)) {
                try {
                    BufferedOutputStream dataOut = new BufferedOutputStream(outputStream);

                    HttpRequest httpRequest = requestReader.parseHttpRequest();
                    log.info("Serving request from socket: " + socket.getRemoteSocketAddress().toString());
                    log.info("The request received: " + httpRequest.getHttpRequestOperationLine().toString());

                    socket.setSoTimeout(keepAliveTimeoutInMillis);
                    ++requests;
                    handleKeepAliveBehaviour(httpRequest);

                    HttpResponse httpResponse = createHttpResponse(httpRequest, directoryPath);

                    addKeepAliveHeaders(httpRequest, httpResponse.getHttpResponseHeader(),
                        keepAliveTimeoutInMillis, maxRequests);

                    dataOut.write(httpResponse.toString().getBytes(UTF_8));
                    dataOut.write(httpResponse.getContentBody().getBytes(UTF_8));
                    dataOut.flush();

                } catch (SocketTimeoutException e) {
                    log.info("Socket timed out");
                    return;
                } catch (IOException ioe) {
                    log.error("Server error : " + ioe.getMessage());
                    return;
                }
            }
            if (requests == maxRequests) {
                log.info("The maximum request limit reached on the socket: " + socket.getRemoteSocketAddress());
            }
        }
        catch (IOException exception) {
            log.error("Problem getting the server io stream" + exception.getMessage());
        }
    }

    private void closeSocket() {
        try {
            log.info("Closing the socket");
            socket.close();
        } catch (IOException e) {
            log.error("Encountered exception while closing the socket" + e.getMessage());
        }
    }

    private void handleKeepAliveBehaviour(HttpRequest httpRequest) {
        this.keepAlive = httpRequest.getHttpRequestHeader()
            .getHeadersMap()
            .getOrDefault(CONNECTION, "keep-alive")
            .toLowerCase()
            .equals("keep-alive");
    }

}
