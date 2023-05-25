package com.codenjoy.dojo.services.controller.screen;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2012 - 2022 Codenjoy
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


import com.codenjoy.dojo.services.Deals;
import com.codenjoy.dojo.services.Player;
import com.codenjoy.dojo.services.annotations.PerformanceOptimized;
import com.codenjoy.dojo.services.playerdata.PlayerData;
import com.codenjoy.dojo.services.serializer.JSONObjectSerializer;
import com.codenjoy.dojo.transport.ws.PlayerSocket;
import com.codenjoy.dojo.transport.ws.PlayerTransport;
import com.codenjoy.dojo.transport.ws.ResponseHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.websocket.api.Session;
import org.json.JSONObject;

import java.util.Map;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;

@Slf4j
@AllArgsConstructor
public class ScreenResponseHandler implements ResponseHandler {

    private static final ObjectMapper mapper;
    static {
        mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addSerializer(JSONObject.class, new JSONObjectSerializer());
        mapper.registerModule(module);
    }

    private PlayerTransport transport;
    private Player player;

    @Override
    public void onResponse(PlayerSocket socket, String message) {
        GetScreenJSONRequest request = new GetScreenJSONRequest(message);
        if (!request.itsMine()) {
            return;
        }

        transport.setFilterFor(socket,
                data -> filter((Map<Player, PlayerData>) data, request));
    }

    private String filter(Map<Player, PlayerData> data,
                                           GetScreenJSONRequest request)
    {
        Stream<Map.Entry<Player, PlayerData>> stream = data.entrySet().stream()
                .filter(entry -> request.isMyRoom(entry.getKey()))
                .filter(entry -> request.isAllPlayers() || request.isFor(entry.getKey()));
        if (request.isAllPlayers()) {
            stream = stream.filter(Deals.distinctByKey(entry -> entry.getValue().getGroup().toString()));
        }
        return toString(stream);
    }

    @PerformanceOptimized
    private String toString(Stream<Map.Entry<Player, PlayerData>> stream) {
        return toJson(stream.collect(toMap(
                entry -> entry.getKey().getId(),
                entry -> entry.getValue()
        )));
    }

    @SneakyThrows
    private String toJson(Object data) {
        return mapper.writeValueAsString(data);
    }

    @Override
    public void onClose(PlayerSocket socket, int statusCode, String reason) {
        log.debug("Websocket closed: {} from player: {} status code: {} reason: {}", player.getId(), statusCode, reason);
    }

    @Override
    public void onError(PlayerSocket socket, Throwable error) {
        log.error("Request error: player: {}, error: {}", player.getId(), error);
    }

    @Override
    public void onConnect(PlayerSocket socket, Session session) {
        log.debug("Connected: player: {}, session: {}", player.getId(), session);
    }
}
