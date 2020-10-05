package com.adobe.project.server;

import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author <a href="mailto:amansinh@gmail.com">Aman Sinha</a>
 *
 * {@link ServerExecutorService} class returns a {@link ExecutorService} with the defined threadpool size.
 *
 */
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
