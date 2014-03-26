package com.codenjoy.dojo.a2048.model;

import com.codenjoy.dojo.a2048.services.A2048Events;
import com.codenjoy.dojo.sample.services.SampleEvents;
import com.codenjoy.dojo.services.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static com.codenjoy.dojo.services.PointImpl.pt;

public class A2048 implements Tickable {

    public static final Point NO_SPACE = pt(-1, -1);

    private List<Number> numbers;
    private final int size;
    private Dice dice;
    private Direction direction;
    private Player player;

    public A2048(Level level, Dice dice) {
        this.dice = dice;
        numbers = level.getNumbers();
        size = level.getSize();
    }

    public void newGame(Player player) {
        direction = null;
        if (this.player != null) {
            numbers.clear();
        }
        this.player = player;
    }

    @Override
    public void tick() {
        if (isGameOver()) {
            return;
        }

        if (numbers.isEmpty()) {
            direction = Direction.DOWN;
        }

        if (direction != null) {
            List<Number> sorted = sortByDirection(direction);
            numbers = merge(sorted);
            generateNewNumber();
        }

        if (isWin()) {
            player.event(new A2048Events(A2048Events.Event.WIN));
        } else if (isGameOver()) {
            player.event(new A2048Events(A2048Events.Event.GAME_OVER));
        }

        direction = null;
    }

    private void generateNewNumber() {
        Point pt = getFreeRandom();
        if (!pt.itsMe(NO_SPACE)) {
            numbers.add(new Number(2, pt));
        }
    }

    private List<Number> merge(List<Number> sorted) {
        List<Number> result = new LinkedList<Number>();
        List<Point> alreadyIncreased = new LinkedList<Point>();
        for (Number number : sorted) {
            Point moved = number;
            while (true) {
                Point temp = direction.change(moved);
                if (temp.isOutOf(size)) {
                    break;
                } else {
                    moved = temp;
                }
                if (result.contains(moved)) break;
            }

            if (!result.contains(moved) || moved.equals(number)) {
                result.add(new Number(number.get(), moved));
            } else {
                Number atWay = result.get(result.indexOf(moved));
                if (atWay.get() == number.get() && !alreadyIncreased.contains(atWay)) {
                    result.remove(result.indexOf(atWay));
                    result.add(new Number(number.next(), atWay));

                    alreadyIncreased.add(atWay);

                    player.event(new A2048Events(A2048Events.Event.INC, number.next()));
                } else {
                    Point prev = direction.inverted().change(moved);
                    result.add(new Number(number.get(), prev));
                }
            }
        }
        return result;
    }

    private List<Number> sortByDirection(Direction direction) {
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
        return sorted;
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
        return !numbers.contains(pt(x, y));
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
        if (isWin()) return true;

        boolean result = true;
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                Point pt = pt(x, y);
                result &= numbers.contains(pt);
            }
        }
        return result;
    }

    private boolean isWin() {
        for (Number number : numbers) {
            if (number.get() == Elements._2048.number()) {
                return true;
            }
        }
        return false;
    }
}
