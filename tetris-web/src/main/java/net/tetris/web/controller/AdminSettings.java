package net.tetris.web.controller;

import net.tetris.services.PlayerInfo;

import java.util.List;

/**
 * User: oleksandr.baglai
 * Date: 9/24/12
 * Time: 5:56 PM
 */
public class AdminSettings {

    private String selectedLevels;
    private List<PlayerInfo> players;

    public String getSelectedLevels() {
        return selectedLevels;
    }

    public void setSelectedLevels(String selectedLevels) {
        this.selectedLevels = selectedLevels;
    }

    public List<PlayerInfo> getPlayers() {
        return players;
    }

    public void setPlayers(List<PlayerInfo> players) {
        this.players = players;
    }
}
