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
        size = level.getSize();
        numbers = new Numbers(level.getNumbers(), level.getSize(), getBreak(level.getMode()));
    }

    private List<Number> getBreak(Mode mode) {
        LinkedList<Number> result = new LinkedList<Number>();
        if (mode == Mode.CLASSIC) return result;

        if (size < 5) return result;

        if (size == 5) {
            result.add(new Number(Numbers.BREAK, pt(0, 2)));
            result.add(new Number(Numbers.BREAK, pt(2, 0)));
            result.add(new Number(Numbers.BREAK, pt(4, 2)));
            result.add(new Number(Numbers.BREAK, pt(2, 4)));
        }

        return result;
    }

    public void newGame(Player player) {
        direction = null;
        if (this.player != null) {
            numbers.clear();
        }
        this.player = player;
    }

    int oldSum = 0; // TODO подумай над этим

    @Override
    public void tick() {
        if (isGameOver()) {
            return;
        }

        if (numbers.isEmpty()) {
            direction = Direction.DOWN;
        }

        if (direction != null) {
            numbers.move(direction);

            generateNewNumber();
        }

        int sum = numbers.getSum();
        if (sum > oldSum) {
            player.event(new A2048Events(A2048Events.Event.INC, sum - oldSum));
            oldSum = sum;
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
        if (!numbers.isFull()) return false;
        return !numbers.canGo();
    }

    private boolean isWin() {
        return numbers.contains(Elements._4194304);
    }
}
