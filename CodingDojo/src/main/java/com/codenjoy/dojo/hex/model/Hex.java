package com.codenjoy.dojo.hex.model;

import com.codenjoy.dojo.sample.services.SampleEvents;
import com.codenjoy.dojo.services.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static com.codenjoy.dojo.services.PointImpl.pt;

public class Hex implements Tickable, Field {

    private final List<Point> walls;
    private List<Player> players;

    private final int size;
    private Dice dice;

    public Hex(Level level, Dice dice) {
        this.dice = dice;
        walls = level.getWalls();
        size = level.getSize();
        players = new LinkedList<Player>();
    }

    @Override
    public void tick() {
        for (Player player : players) {
            Hero hero = player.getHero();

            hero.tick();
        }

        for (Player player : players) {
            Hero hero = player.getHero();

            if (!hero.isAlive()) {
                player.event(SampleEvents.LOOSE);
            }
        }
    }

    public int getSize() {
        return size;
    }

    @Override
    public boolean isBarrier(int x, int y) {
        Point pt = pt(x, y);
        return x > size - 1 || x < 0 || y < 0 || y > size - 1 || walls.contains(pt) || getHeroes().contains(pt);
    }

    @Override
    public Point getFreeRandom() {
        int rndX = 0;
        int rndY = 0;
        int c = 0;
        do {
            rndX = dice.next(size);
            rndY = dice.next(size);
        } while (!isFree(rndX, rndY) && c++ < 100);

        if (c >= 100) {
            return pt(0, 0);
        }

        return pt(rndX, rndY);
    }

    @Override
    public boolean isFree(int x, int y) {
        Point pt = pt(x, y);

        return !walls.contains(pt) &&
                !getHeroes().contains(pt);
    }

    public List<Hero> getHeroes() {
        List<Hero> result = new ArrayList<Hero>(players.size());
        for (Player player : players) {
            result.add(player.getHero());
        }
        return result;
    }

    public void newGame(Player player) {
        if (!players.contains(player)) {
            players.add(player);
        }
        player.newHero(this);
    }

    public void remove(Player player) {
        players.remove(player);
    }

    public List<Point> getWalls() {
        return walls;
    }
}
