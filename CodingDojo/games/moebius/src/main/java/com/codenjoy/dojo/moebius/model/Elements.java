package com.codenjoy.dojo.moebius.model;

import com.codenjoy.dojo.services.CharElements;
import com.codenjoy.dojo.services.Dice;

public enum Elements implements CharElements {

    LEFT_UP('╝'),
    UP_RIGHT('╚'),
    RIGHT_DOWN('╔'),
    DOWN_LEFT('╗'),
    LEFT_RIGHT('═'),
    UP_DOWN('║'),
    EMPTY(' ');

    final char ch;

    Elements(char ch) {
        this.ch = ch;
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
        return Elements.values()[dice.next(6)];
    }
}
