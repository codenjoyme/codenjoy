package com.codenjoy.dojo.services.multiplayer;

import java.util.LinkedList;
import java.util.List;

public class Room {
    private GameField field;
    private int count;
    private List<GamePlayer> players = new LinkedList<>();

    public Room(GameField field, int count) {
        this.field = field;
        this.count = count;
    }

    public GameField getField(GamePlayer player) {
        players.add(player);
        return field;
    }

    public boolean isFree() {
        return players.size() < count;
    }

    public boolean contains(GamePlayer player) {
        return players.stream()
                .filter(p -> p.equals(player))
                .count() != 0;
    }

    public List<GamePlayer> getPlayers() {
        return players;
    }
}
