package com.codenjoy.dojo.moebius.model;

import com.codenjoy.dojo.services.CharElements;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Point;

import static com.codenjoy.dojo.services.PointImpl.pt;

public enum Elements implements CharElements {

    LEFT_UP('╝', pt(-1, 0), pt(0, 1)), // TODO Тут тоже зеркалирование по Y
    UP_RIGHT('╚', pt(0, 1), pt(1, 0)),
    RIGHT_DOWN('╔', pt(1, 0), pt(0, -1)),
    DOWN_LEFT('╗', pt(0, -1), pt(-1, 0)),
    LEFT_RIGHT('═', pt(-1, 0),  pt(1, 0)),
    UP_DOWN('║', pt(0, 1),  pt(0, -1)),
    CROSS('╬', null, null),
    EMPTY(' ', null, null);

    private final char ch;
    private final Point from;
    private final Point to;

    Elements(char ch, Point from, Point to) {
        this.ch = ch;
        this.from = from;
        this.to = to;
    }

    @Override
    public char ch() {
        return ch;
    }

    @Override
    public String toString() {
        return String.valueOf(ch);
    }

    public static Elements valueOf(char ch) {
        for (Elements el : Elements.values()) {
            if (el.ch == ch) {
                return el;
            }
        }
        throw new IllegalArgumentException("No such element for " + ch);
    }

    public static Elements random(Dice dice) {
        return Elements.values()[dice.next(Elements.values().length - 1)];
    }

    public Point from() {
        return from;
    }

    public Point to() {
        return to;
    }
}
