package com.codenjoy.dojo.battlecity.model.items;

import com.codenjoy.dojo.battlecity.model.Elements;
import com.codenjoy.dojo.battlecity.model.Tank;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.Tickable;

import java.util.LinkedList;
import java.util.List;

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
}
