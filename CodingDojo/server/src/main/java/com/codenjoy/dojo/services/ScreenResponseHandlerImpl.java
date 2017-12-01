package com.codenjoy.dojo.services;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 Codenjoy
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
import com.codenjoy.dojo.transport.ws.PlayerResponseHandler;
import com.codenjoy.dojo.transport.ws.PlayerSocket;
import com.codenjoy.dojo.transport.ws.PlayerTransport;
import org.eclipse.jetty.websocket.api.Session;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ScreenResponseHandlerImpl implements PlayerResponseHandler {

    private static Logger logger = LoggerFactory.getLogger(ScreenResponseHandlerImpl.class);

    private PlayerTransport transport;
    private Player player;

    public ScreenResponseHandlerImpl(PlayerTransport transport, Player player) {
        this.transport = transport;
        this.player = player;
    }

    @Override
    public void onResponseComplete(PlayerSocket socket, String responseContent) {
        JSONObject request = new JSONObject(responseContent);
        if (request.getString("name").equals("getScreen")) {
            boolean allPlayersScreen = request.getBoolean("allPlayersScreen");

            List<String> players = new LinkedList<>();
            request.getJSONArray("players").forEach(player -> players.add((String) player));

            String gameName = request.getString("gameName");

            transport.setFilterFor(socket,
                    data -> new JSONObject(filter((Map<Player, PlayerData>) data,
                            allPlayersScreen, players, gameName)));
        }
    }

    private Map<Player, PlayerData> filter(Map<Player, PlayerData> data, boolean allPlayersScreen, List<String> players, String gameName) {
        Map<Player, PlayerData> result = new HashMap<>();
        for (Map.Entry<Player, PlayerData> entry : data.entrySet()) {
            Player player = entry.getKey();
            if (!player.getGameName().equals(gameName)) {
                continue;
            }

            if (!allPlayersScreen && !players.contains(player.getName())) {
                continue;
            }

            result.put(player, entry.getValue());
        }
        return result;
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