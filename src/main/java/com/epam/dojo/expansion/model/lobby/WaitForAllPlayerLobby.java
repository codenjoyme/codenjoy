package com.epam.dojo.expansion.model.lobby;

import com.codenjoy.dojo.services.Tickable;
import com.epam.dojo.expansion.model.Player;
import com.epam.dojo.expansion.model.PlayerBoard;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Created by Oleksandr_Baglai on 2017-09-11.
 */
public class WaitForAllPlayerLobby implements PlayerLobby, Tickable {
    private List<Player> all = new LinkedList<>();
    private List<Player> waiting = new LinkedList<>();
    private Map<Player, Supplier<PlayerBoard>> loaders = new HashMap<>();

    @Override
    public void remove(Player player) {
        all.remove(player);
    }

    @Override
    public void addPlayer(Player player) {
        all.add(player);
    }

    @Override
    public PlayerBoard start(Player player, Supplier<PlayerBoard> loader) {
        waiting.add(player);
        loaders.put(player, loader);
        if (all.size() == waiting.size()) {
            waiting.clear();
            for (Player p : all) {
                p.setPlayerBoard(loaders.get(p).get());
            }
        }
        return new LobbyPlayerBoard(waiting) {
            @Override
            public void tick() {

            }
        };
    }

    @Override
    public void tick() {

    }
}
