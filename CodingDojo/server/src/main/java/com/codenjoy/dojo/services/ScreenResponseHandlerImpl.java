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


import com.codenjoy.dojo.transport.ws.PlayerResponseHandler;
import com.codenjoy.dojo.transport.ws.TransportErrorType;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;

public class ScreenResponseHandlerImpl implements PlayerResponseHandler {

    private static Logger logger = LoggerFactory.getLogger(ScreenResponseHandlerImpl.class);

    private Player player;

    public ScreenResponseHandlerImpl(Player player) {
        this.player = player;
    }

    @Override
    public void onResponseComplete(String responseContent, Object context) {
        JSONObject request = new JSONObject(responseContent);
        if (request.getString("name").equals("getScreen")) {
            boolean allPlayersScreen = request.getBoolean("allPlayersScreen");
            List<String> players = new LinkedList<>();
            request.getJSONArray("players").forEach(player -> players.add((String) player));
            System.out.printf("allPlayersScreen = %s, players = %s\n", allPlayersScreen, players);
        }
    }

    @Override
    public void onError(TransportErrorType type, Object context) {
        if (type == TransportErrorType.EXPIRED) {
            logger.warn("Request expired: player: {}, context: {}",
                    new Object[]{player.getName(), context});
        }
    }
}