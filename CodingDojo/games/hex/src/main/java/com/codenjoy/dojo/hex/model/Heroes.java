package com.codenjoy.dojo.hex.model;

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

public class Heroes implements Iterable<Hero> {
    private List<Hero> heroes;

    public Heroes() {
        heroes = new LinkedList<Hero>();
    }

    public void clear() {
        for (Hero hero : heroes) {
            hero.newOwner(null);
        }
        heroes.clear();
    }

    @Override
    public Iterator<Hero> iterator() {
        return new LinkedList<Hero>(heroes).iterator();
    }

    public Stream<Hero> stream() {
        return heroes.stream();
    }

    public boolean remove(Hero hero) {
        hero.newOwner(null);
        return heroes.remove(hero);
    }

    public void add(Hero hero, Player player) {
        heroes.add(hero);
        hero.newOwner(player);
    }

    public boolean contains(Hero hero) {
        return heroes.contains(hero);
    }

    public boolean isEmpty() {
        return heroes.isEmpty();
    }

    public int size() {
        return heroes.size();
    }
}
