package com.codenjoy.dojo.a2048.model;

import com.codenjoy.dojo.services.GamePrinter;
import com.codenjoy.dojo.services.Point;

import java.util.List;

/**
 * User: sanja
 * Date: 24.12.13
 * Time: 3:41
 */
public class A2048Printer implements GamePrinter {

    private final A2048 game;
    private Player player;

    private Numbers numbers;

    public A2048Printer(A2048 game) {
        this.game = game;
    }

    @Override
    public boolean init() {
        numbers = game.getNumbers();
        return true;
    }

    @Override
    public char get(Point pt) {
        if (numbers.contains(pt)) {
            Number number = numbers.get(pt);
            if (number.get() == Numbers.BREAK) {
                return Elements._x.ch;
            }
            return Elements.valueOf(number).ch;
        }

        return Elements.NONE.ch;
    }

    @Override
    public void printAll(Filler filler) {
        // TODO использовать его
    }
}
