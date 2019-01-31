package com.codenjoy.dojo.moebius.model;

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


import com.codenjoy.dojo.moebius.services.Events;
import com.codenjoy.dojo.services.BoardUtils;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.printer.BoardReader;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import static com.codenjoy.dojo.services.BoardUtils.NO_SPACE;

public class Moebius implements Field {

    private List<Line> lines;
    private final Level level;
    private int size;
    private Dice dice;
    private Player player;

    public Moebius(Level level, Dice dice) {
        this.dice = dice;
        this.level = level;
        this.size = level.getSize();
    }

    @Override
    public void tick() {
        Point act = player.getHero();
        if (!act.isOutOf(size)) {
            int index = lines.indexOf(act);
            if (index != -1) {
                Line line = lines.get(index);
                line.rotate();
            } else {
                // do nothing
                // TODO test me
            }
        }

        removePipes();

        Point pt = getFreeRandom();
        if (pt.equals(NO_SPACE)) {
            player.event(new Events(Events.Event.GAME_OVER));
            player.getHero().die();
        } else {
            setLine(pt, Elements.random(dice));
        }
    }

    private void removePipes() {
        Queue<Line> processing = new LinkedList<>(lines);
        do {
            Line line = processing.remove();

            if (line.getType() == Line.Type.CROSS) continue;
            if (line.isOutOf(1, 1, size)) continue;

            List<Line> cycle = checkCycle(line);

            if (cycle.isEmpty()) continue;

            int count = 0;
            for (Line l : cycle) {
                if (removeLine(l)) {
                    count++;
                }
            }
            player.event(new Events(Events.Event.WIN, count));
        } while (!processing.isEmpty());
    }

    private List<Line> checkCycle(Line start) {
        List<Line> result = new LinkedList<>();

        Line one = start;
        Line two = getLine(one.to());
        if (two == null) {
            return Arrays.asList();
        }

        Line three;
        while (true) {
            three = getLine(two.pipeFrom(one));
            if (three == null) {
                return Arrays.asList();
            }

            result.add(one);
            one = two;
            if (one == start) {
                return result;
            }
            two = three;
        }
    }

    private Line getLine(Point pt) {
        if (pt == null) {
            return null;
        }
        for (Line line : lines) {
            if (line.itsMe(pt)) {
                return line;
            }
        }
        return null;
    }

    @Override
    public Point getFreeRandom() {
        return BoardUtils.getFreeRandom(size, dice,
                pt -> !pt.isOutOf(1, 1, size) && isFree(pt));
    }

    @Override
    public boolean isFree(Point pt) {
        return !lines.contains(pt);
    }

    @Override
    public void setLine(Point pt, Elements element) {
        if (!lines.contains(pt)) {
            lines.add(new Line(pt, element));
        }
    }

    @Override
    public boolean removeLine(Point pt) {
        return lines.remove(pt);
    }

    @Override
    public void newGame(Player player) {
        this.player = player;
        lines = level.getLines();
        player.newHero(this);
    }

    @Override
    public void remove(Player player) {
        this.player = null;
    }

    public List<Line> getLines() {
        return lines;
    }

    @Override
    public BoardReader reader() {
        return new BoardReader() {
            private int size = Moebius.this.size;

            @Override
            public int size() {
                return size;
            }

            @Override
            public Iterable<? extends Point> elements() {
                return new LinkedList<Point>(){{
                    addAll(Moebius.this.getLines());
                }};
            }
        };
    }

}
