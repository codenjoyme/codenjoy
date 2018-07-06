package com.codenjoy.dojo.hex.model;

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