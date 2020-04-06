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
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

class GetScreenJSONRequest {

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
        return new LinkedList<String>() {{
            request.getJSONArray("players")
                    .forEach(it -> add((String) it));
        }};
    }

    public boolean isFor(Player player) {
        return getPlayers().contains(player.getId());
    }

    public String getGameName() {
        return request.getString("gameName");
    }

    public boolean isMyGame(Player player) {
        return player.getGameName().equals(getGameName());
    }
}
