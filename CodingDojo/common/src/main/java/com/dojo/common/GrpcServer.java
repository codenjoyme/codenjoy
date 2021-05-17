package com.dojo.common;

import io.grpc.BindableService;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.netty.shaded.io.grpc.netty.NettyServerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

public final class GrpcServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(GrpcServer.class);

    private final Server server;

    private final ExecutorService applicationExecutor;


    public GrpcServer(final int applicationExecutorPoolSize, final int port, final BindableService... services) {
        applicationExecutor = Executors.newFixedThreadPool(applicationExecutorPoolSize);

        final ServerBuilder<?> serverBuilder = NettyServerBuilder
                .forPort(port)
                .executor(applicationExecutor)
                .keepAliveTime(1, TimeUnit.MINUTES)
                .permitKeepAliveTime(1, TimeUnit.MINUTES);

        Stream.of(services).forEach(serverBuilder::addService);
        server = serverBuilder.build();
    }

    @PostConstruct
    private void start() throws IOException {
        server.start();
        waitForServerAsynchronously();
        LOGGER.info("Server started on port {}...", server.getPort());
    }

    private void waitForServerAsynchronously() {
        applicationExecutor.submit(() -> {
            try {
                server.awaitTermination();
            } catch (final InterruptedException exception) {
                LOGGER.error("Server interrupted!", exception);
            }
        });
    }

    @PreDestroy
    private void destroy() {
        server.shutdownNow();
        applicationExecutor.shutdownNow();
        LOGGER.info("Server stopped!");
    }

}
