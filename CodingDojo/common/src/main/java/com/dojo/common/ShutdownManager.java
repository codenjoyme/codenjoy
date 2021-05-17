package com.dojo.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public final class ShutdownManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(ShutdownManager.class);

    private final ApplicationContext applicationContext;

    private final ScheduledExecutorService waitingExecutorService;

    public ShutdownManager(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        this.waitingExecutorService = Executors.newSingleThreadScheduledExecutor();
    }

    @PostConstruct
    private void initialize() {
        final int delay = 30;
        LOGGER.info("Application will stop in {} minutes!", delay);
        waitingExecutorService.schedule(
                () -> SpringApplication.exit(applicationContext, () -> 0),
                delay,
                TimeUnit.MINUTES);
    }

    @PreDestroy
    private void destroy() {
        waitingExecutorService.shutdownNow();
    }

}
