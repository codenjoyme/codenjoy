package com.codenjoy.dojo.moebius.model;

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


import com.codenjoy.dojo.moebius.services.Events;
import com.codenjoy.dojo.services.*;
import com.codenjoy.dojo.services.joystick.ActJoystick;
import com.codenjoy.dojo.services.printer.BoardReader;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import static com.codenjoy.dojo.services.PointImpl.pt;

public class Moebius implements Tickable, Field {

    private List<Line> lines;

    private final Level level;
    private int size;
    private Dice dice;
    private final EventListener listener;
    private boolean alive;

    private Point act;

    public Moebius(Level level, Dice dice, EventListener listener) {
        this.dice = dice;
        this.listener = listener;
        this.level = level;
        newGame(null);
    }

    @Override
    public void tick() {
        if (act != null) {
            int index = lines.indexOf(act);
            if (index == -1) return;

            Line line = lines.get(index);
            line.rotate();
        }

        removePipes();

        Point pt = getFreeRandom();
        if (pt == null) {
            listener.event(new Events(Events.Event.GAME_OVER));
            alive = false;
        } else {
            setLine(pt, Elements.random(dice));
        }

        act = null;
    }

    private void removePipes() {
        Queue<Line> processing = new LinkedList<Line>(lines);
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
            listener.event(new Events(Events.Event.WIN, count));
        } while (!processing.isEmpty());
    }

    private List<Line> checkCycle(Line start) {
        List<Line> result = new LinkedList<Line>();

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

    public int size() {
        return size;
    }

    @Override
    public Point getFreeRandom() {
        Point pt;
        int c = 0;
        do {
            int rndX = dice.next(size);
            int rndY = size - 1 - dice.next(size); // TODO надо избавиться от этого зеркалирования везде во всех играх
            pt = pt(rndX, rndY);
        } while ((pt.isOutOf(1, 1, size) || !isFree(pt)) && c++ < 100);

        if (c >= 100) {
            return null;
        }

        return pt;
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

    public void newGame(Player player) {
        size = level.getSize();
        lines = level.getLines();
        act = null;
        alive = true;
    }

    public List<Line> getLines() {
        return lines;
    }

    public BoardReader reader() {
        return new BoardReader() {
            private int size = Moebius.this.size;

            @Override
            public int size() {
                return size;
            }

            @Override
            public Iterable<? extends Point> elements() {
                List<Point> result = new LinkedList<Point>();
                result.addAll(Moebius.this.getLines());
                return result;
            }
        };
    }

    @Override
    public boolean isGameOver() {
        return !alive;
    }

    public Joystick getJoystick() {
        return new ActJoystick() {
            @Override
            public void act(int... p) {
                if (p == null || p.length != 2) return;

                int x = p[0];
                int y = size - 1 - p[1]; // TODO а че тут надо зеркалировать?

                act = new PointImpl(x, y);
                // TODO validate out of box
            }
        };
    }

    public Point getSelected() {
        return act;
    }
}
