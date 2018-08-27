package com.codenjoy.dojo.services.controller;

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
        return getPlayers().contains(player.getName());
    }

    public String getGameName() {
        return request.getString("gameName");
    }

    public boolean isMyGame(Player player) {
        return player.getGameName().equals(getGameName());
    }
}
