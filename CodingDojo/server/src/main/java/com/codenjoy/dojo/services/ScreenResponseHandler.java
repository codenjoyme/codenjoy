package com.codenjoy.dojo.services;

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


import com.codenjoy.dojo.services.playerdata.PlayerData;
import com.codenjoy.dojo.transport.ws.ResponseHandler;
import com.codenjoy.dojo.transport.ws.PlayerSocket;
import com.codenjoy.dojo.transport.ws.PlayerTransport;
import org.eclipse.jetty.websocket.api.Session;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static java.util.stream.Collectors.toMap;

public class ScreenResponseHandler implements ResponseHandler {

    private static Logger logger = LoggerFactory.getLogger(ScreenResponseHandler.class);

    private PlayerTransport transport;
    private Player player;

    public ScreenResponseHandler(PlayerTransport transport, Player player) {
        this.transport = transport;
        this.player = player;
    }

    static class GetScreenJSONRequest {

        private JSONObject request;

        public GetScreenJSONRequest(String message) {
            request = new JSONObject(message);
        }

        public boolean itsMine() {
            return request.getString("name").equals("getScreen");
        }

        public boolean isAllPlayers() {
            return request.getBoolean("allPlayersScreen");
        }

        private List<String> getPlayers() {
            return new LinkedList<String>(){{
                request.getJSONArray("players")
                        .forEach(it -> add((String)it));
            }};
        }

        public boolean isFor(Player player) {
            return getPlayers().contains(player.getName());
        }

        public String getGameName() {
            return request.getString("gameName");
        }

        public boolean isMyGame(Player player) {
            return player.getGameName().equals(getGameName());
        }
    }

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
        return data.entrySet().stream()
                .filter(entry -> request.isMyGame(entry.getKey()))
                .filter(entry -> request.isAllPlayers() || request.isFor(entry.getKey()))
                .collect(toMap(entry -> entry.getKey(),
                        entry -> entry.getValue()));
    }

    @Override
    public void onClose(PlayerSocket socket, int statusCode, String reason) {
        logger.debug("Websocket closed: {} from player: {} status code: {} reason: {}",
                new Object[]{player.getName(), statusCode, reason});
    }

    @Override
    public void onError(PlayerSocket socket, Throwable error) {
        logger.error("Request error: player: {}, error: {}",
                new Object[]{player.getName(), error});
    }

    @Override
    public void onConnect(PlayerSocket socket, Session session) {
        logger.debug("Connected: player: {}, session: {}",
                new Object[]{player.getName(), session});
    }
}
