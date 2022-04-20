package com.codenjoy.dojo.services.grpc;

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

import com.codenjoy.dojo.Event;
import com.codenjoy.dojo.EventServiceGrpc;
import com.codenjoy.dojo.EventsRequest;
import com.codenjoy.dojo.EventsResponse;
import com.codenjoy.dojo.config.grpc.EventsConfig;
import com.codenjoy.dojo.services.dao.PlayerGameSaver;
import io.grpc.stub.StreamObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class EventService extends EventServiceGrpc.EventServiceImplBase {

    @Value("${grpc.code-execution.host}:${grpc.code-execution.port}")
    private String URL;

    private final PlayerGameSaver playerGameSaver;
    private final EventsConfig eventsConfig;

    @Autowired
    public EventService(PlayerGameSaver playerGameSaver, EventsConfig eventsConfig) {
        this.playerGameSaver = playerGameSaver;
        this.eventsConfig = eventsConfig;
    }

    @Override
    public void getAllEvents(EventsRequest request, StreamObserver<EventsResponse> responseObserver) {
        Map<String, String> events = playerGameSaver.getEventsList();

        EventsResponse.Builder responseBuilder = EventsResponse.newBuilder();

        events.forEach((key, value) -> {
            Event event = Event.newBuilder()
                    .setRoomName(key)
                    .setGameName(value)
                    .setGameServerUrl(URL)
                    .build();
            responseBuilder.addEvent(event);
        });

        responseObserver.onNext(responseBuilder.build());
        responseObserver.onCompleted();
    }
}
