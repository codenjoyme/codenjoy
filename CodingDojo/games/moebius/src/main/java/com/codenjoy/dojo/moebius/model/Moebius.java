package com.codenjoy.dojo.moebius.model;

import com.codenjoy.dojo.services.*;
import static com.codenjoy.dojo.services.PointImpl.*;

import java.util.LinkedList;
import java.util.List;

public class Moebius implements Tickable, Field {

    private List<Line> lines;

    private final int size;
    private Dice dice;

    private Point act;

    public Moebius(Level level, Dice dice) {
        this.dice = dice;
        size = level.getSize();
        lines = level.getLines();
    }

    @Override
    public void tick() {
        if (act != null) {
            int index = lines.indexOf(act);
            if (index == -1) return;

            Line line = lines.get(index);
            line.rotate();
        }

        Point pt = getFreeRandom();
        setLine(pt, Elements.random(dice));

        act = null;
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
            int rndY = dice.next(size);
            pt = pt(rndX, rndY);
        } while (!pt.isOutOf(1, 1, size) && !isFree(pt) && c++ < 100);

        if (c >= 100) {
            return PointImpl.pt(0, 0);
        }

        return pt;
    }

    @Override
    public boolean isFree(Point pt) {
        return !lines.contains(pt);
    }

    @Override
    public boolean isLine(Point pt) {
        return lines.contains(pt);
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
        // TODO
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
        return false; // TODO
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
                int y = size - 1 - p[1];

                act = new PointImpl(x, y);
                // TODO validate out of box
            }
        };
    }
}
