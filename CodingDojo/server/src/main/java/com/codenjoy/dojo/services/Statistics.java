package com.codenjoy.dojo.services;

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


import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Component
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
