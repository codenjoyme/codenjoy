package com.codenjoy.dojo.a2048.model;

import com.codenjoy.dojo.sample.services.SampleEvents;
import com.codenjoy.dojo.services.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static com.codenjoy.dojo.services.PointImpl.pt;

public class A2048 implements Tickable {

    public static final Point NO_SPACE = pt(0, 0);

    private List<Number> numbers;
    private final int size;
    private Dice dice;
    private Direction direction;

    public A2048(Level level, Dice dice) {
        this.dice = dice;
        numbers = level.getNumbers();
        size = level.getSize();
    }

    @Override
    public void tick() {
        if (direction == null) return ;

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
                if (!numbers.contains(pt)) continue;

                Number number = numbers.get(numbers.indexOf(pt));
                sorted.add(number);
            }
        }

        List<Number> newNumbers = new LinkedList<Number>();
        for (Number number : sorted) {
            Point moved = number;
            while (true) {
                Point temp = direction.change(moved);
                if (temp.isOutOf(size)) {
                    break;
                } else {
                    moved = temp;
                }
                if (newNumbers.contains(moved)) break;
            }

            if (!newNumbers.contains(moved) || moved.equals(number)) {
                newNumbers.add(new Number(number.get(), moved));
            } else {
                Number atWay = newNumbers.get(newNumbers.indexOf(moved));
                if (atWay.get() == number.get()) {
                    newNumbers.remove(newNumbers.indexOf(atWay));
                    newNumbers.add(new Number(number.next(), atWay));
                } else {
                    Point prev = direction.inverted().change(moved);
                    newNumbers.add(new Number(number.get(), prev));
                }
            }
        }

        numbers = newNumbers;
        direction = null;
    }

    public int getSize() {
        return size;
    }

    public Point getFreeRandom() {
        int rndX = 0;
        int rndY = 0;
        int c = 0;
        do {
            rndX = dice.next(size);
            rndY = dice.next(size);
        } while (!isFree(rndX, rndY) && c++ < 100);

        if (c >= 100) {
            return NO_SPACE;
        }

        return pt(rndX, rndY);
    }

    public boolean isFree(int x, int y) {
        Point pt = pt(x, y);

        return !numbers.contains(pt);
    }

    public List<Number> getNumbers() {
        return numbers;
    }

    public Joystick getJoystick() {
        return new Joystick() {
            @Override
            public void down() {
                direction = Direction.DOWN;
            }

            @Override
            public void up() {
                direction = Direction.UP;
            }

            @Override
            public void left() {
                direction = Direction.LEFT;
            }

            @Override
            public void right() {
                direction = Direction.RIGHT;
            }

            @Override
            public void act(int... p) {
                // do nothing
            }
        };
    }

    public boolean isGameOver() {
        return getFreeRandom().equals(NO_SPACE);
    }
}
