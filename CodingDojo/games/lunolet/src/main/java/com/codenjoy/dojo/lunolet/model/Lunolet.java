package com.codenjoy.dojo.lunolet.model;

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


import com.codenjoy.dojo.services.printer.BoardReader;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;

public class Lunolet implements Field {

    private LevelManager levelManager;
    private List<Player> players;

    public Lunolet(LevelManager levelManager) {
        this.levelManager = levelManager;
        players = new LinkedList<>();
    }

    @Override
    public void tick() {
        for (Player player : players) {
            player.getHero().tick();
        }
    }

    @Override
    public BoardReader reader() {
        return null; // do nothing, because this game use custom Printer
    }

    @Override
    public void newGame(Player player) {
        if (!players.contains(player)) {
            players.add(player);
        }
        player.newHero(this);
    }

    @Override
    public void remove(Player player) {
        players.remove(player);
    }

    @Override
    public void clearScore() {
        for (Player player : players) {
            player.resetLevels();
        }
    }

    @Override
    public Level getLevel(int level) {
        return levelManager.getLevel(level);
    }
}
