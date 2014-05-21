package com.codenjoy.dojo.a2048.model;

import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;

import java.util.LinkedList;
import java.util.List;

import static com.codenjoy.dojo.services.PointImpl.pt;

/**
 * Created by Sanja on 21.05.14.
 */
public class Numbers {

    public static final int NONE = -1;
    private final int size;
    private int[][] data;

    public Numbers(List<Number> numbers, int size) {
        data = new int[size][size];
        this.size = size;
        clear();

        for (Number number : numbers) {
            add(number);
        }
    }

    public void clear() {
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                data[x][y] = NONE;
            }
        }
    }

    public boolean isEmpty() {
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                if (data[x][y] != NONE) return false;
            }
        }
        return true;
    }

    public void add(Number number) {
        data[number.getX()][number.getY()] = number.get();
    }

    public boolean contains(Point pt) {
        if (pt.getX() == -1 || pt.getY() == -1) return false;

        return data[pt.getX()][pt.getY()] != NONE;
    }

    public Number get(Point pt) {
        return new Number(data[pt.getX()][pt.getY()], pt);
    }

    public boolean contains(Elements element) {
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                if (data[x][y] == element.number()) return true;
            }
        }
        return false;
    }

    public void addRandom(Dice dice, int count) {
        for (int i = 0; i < count; i++) {
            Point pt = getFreeRandom(dice);
            if (!pt.itsMe(NO_SPACE)) {
                add(new Number(2, pt));
            }
        }
    }

    public static final Point NO_SPACE = pt(-1, -1);

    public Point getFreeRandom(Dice dice) {
        int rndX = 0;
        int rndY = 0;
        int c = 0;
        do {
            rndX = dice.next(size);
            rndY = dice.next(size);
        } while (rndX != -1 && rndY != -1 && data[rndX][rndY] != NONE && c++ < 100);

        if (c >= 100) {
            return NO_SPACE;
        }

        return pt(rndX, rndY);
    }

    public void remove(Point pt) {
        data[pt.getX()][pt.getY()] = NONE;
    }

    public List<Number> by(Direction direction) {
        int fromX = 0;
        int toX = size - 1;
        int dx = 1;

        int fromY = 0;
        int toY = size - 1;
        int dy = 1;

        switch (direction) {
            case LEFT  : break;
            case RIGHT : fromX = size - 1; toX = 0; dx = -1; break;
            case UP    : fromY = size - 1; toY = 0; dy = -1; break;
            case DOWN  : break;
        }

        List<Number> sorted = new LinkedList<Number>();
        for (int x = fromX; x != toX + dx; x += dx) {
            for (int y = fromY; y != toY + dy; y += dy) {
                Point pt = pt(x, y);
                if (!contains(pt)) continue;

                Number number = get(pt);
                sorted.add(number);
            }
        }
        return sorted;
    }
}
