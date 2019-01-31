package com.codenjoy.dojo.fifteen.model;

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

import com.codenjoy.dojo.fifteen.services.Events;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.printer.BoardReader;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static com.codenjoy.dojo.services.PointImpl.pt;

public class Fifteen implements Field {
    private final Level level;
    private List<Player> players;

    private List<Digit> digits;
    private List<Wall> walls;

    private int size;
    private Dice dice;

    public Fifteen(Level level, Dice dice) {
        this.level = level;
        this.dice = dice;
        size = level.getSize();
        players = new LinkedList<>();
    }

    @Override
    public void tick() {
        for (Player player : players) {
            Hero hero = player.getHero();
            hero.tick();

            if(isAllPositionCorrect()){
                player.event(Events.WIN);
                player.getHero().die();
            }
        }
    }

    private boolean isAllPositionCorrect() {
        for(Digit digit: digits){
            if(!(new DigitHandler().isRightPosition(digit))){
               return false;
            }
        }
        return true;
    }

    @Override
    public boolean isBarrier(int x, int y) {
        Point pt = pt(x, y);
        return x > size - 1 || x < 0 || y < 0 || y > size - 1 || walls.contains(pt);
    }

    @Override
    public Digit getDigit(int x, int y) {
        for (Digit point : digits) {
            if (point.itsMe(x, y)) {
                return point;
            }
        }
        return null;
    }

    @Override
    public boolean isFree(int x, int y) {
        Point pt = pt(x, y);

        return !digits.contains(pt) &&
                !walls.contains(pt) &&
                !getHeroes().contains(pt);
    }

    public List<Hero> getHeroes() {
        List<Hero> result = new ArrayList<>();
        for (Player player : players) {
            result.add(player.getHero());
        }
        return result;
    }

    @Override
    public void newGame(Player player) {
        walls = level.getWalls();
        digits = level.getDigits();
        size = level.getSize();
        if (!players.contains(player)) {
            players.add(player);
        }
        player.newHero(this);
    }

    @Override
    public Hero getLevelHero() {
        return level.getHero().get(0);
    }

    @Override
    public void remove(Player player) {
        players.remove(player);
    }

    public List<Wall> getWalls() {
        return walls;
    }

    public List<Digit> getDigits() {
        return digits;
    }

    @Override
    public BoardReader reader() {
        return new BoardReader() {
            private int size = Fifteen.this.size;

            @Override
            public int size() {
                return size;
            }

            @Override
            public Iterable<? extends Point> elements() {
                return new LinkedList<Point>() {{
                    addAll(Fifteen.this.getWalls());
                    addAll(Fifteen.this.getHeroes());
                    addAll(Fifteen.this.getDigits());
                }};
            }
        };
    }
}
