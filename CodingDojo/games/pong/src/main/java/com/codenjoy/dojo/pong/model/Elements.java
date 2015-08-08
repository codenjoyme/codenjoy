package com.codenjoy.dojo.pong.model;

import com.codenjoy.dojo.services.CharElements;

public enum Elements implements CharElements {

    NONE(' '),
    VERTICAL_WALL('|'),
    HORIZONTAL_WALL('-'),
    BALL('o'),
    PANEL('#'),
    HERO('H');

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

}
