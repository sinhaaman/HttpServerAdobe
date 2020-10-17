package com.adobe.project.handler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import com.adobe.project.server.ServerExecutorService;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.slf4j.LoggerFactory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class HttpConnectionHandlerTests {

    @Mock
    private Socket socket;

    @Mock
    private SocketAddress socketAddress;

    private static final String SOCKET_ADDRESS = "TEST_ADDRESS";

    private HttpConnectionHandler httpConnectionHandler;

    @BeforeEach
    public void setUp() throws IOException {
        httpConnectionHandler = new HttpConnectionHandler(socket, "./pages/pages_1");
        doReturn(socketAddress).when(socket).getRemoteSocketAddress();
        doReturn(SOCKET_ADDRESS).when(socketAddress).toString();
        doNothing().when(socket).setSoTimeout(anyInt());
    }

    @DisplayName("ConnectionHandler successful run with default keep-alive behaviour")
    @Test
    public void testRunProvidedValidRequestKeepAliveDefault() throws IOException, InterruptedException {
        // given
        InputStream inputStream = IOUtils.toInputStream("GET / HTTP/1.1", "UTF-8");
        OutputStream outputStream = mock(OutputStream.class);
        doReturn(inputStream).when(socket).getInputStream();
        doReturn(outputStream).when(socket).getOutputStream();

        // when
        Executors.newFixedThreadPool(2).execute(httpConnectionHandler);
        ServerExecutorService.getServerExecutorService().awaitTermination(1, TimeUnit.SECONDS);

        // then
        verify(socket, times(0)).close();
        verify(outputStream).write(any(byte[].class), anyInt(), anyInt());
        verify(outputStream, times(1)).flush();

    }

    @DisplayName("ConnectionHandler successful run with explicit keep-alive connection request header")
    @Test
    public void testRunProvidedValidRequestWithKeepAliveHeader() throws IOException, InterruptedException {
        // given
        InputStream inputStream = IOUtils.toInputStream("GET / HTTP/1.1 \r\n" + "Connection: keep-alive\r\n", "UTF-8");
        OutputStream outputStream = mock(OutputStream.class);
        doReturn(inputStream).when(socket).getInputStream();
        doReturn(outputStream).when(socket).getOutputStream();

        // when
        Executors.newFixedThreadPool(2).execute(httpConnectionHandler);
        ServerExecutorService.getServerExecutorService().awaitTermination(1, TimeUnit.SECONDS);

        // then
        verify(socket, times(0)).close();
        verify(outputStream).write(any(byte[].class), anyInt(), anyInt());
        verify(outputStream, times(1)).flush();

    }

    @DisplayName("ConnectionHandler successful run with explicit close connection request header")
    @Test
    public void testRunProvidedValidRequestWithCloseHeader() throws IOException, InterruptedException {
        // given
        InputStream inputStream = IOUtils.toInputStream("GET / HTTP/1.1 \r\n" + "Connection: closed\r\n", "UTF-8");
        OutputStream outputStream = mock(OutputStream.class);
        doReturn(inputStream).when(socket).getInputStream();
        doReturn(outputStream).when(socket).getOutputStream();

        // when
        Executors.newFixedThreadPool(2).execute(httpConnectionHandler);
        ServerExecutorService.getServerExecutorService().awaitTermination(1, TimeUnit.SECONDS);

        // then
        verify(socket, times(1)).close();
        verify(outputStream).write(any(byte[].class), anyInt(), anyInt());
        verify(outputStream, times(1)).flush();

    }

    @DisplayName("ConnectionHandler run with socket closing exception")
    @Test
    public void testExceptionLoggedWhileClosingSocket() throws IOException, InterruptedException {
        // given
        List<ILoggingEvent> iLoggingEventList = setUpLogTracker(HttpConnectionHandler.class);

        doThrow(new IOException(" Test Socket IO exception")).when(socket).close();
        InputStream inputStream = IOUtils.toInputStream("GET / HTTP/1.1 \r\n" + "Connection: closed\r\n", "UTF-8");
        OutputStream outputStream = mock(OutputStream.class);
        doReturn(inputStream).when(socket).getInputStream();
        doReturn(outputStream).when(socket).getOutputStream();

        // when
        Executors.newFixedThreadPool(2).execute(httpConnectionHandler);
        ServerExecutorService.getServerExecutorService().awaitTermination(1, TimeUnit.SECONDS);

        // then
        verify(socket, times(1)).close();
        verify(outputStream).write(any(byte[].class), anyInt(), anyInt());
        verify(outputStream, times(1)).flush();
        assertThat(iLoggingEventList.get(iLoggingEventList.size()-1).getMessage()).isEqualTo("Encountered exception while closing the socket Test Socket IO exception");

    }

    @DisplayName("ConnectionHandler run with IO Stream exception")
    @Test
    public void testIOExceptionLoggedWhileHandlingConnection() throws IOException, InterruptedException {
        // given
        List<ILoggingEvent> iLoggingEventList = setUpLogTracker(HttpConnectionHandler.class);

        doThrow(new IOException(" Test IO exception")).when(socket).getInputStream();

        // when
        Executors.newFixedThreadPool(2).execute(httpConnectionHandler);
        ServerExecutorService.getServerExecutorService().awaitTermination(1, TimeUnit.SECONDS);

        // then
        verify(socket, times(1)).close();
        assertThat(iLoggingEventList.get(0).getMessage()).isEqualTo("Problem getting the server io stream Test IO exception");

    }

    @DisplayName("ConnectionHandler run with Socket exception")
    @Test
    public void testSocketExceptionLoggedWhileHandlingConnection() throws IOException, InterruptedException {
        // given
        List<ILoggingEvent> iLoggingEventList = setUpLogTracker(HttpConnectionHandler.class);

        InputStream inputStream = IOUtils.toInputStream("GET / HTTP/1.1 \r\n" + "Connection: closed\r\n", "UTF-8");
        OutputStream outputStream = mock(OutputStream.class);
        doReturn(inputStream).when(socket).getInputStream();
        doReturn(outputStream).when(socket).getOutputStream();

        doThrow(new SocketException("Test IO exception")).when(socket).setSoTimeout(anyInt());

        // when
        Executors.newFixedThreadPool(2).execute(httpConnectionHandler);
        ServerExecutorService.getServerExecutorService().awaitTermination(1, TimeUnit.SECONDS);

        // then
        verify(socket, times(1)).close();
        assertThat(iLoggingEventList.get(2).getMessage()).isEqualTo("Server error : Test IO exception");

    }

    public static List<ILoggingEvent> setUpLogTracker(Class clazz) {
        // get Logback Logger
        Logger classLogger = (Logger) LoggerFactory.getLogger(clazz);

        // create and start a ListAppender
        ListAppender<ILoggingEvent> listAppender = new ListAppender<>();
        listAppender.start();

        // add the appender to the logger
        // addAppender is outdated now
        classLogger.addAppender(listAppender);

        return listAppender.list;
    }



}
