package com.epam.dojo.expansion.model;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;

/**
 * Created by Oleksandr_Baglai on 2017-09-11.
 */
public class PlayerLobby {
    private List<Player> allPlayers = new LinkedList<>();

    public void remove(Player player) {
        allPlayers.remove(player);
    }

    public void addPlayer(Player player) {
        allPlayers.add(player);
    }

    public PlayerBoard start(Supplier<PlayerBoard> loader) {
        return loader.get();
    }
}
