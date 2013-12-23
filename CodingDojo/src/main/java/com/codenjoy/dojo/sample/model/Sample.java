package com.codenjoy.dojo.sample.model;

import com.codenjoy.dojo.sample.services.SampleEvents;
import com.codenjoy.dojo.services.*;

import java.util.LinkedList;
import java.util.List;

import static com.codenjoy.dojo.services.PointImpl.pt;

/**
 * User: sanja
 * Date: 17.12.13
 * Time: 4:56
 */
public class Sample implements Tickable, Field, Players {

    private final List<Point> walls;
    private List<Point> gold;
    private List<Point> bombs;

    private List<Player> players;

    private final int size;
    private Dice dice;

    public Sample(Level level, Dice dice) {
        this.dice = dice;
        walls = level.getWalls();
        gold = level.getGold();
        size = level.getSize();
        players = new LinkedList<Player>();
        bombs = new LinkedList<Point>();
    }

    @Override
    public void tick() {
        for (Player player : players) {
            Hero hero = player.getHero();

            hero.tick();

            if (gold.contains(hero)) {
                gold.remove(hero);
                player.event(SampleEvents.WIN);

                Point pos = getFreeRandom();
                gold.add(pt(pos.getX(), pos.getY()));
            }
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
        return x > size - 1 || x < 0 || y < 0 || y > size - 1 || walls.contains(pt);
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

        return !gold.contains(pt) &&
                !bombs.contains(pt) &&
                !walls.contains(pt) &&
                !getHeroes().contains(pt);
    }

    @Override
    public boolean isBomb(int x, int y) {
        return bombs.contains(pt(x, y));
    }

    @Override
    public void setBomb(int x, int y) {
        Point pt = pt(x, y);
        if (!bombs.contains(pt)) {
            bombs.add(pt);
        }
    }

    @Override
    public void removeBomb(int x, int y) {
        bombs.remove(pt(x, y));
    }

    public List<Point> getGold() {
        return gold;
    }

    public List<Hero> getHeroes() {
        List<Hero> result = new LinkedList<Hero>();
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

    @Override
    public int getCount() {
        return players.size();
    }

    public List<Point> getWalls() {
        return walls;
    }

    public List<Point> getBombs() {
        return bombs;
    }
}
