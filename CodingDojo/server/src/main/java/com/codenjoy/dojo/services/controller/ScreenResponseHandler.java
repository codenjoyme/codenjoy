package com.codenjoy.dojo.services.controller;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 Codenjoy
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


import com.codenjoy.dojo.services.Player;
import com.codenjoy.dojo.services.playerdata.PlayerData;
import com.codenjoy.dojo.transport.ws.PlayerSocket;
import com.codenjoy.dojo.transport.ws.PlayerTransport;
import com.codenjoy.dojo.transport.ws.ResponseHandler;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.websocket.api.Session;
import org.json.JSONObject;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;

@Slf4j
@AllArgsConstructor
public class ScreenResponseHandler implements ResponseHandler {

    private PlayerTransport transport;
    private Player player;

    @Override
    public void onResponse(PlayerSocket socket, String message) {
        GetScreenJSONRequest request = new GetScreenJSONRequest(message);
        if (!request.itsMine()) {
            return;
        }

        transport.setFilterFor(socket,
                data -> new JSONObject(filter((Map<Player, PlayerData>) data, request)));
    }

    private Map<Player, PlayerData> filter(Map<Player, PlayerData> data,
                                           GetScreenJSONRequest request)
    {
        Stream<Map.Entry<Player, PlayerData>> stream = data.entrySet().stream()
                .filter(entry -> request.isMyGame(entry.getKey()))
                .filter(entry -> request.isAllPlayers() || request.isFor(entry.getKey()));
        if (request.isAllPlayers()) {
            stream = stream.filter(distinctByKey(entry -> entry.getValue().getHeroesData().get("group").toString()));
        }
        return stream
                .collect(toMap(entry -> entry.getKey(),
                        entry -> entry.getValue()));
    }

    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
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
