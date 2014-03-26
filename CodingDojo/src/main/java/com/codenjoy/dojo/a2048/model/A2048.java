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

        List<Number> newNumbers = new LinkedList<Number>();

        for (Number number : numbers) {
            Point moved = number;
            while (true) {
                Point temp = direction.change(moved);
                if (temp.isOutOf(size)) {
                    break;
                } else {
                    moved = temp;
                }
                if (numbers.contains(moved)) break;
            }

            if (!numbers.contains(moved) || moved.equals(number)) {
                newNumbers.add(new Number(number.get(), moved));
            } else {
//                Number atWay = numbers.get(numbers.indexOf(moved));
//                if (atWay.get() == number.get()) {
//                    newNumbers.add(new Number(number.next(), atWay));
//                }
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
