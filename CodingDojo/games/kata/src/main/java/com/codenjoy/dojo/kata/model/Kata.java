package com.codenjoy.dojo.kata.model;

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


import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.printer.BoardReader;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Kata implements Field {

    private List<Player> players;
    private Dice dice;

    public Kata(Dice dice) {
        this.dice = dice;
        players = new LinkedList<>();
    }

    @Override
    public void tick() {
        for (Player player : players) {
            player.checkAnswer();
        }
    }

    public List<Hero> getHeroes() {
        List<Hero> result = new ArrayList<>(players.size());
        for (Player player : players) {
            result.add(player.getHero());
        }
        return result;
    }

    @Override
    public BoardReader reader() {
        // do nothing, this is text game
        return null;
    }

    public void newGame(Player player) {
        if (!players.contains(player)) {
            players.add(player);
        } else {
            player.clearScore();
        }
        player.newHero(this);
    }

    public void remove(Player player) {
        players.remove(player);
    }

}
