package com.adobe.project.server;

import java.io.IOException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class HttpServerTests {

    @DisplayName("HttpServer constructor test with all arguments")
    @Test
    public void testHttpServerConstructor() {
        // given

        // when
        HttpServer httpServer = new HttpServer(8080, "./pages/pages_1");

        // then
        assertThat(httpServer.getServerState()).isEqualTo(HttpServer.ServerState.INITIALIZED);
        assertThat(httpServer.getDirectoryPath()).isEqualTo("./pages/pages_1");
        assertThat(httpServer.getPort()).isEqualTo(8080);
    }

    @DisplayName("HttpServer constructor test with default port")
    @Test
    public void testHttpServerConstructorWithDefaultPort() {
        // given

        // when
        HttpServer httpServer = new HttpServer("./pages/pages_1");

        // then
        assertThat(httpServer.getServerState()).isEqualTo(HttpServer.ServerState.INITIALIZED);
        assertThat(httpServer.getDirectoryPath()).isEqualTo("./pages/pages_1");
        assertThat(httpServer.getPort()).isEqualTo(8080);
    }

    @DisplayName("HttpServer start test with default port")
    @Test
    public void testHttpServerStartWithDefaultPort() throws IOException {
        // given
        HttpServer httpServer = new HttpServer("./pages/pages_1");

        // when
        httpServer.start();

        // then
        assertThat(httpServer.getServerState()).isEqualTo(HttpServer.ServerState.RUNNING);
        assertThat(httpServer.getServerSocket()).isNotNull();
        assertThat(httpServer.getServerThread()).isNotNull();

        // finally
        httpServer.stop();
    }

    @DisplayName("HttpServer stop test with default port")
    @Test
    public void testHttpServerStopWithDefaultPort() throws IOException {
        // given
        HttpServer httpServer = new HttpServer("./pages/pages_1");

        // when
        httpServer.start();

        // then
        assertThat(httpServer.getServerState()).isEqualTo(HttpServer.ServerState.RUNNING);

        // when
        httpServer.stop();

        // then
        assertThat(httpServer.getServerState()).isEqualTo(HttpServer.ServerState.STOPPED);

    }
}
