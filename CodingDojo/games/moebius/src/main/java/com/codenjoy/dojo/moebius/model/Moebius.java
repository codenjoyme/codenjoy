package com.codenjoy.dojo.moebius.model;

import com.codenjoy.dojo.moebius.services.Events;
import com.codenjoy.dojo.services.*;
import com.codenjoy.dojo.services.EventListener;

import static com.codenjoy.dojo.services.PointImpl.*;

import java.util.*;

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
            if (line.isOutOf(1, 1, size)) continue;
            List<Line> cycle = checkCycle(line);
            if (cycle.isEmpty()) continue;
            for (Line l : cycle) {
                removeLine(l);
            }
            listener.event(new Events(Events.Event.WIN, cycle.size()));
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
    public void removeLine(Point pt) {
        lines.remove(pt);
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
        return new Joystick() {
            @Override
            public void down() {
                // do nothing
            }

            @Override
            public void up() {
                // do nothing
            }

            @Override
            public void left() {
                // do nothing
            }

            @Override
            public void right() {
                // do nothing
            }

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
}
