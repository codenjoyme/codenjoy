package com.dojo.common.channel;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PreDestroy;
import java.util.concurrent.TimeUnit;

public final class GrpcChannel {

    private static final int MAX_RETRY_ATTEMPTS = 1000;

    private static final Logger LOGGER = LoggerFactory.getLogger(GrpcChannel.class);

    private final ManagedChannel managedChannel;

    public GrpcChannel(final String host, final int port) {
        LOGGER.info("Managed channel started on address {}:{}...", host, port);
        this.managedChannel = ManagedChannelBuilder
                .forAddress(host, port)
                .usePlaintext()
                .defaultServiceConfig(ChannelRPCConfig.getInstance().getServiceConfig())
                .enableRetry()
                .maxRetryAttempts(MAX_RETRY_ATTEMPTS)
                .build();
    }

    public ManagedChannel getManagedChannel() {
        return managedChannel;
    }

    @PreDestroy
    private void stop() {
        managedChannel.shutdownNow();

        try {
            managedChannel.awaitTermination(5L, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
            LOGGER.error(e.getMessage());
        }

        managedChannel.shutdownNow();
        LOGGER.info("Managed channel stopped!");
    }

}
