package com.adobe.project.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.adobe.project.handler.HttpConnectionHandler;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author <a href="mailto:amansinh@gmail.com">Aman Sinha</a>
 *
 * This class is responsible for starting, stopping the server and creating {@link HttpConnectionHandler}  to serve the request
 * passed to the server socket.
 */

@Getter
@Slf4j
public class HttpServer implements Runnable {

    public enum ServerState {
        INITIALIZED,
        RUNNING,
        STOPPED
    }

    private static final int DEFAULT_PORT = 8080;

    private ServerSocket serverSocket;
    private final int port;

    private volatile ServerState serverState;

    private Thread serverThread;

    private final String directoryPath;

    public HttpServer(int port, String directoryPath) {
        this.port = port;
        this.serverState = ServerState.INITIALIZED;
        this.directoryPath = directoryPath;
    }

    public HttpServer(String directoryPath) {
      this(DEFAULT_PORT, directoryPath);
    }

    @Override
    public void run() {
        log.info("Starting server at the port: " + this.port);

        while (!serverState.equals(ServerState.STOPPED)) {
            try {
                final Socket socket = serverSocket.accept();
                log.info("Accepted a connection");

                HttpConnectionHandler handler = new HttpConnectionHandler(socket, directoryPath);
                ServerExecutorService.getServerExecutorService().execute(handler);
            } catch (IOException e) {
                log.error("Server Error: " + e.getMessage());
            }
        }
    }

    public ServerState start() {
        if (!serverState.equals(ServerState.RUNNING)){
            try {
                    serverSocket = new ServerSocket(port);
                    serverThread = new Thread(this);
                    serverThread.start();
                    serverState = ServerState.RUNNING;
            } catch (IOException exception) {
                log.error("Unable to start the server due to: " + exception.getMessage());
            }
        }

        return serverState;
    }

    public void stop() throws IOException {
        stopServerThread();
        serverSocket.close();
        log.info("Server is stopped");
    }

    private void stopServerThread() {
        serverState = ServerState.STOPPED;
        ServerExecutorService.getServerExecutorService().shutdownNow();
        serverThread.interrupt();
    }

}
