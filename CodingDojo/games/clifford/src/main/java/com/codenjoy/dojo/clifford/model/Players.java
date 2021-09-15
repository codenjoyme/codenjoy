package com.codenjoy.dojo.clifford.model;

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

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class Players implements Iterable<Player> {

    private List<Player> players = new LinkedList<>();
    private List<Hero> heroes = new LinkedList<>();
    private Field field;

    public Players(Field field) {
        this.field = field;
        resetHeroes();
    }

    public void resetAll() {
        for (Player player : players) {
            player.newHero(field);
        }
        resetHeroes();
    }

    public void resetHeroes() {
        heroes = players.stream()
                .map(Player::getHero)
                .collect(toList());
    }

    public Stream<Player> stream() {
        return players.stream();
    }

    public List<Hero> heroes() {
        return heroes;
    }

    @Override
    public Iterator<Player> iterator() {
        return players.iterator();
    }

    public Player getPlayer(Hero hero) {
        for (Player player : players) {
            if (player.getHero() == hero) {
                return player;
            }
        }
        return null;
    }

    public void add(Player player) {
        if (!players.contains(player)) {
            players.add(player);
        }
        player.newHero(field);
        resetHeroes();
    }

    public void remove(Player player) {
        players.remove(player);
        resetHeroes();
    }

    public List<Player> all() {
        return players;
    }
}
