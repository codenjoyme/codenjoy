package com.epam.dojo.expansion.model.lobby;

import com.epam.dojo.expansion.model.Player;
import com.epam.dojo.expansion.model.PlayerBoard;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;

/**
 * Created by Oleksandr_Baglai on 2017-09-11.
 */
public class NoPlayerLobby implements PlayerLobby {
    @Override
    public void remove(Player player) {
        // do nothing
    }

    @Override
    public void addPlayer(Player player) {
        // do nothing
    }

    @Override
    public PlayerBoard start(Player player, Supplier<PlayerBoard> loader) {
        return loader.get();
    }

    @Override
    public void tick() {
        // do nothing
    }
}
