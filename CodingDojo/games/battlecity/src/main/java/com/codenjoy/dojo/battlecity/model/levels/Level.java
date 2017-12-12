package com.codenjoy.dojo.battlecity.model.levels;

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


import com.codenjoy.dojo.battlecity.model.*;
import com.codenjoy.dojo.services.*;
import com.codenjoy.dojo.services.printer.BoardReader;

import java.util.LinkedList;
import java.util.List;

public class Level implements Field {

    private final LengthToXY xy;

    private String map =
            "☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ " +
            "☼   ¿         ¿         ¿                 ¿         ¿         ¿   ☼ " +
            "☼                                                                 ☼ " +
            "☼     ╬ ╬ ╬     ╬ ╬ ╬     ╬ ╬ ╬     ╬ ╬ ╬     ╬ ╬ ╬     ╬ ╬ ╬     ☼ " +
            "☼     ╬ ╬ ╬     ╬ ╬ ╬     ╬ ╬ ╬     ╬ ╬ ╬     ╬ ╬ ╬     ╬ ╬ ╬     ☼ " +
            "☼     ╬ ╬ ╬     ╬ ╬ ╬     ╬ ╬ ╬     ╬ ╬ ╬     ╬ ╬ ╬     ╬ ╬ ╬     ☼ " +
            "☼     ╬ ╬ ╬     ╬ ╬ ╬     ╬ ╬ ╬     ╬ ╬ ╬     ╬ ╬ ╬     ╬ ╬ ╬     ☼ " +
            "☼     ╬ ╬ ╬     ╬ ╬ ╬     ╬ ╬ ╬ ☼ ☼ ╬ ╬ ╬     ╬ ╬ ╬     ╬ ╬ ╬     ☼ " +
            "☼     ╬ ╬ ╬     ╬ ╬ ╬     ╬ ╬ ╬ ☼ ☼ ╬ ╬ ╬     ╬ ╬ ╬     ╬ ╬ ╬     ☼ " +
            "☼     ╬ ╬ ╬     ╬ ╬ ╬     ╬ ╬ ╬     ╬ ╬ ╬     ╬ ╬ ╬     ╬ ╬ ╬     ☼ " +
            "☼     ╬ ╬ ╬     ╬ ╬ ╬     ╬ ╬ ╬     ╬ ╬ ╬     ╬ ╬ ╬     ╬ ╬ ╬     ☼ " +
            "☼     ╬ ╬ ╬     ╬ ╬ ╬                         ╬ ╬ ╬     ╬ ╬ ╬     ☼ " +
            "☼     ╬ ╬ ╬     ╬ ╬ ╬                         ╬ ╬ ╬     ╬ ╬ ╬     ☼ " +
            "☼                         ╬ ╬ ╬     ╬ ╬ ╬                         ☼ " +
            "☼                         ╬ ╬ ╬     ╬ ╬ ╬                         ☼ " +
            "☼           ╬ ╬ ╬ ╬ ╬     ╬ ╬ ╬     ╬ ╬ ╬     ╬ ╬ ╬ ╬ ╬           ☼ " +
            "☼ ☼ ☼       ╬ ╬ ╬ ╬ ╬                         ╬ ╬ ╬ ╬ ╬       ☼ ☼ ☼ " +
            "☼                                                                 ☼ " +
            "☼                         ╬ ╬ ╬     ╬ ╬ ╬                         ☼ " +
            "☼     ╬ ╬ ╬     ╬ ╬ ╬     ╬ ╬ ╬     ╬ ╬ ╬     ╬ ╬ ╬     ╬ ╬ ╬     ☼ " +
            "☼     ╬ ╬ ╬     ╬ ╬ ╬     ╬ ╬ ╬     ╬ ╬ ╬     ╬ ╬ ╬     ╬ ╬ ╬     ☼ " +
            "☼     ╬ ╬ ╬     ╬ ╬ ╬     ╬ ╬ ╬ ╬ ╬ ╬ ╬ ╬     ╬ ╬ ╬     ╬ ╬ ╬     ☼ " +
            "☼     ╬ ╬ ╬     ╬ ╬ ╬     ╬ ╬ ╬ ╬ ╬ ╬ ╬ ╬     ╬ ╬ ╬     ╬ ╬ ╬     ☼ " +
            "☼     ╬ ╬ ╬     ╬ ╬ ╬     ╬ ╬ ╬     ╬ ╬ ╬     ╬ ╬ ╬     ╬ ╬ ╬     ☼ " +
            "☼     ╬ ╬ ╬     ╬ ╬ ╬     ╬ ╬ ╬     ╬ ╬ ╬     ╬ ╬ ╬     ╬ ╬ ╬     ☼ " +
            "☼     ╬ ╬ ╬     ╬ ╬ ╬     ╬ ╬ ╬     ╬ ╬ ╬     ╬ ╬ ╬     ╬ ╬ ╬     ☼ " +
            "☼     ╬ ╬ ╬     ╬ ╬ ╬     ╬ ╬ ╬     ╬ ╬ ╬     ╬ ╬ ╬     ╬ ╬ ╬     ☼ " +
            "☼     ╬ ╬ ╬                                             ╬ ╬ ╬     ☼ " +
            "☼     ╬ ╬ ╬                                             ╬ ╬ ╬     ☼ " +
            "☼     ╬ ╬ ╬               ╬ ╬ ╬ ╬ ╬ ╬ ╬ ╬               ╬ ╬ ╬     ☼ " +
            "☼     ╬ ╬ ╬               ╬ ╬ ╬ ╬ ╬ ╬ ╬ ╬               ╬ ╬ ╬     ☼ " +
            "☼                         ╬ ╬         ╬ ╬                         ☼ " +
            "☼                         ╬ ╬         ╬ ╬                         ☼ " +
            "☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ";

    public Level() {
        removeSpaces();
        xy = new LengthToXY(size());
    }

    private void removeSpaces() {
        String result = "";
        for (int i = 0; i < map.length(); i += 2) {
            result += map.charAt(i);
        }
        map = result;
    }

    @Override
    public int size() {
        return (int) Math.sqrt(map.length());
    }

    @Override
    public List<Construction> getConstructions() {
        List<Construction> result = new LinkedList<Construction>();
        for (int index = 0; index < map.length(); index++) {
            if (map.charAt(index) == Elements.CONSTRUCTION.ch) {
                result.add(new Construction(xy.getXY(index)));
            }
        }
        return result;
    }

    @Override
    public boolean isBarrier(int x, int y) {
        return false; // do nothing
    }

    @Override
    public boolean outOfField(int x, int y) {
        return false;  // do nothing
    }

    @Override
    public void affect(Bullet bullet) {
        // do nothing
    }

    @Override
    public List<Bullet> getBullets() {
        return new LinkedList<Bullet>(); // do nothing
    }

    @Override
    public BoardReader reader() {
        return new BoardReader() {
            @Override
            public int size() {
                return Level.this.size();
            }

            @Override
            public Iterable<? extends Point> elements() {
                List<Point> result = new LinkedList<Point>();
                result.addAll(Level.this.getBorders());
                result.addAll(Level.this.getBullets());
                result.addAll(Level.this.getConstructions());
                result.addAll(Level.this.getTanks());
                return result;
            }
        };
    }

    @Override
    public List<Tank> getTanks() {
        List<Tank> result = new LinkedList<Tank>();
        for (int index = 0; index < map.length(); index++) {
            if (map.charAt(index) == Elements.AI_TANK_DOWN.ch) {
                Point pt = xy.getXY(index);
                result.add(new AITank(pt.getX(), pt.getY(), new RandomDice(), Direction.DOWN));
            }
        }
        return result;
    }

    @Override
    public List<Border> getBorders() {
        List<Border> result = new LinkedList<Border>();
        for (int index = 0; index < map.length(); index++) {
            if (map.charAt(index) == Elements.BATTLE_WALL.ch) {
                result.add(new Border(xy.getXY(index)));
            }
        }
        return result;
    }
}
