package com.adobe.project.server;

import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerExecutorService {

    private static final int MAX_THREADS = 20;

    private static volatile ExecutorService executorService;

    private static ExecutorService createExecutorService() {
        executorService = Executors.newFixedThreadPool(MAX_THREADS);
        return executorService;
    }

    public static ExecutorService getServerExecutorService() {
        return Optional.ofNullable(executorService)
            .filter(service -> !service.isTerminated())
            .orElseGet(ServerExecutorService::createExecutorService);
    }
}
