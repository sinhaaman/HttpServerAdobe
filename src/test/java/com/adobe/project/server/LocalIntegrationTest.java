package com.adobe.project.server;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;

import com.adobe.project.response.ResponseCodes;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class LocalIntegrationTest {

    @DisplayName("Local integration test")
    @Test
    //@Disabled("Needs to be run only if integration test is required")
    public void localIntegrationTest() throws IOException {
        // given
        HttpServer httpServer = new HttpServer("./pages/pages_1");
        httpServer.start();
        String url = "http://localhost:8080/";

        // when
        HttpURLConnection httpConnection = (HttpURLConnection) new URL(url).openConnection();

        // then
        int status = httpConnection.getResponseCode();
        assertThat(Arrays.stream(ResponseCodes.values()).anyMatch(s -> s.getStatusCode() == status)).isTrue();
        assertThat(httpConnection.getHeaderField("Server")).isEqualTo("Adobe Assessment Server");

        // finally
        httpServer.stop();


    }
}
