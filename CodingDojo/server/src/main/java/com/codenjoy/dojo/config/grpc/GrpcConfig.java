package com.codenjoy.dojo.config.grpc;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2021 Codenjoy
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import com.codenjoy.dojo.services.grpc.EventService;
import com.codenjoy.dojo.services.grpc.LeaderboardService;
import com.codenjoy.dojo.services.grpc.UserDetailsService;
import com.dojo.common.GrpcServer;
import com.dojo.common.channel.GrpcChannel;
import com.dojo.notifications.QueryServiceGrpc;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@ConfigurationProperties
@Component
public class GrpcConfig {

    private final GrpcChannel dojoChannel;
    private final GrpcServer grpcServer;

    public GrpcConfig(@Value("${grpc.executor.pool.size}") final int applicationExecutorPoolSize,
                      @Value("${grpc.server.port}") final int port,
                      @Value("${grpc.notifications.host}") final String host,
                      @Value("${grpc.notifications.port}") final int dojoPort,
                      final LeaderboardService leaderboardService,
                      final UserDetailsService userDetailsService,
                      final EventService eventService) {
        this.dojoChannel = new GrpcChannel(host, dojoPort);
        this.grpcServer = new GrpcServer(applicationExecutorPoolSize, port, leaderboardService, userDetailsService, eventService);
    }

    @Bean
    public GrpcServer getGrpcServer () {
        return grpcServer;
    }

    @Bean
    public QueryServiceGrpc.QueryServiceBlockingStub getQueryBlockingStub() {
        return QueryServiceGrpc.newBlockingStub(this.dojoChannel.getManagedChannel());
    }
}
