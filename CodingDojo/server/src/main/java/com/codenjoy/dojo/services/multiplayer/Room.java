package com.codenjoy.dojo.services.multiplayer;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 Codenjoy
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

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
