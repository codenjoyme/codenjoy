package com.codenjoy.dojo.battlecity.model.items;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2020 Codenjoy
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

import com.codenjoy.dojo.battlecity.model.Elements;
import com.codenjoy.dojo.battlecity.model.Tank;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.Tickable;

import java.util.LinkedList;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class Prizes implements Tickable {

    private List<Prize> prizes = new LinkedList<>();

    @Override
    public void tick() {
        prizes.forEach(Prize::tick);
    }

    public void takeBy(Tank hero) {
        int index = prizes.indexOf(hero);
        if (index == -1) {
            return;
        }

        Prize prize = prizes.get(index);
        hero.take(prize);
        prizes.remove(prize);
    }

    public void removeDead() {
        prizes.removeIf(Prize::isDestroyed);
    }

    public Prize prizeAt(Point pt) {
        int index = prizes.indexOf(pt);
        return prizes.get(index);
    }

    public void add(Prize prize) {
        prizes.add(prize);
        prize.taken(item -> prizes.remove(item));
    }

    public boolean affect(Bullet bullet) {
        boolean contains = prizes.contains(bullet);
        if (contains) {
            Prize prize = prizeAt(bullet);
            prize.kill();
        }
        return contains;
    }

    public List<Prize> all() {
        return prizes;
    }

    public boolean contains(Elements elements) {
        return prizes.stream()
                .anyMatch(x -> elements.equals(x.elements()));
    }

    public int size() {
        return prizes.size();
    }

    public void clear() {
        prizes.clear();
    }

    @Override
    public String toString() {
        return prizes.stream()
                .map(prize -> prize.elements().name())
                .collect(toList())
                .toString();
    }
}
