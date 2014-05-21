package com.codenjoy.dojo.a2048.model;

import com.apofig.profiler.Profiler;
import com.codenjoy.dojo.a2048.services.A2048Events;
import com.codenjoy.dojo.sample.services.SampleEvents;
import com.codenjoy.dojo.services.*;
import com.codenjoy.dojo.services.settings.Parameter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static com.codenjoy.dojo.services.PointImpl.pt;

public class A2048 implements Tickable {

    private Numbers numbers;
    private final int size;
    private Dice dice;
    private Direction direction;
    private Player player;
    private Level level;

    public A2048(Level level, Dice dice) {
        this.level = level;
        this.dice = dice;
        numbers = new Numbers(level.getNumbers(), level.getSize());
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
            numbers = merge(numbers.by(direction));
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
        numbers.addRandom(dice, level.getNewAdd());
    }

    private Numbers merge(List<Number> sorted) {
        int score = 0;

        Numbers result = new Numbers(new LinkedList<Number>(), size);
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
                Number atWay = result.get(moved);
                if (atWay.get() == number.get() && !alreadyIncreased.contains(atWay)) {
                    result.remove(atWay);
                    result.add(new Number(number.next(), atWay));

                    alreadyIncreased.add(atWay);
                    score += getScoreFor(number.next());
                } else {
                    Point prev = direction.inverted().change(moved);
                    result.add(new Number(number.get(), prev));
                }
            }
        }

        if (score > 0) {
            player.event(new A2048Events(A2048Events.Event.INC, score));
        }

        return result;
    }

    public int getScoreFor(int next) {
        return (int)Math.pow(level.getScore(), lg2(next) - 2);
    }

    private double lg2(int number) {
        return Math.ceil(Math.log(number)/Math.log(2));
    }

    public int getSize() {
        return size;
    }

    public Numbers getNumbers() {
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
        return numbers.contains(Elements._4194304);
    }
}
