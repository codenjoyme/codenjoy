package com.epam.dojo.expansion.model.lobby;

import com.codenjoy.dojo.services.Tickable;
import com.epam.dojo.expansion.model.Player;
import com.epam.dojo.expansion.model.PlayerBoard;

import java.util.function.Supplier;

/**
 * Created by Oleksandr_Baglai on 2017-09-11.
 */
public interface PlayerLobby extends Tickable {
    void remove(Player player);

    void addPlayer(Player player);

    PlayerBoard start(Player player, Supplier<PlayerBoard> loader);
}
