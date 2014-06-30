package com.codenjoy.dojo.services;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by Sanja on 15.02.14.
 */
@Component("statistics")
public class Statistics implements Tickable {

    private Map<Player, PlayerSpy> players = new HashMap<Player, PlayerSpy>();

    public static final boolean WAIT_TICKS_LESS = true;
    public static final boolean WAIT_TICKS_MORE_OR_EQUALS = !WAIT_TICKS_LESS;

    public PlayerSpy newPlayer(Player player) {
        PlayerSpy spy = new PlayerSpy();
        players.put(player, spy);
        return spy;
    }

    @Override
    public void tick() {
        for (PlayerSpy spy : players.values()) {
            spy.tick();
        }
    }

    public List<Player> getPlayers(boolean less, int ticks) {
        LinkedList<Player> result = new LinkedList<Player>();

        for (Map.Entry<Player, PlayerSpy> entry : players.entrySet()) {
            if (!less ^ entry.getValue().playing(ticks)) {
                result.add(entry.getKey());
            }
        }

        return result;
    }
}
