package com.adobe.project.server;

import java.util.concurrent.ExecutorService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ServerExecutorServiceTests {

    @DisplayName("Get the server executor service")
    @Test
    public void getServerExecutorServiceTest() {

        // given

        // when
        ExecutorService executorService = ServerExecutorService.getServerExecutorService();

        // then
        assertThat(executorService).isNotNull();
        assertThat(executorService.isTerminated()).isFalse();

    }
}
