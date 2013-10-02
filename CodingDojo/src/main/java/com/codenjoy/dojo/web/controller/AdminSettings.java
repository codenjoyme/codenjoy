package com.codenjoy.dojo.web.controller;

import com.codenjoy.dojo.services.PlayerInfo;
import com.codenjoy.dojo.services.settings.Parameter;


import java.util.List;

/**
 * User: oleksandr.baglai
 * Date: 9/24/12
 * Time: 5:56 PM
 */
public class AdminSettings {

    private List<PlayerInfo> players;
    private List<String> parameters;

    public List<PlayerInfo> getPlayers() {
        return players;
    }

    public void setPlayers(List<PlayerInfo> players) {
        this.players = players;
    }

    public void setParameters(List<String> parameters) {
        this.parameters = parameters;
    }

    public List<String> getParameters() {
        return parameters;
    }

}
