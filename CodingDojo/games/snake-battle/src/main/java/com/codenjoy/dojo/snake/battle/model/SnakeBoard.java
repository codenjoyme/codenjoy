package com.codenjoy.dojo.snake.battle.model;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 Codenjoy
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


import com.codenjoy.dojo.snake.battle.services.Events;
import com.codenjoy.dojo.services.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * О! Это самое сердце игры - борда, на которой все происходит.
 * Если какой-то из жителей борды вдруг захочет узнать что-то у нее, то лучше ему дать интефейс {@see Field}
 * Борда реализует интерфейс {@see Tickable} чтобы быть уведомленной о каждом тике игры. Обрати внимание на {SnakeBoard#tick()}
 */
public class SnakeBoard implements Tickable, Field {

    private List<Wall> walls;
    private List<Apple> apples;
    private List<Stone> stones;

    private List<Player> players;

    private final int size;
    private Dice dice;

    public SnakeBoard(Level level, Dice dice) {
        this.dice = dice;
        walls = level.getWalls();
        apples = level.getApples();
        stones = level.getStones();
        size = level.getSize();
        players = new LinkedList<Player>();
    }

    /**
     * @see Tickable#tick()
     */
    @Override
    public void tick() {
        for (Player player : players) {
            Hero hero = player.getHero();

            Point next = hero.getNextPoint();
            if (apples.contains(next)) {
                apples.remove(next);
                hero.growBy(1);
//                player.event(Events.WIN);

                Point pos = getFreeRandom();
                apples.add(new Apple(pos));
            }
            hero.tick();
        }

        for (Player player : players) {
            Hero hero = player.getHero();

            if (!hero.isAlive()) {
                player.event(Events.LOOSE);
            }
        }
    }

    public int size() {
        return size;
    }

    @Override
    public boolean isBarrier(Point p) {
        return p.isOutOf(size) || walls.contains(p) || getHeroes().contains(p);
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
            return PointImpl.pt(0, 0);
        }

        return PointImpl.pt(rndX, rndY);
    }

    @Override
    public boolean isFree(int x, int y) {
        Point pt = PointImpl.pt(x, y);

        return !apples.contains(pt) &&
                !stones.contains(pt) &&
                !walls.contains(pt) &&
                !getHeroes().contains(pt);
    }

    @Override
    public boolean isStone(Point p) {
        return stones.contains(p);
    }

    @Override
    public void setStone(Point p) {
        if (!stones.contains(p)) {
            stones.add(new Stone(p));
        }
    }

    @Override
    public void removeStone(Point p) {
        stones.remove(p);
    }

    public List<Apple> getApples() {
        return apples;
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

    public List<Wall> getWalls() {
        return walls;
    }

    public List<Stone> getStones() {
        return stones;
    }

    public BoardReader reader() {
        return new BoardReader() {
            private int size = SnakeBoard.this.size;

            @Override
            public int size() {
                return size;
            }

            @Override
            public Iterable<? extends Point> elements() {
                List<Point> result = new LinkedList<Point>();
                result.addAll(SnakeBoard.this.getWalls());
                List<Hero> heroes = SnakeBoard.this.getHeroes();
                for (Hero hero : heroes)
                    result.addAll(hero.getBody());
                result.addAll(SnakeBoard.this.getApples());
                result.addAll(SnakeBoard.this.getStones());
                return result;
            }
        };
    }
}
