package com.codenjoy.dojo.web.controller;

import com.codenjoy.dojo.services.PlayerInfo;

import java.util.List;

public class AdminSettings {

    private List<PlayerInfo> players;

    private List<String> parameters;

    private String gameName;

    private String generateNameMask;
    private String generateCount;

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

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

    public String getGameName() {
        return gameName;
    }

    public String getGenerateNameMask() {
        return generateNameMask;
    }

    public void setGenerateNameMask(String generateNameMask) {
        this.generateNameMask = generateNameMask;
    }

    public String getGenerateCount() {
        return generateCount;
    }

    public void setGenerateCount(String generateCount) {
        this.generateCount = generateCount;
    }
}
