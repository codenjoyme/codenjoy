package com.codenjoy.dojo.sample.model;

/**
 * User: sanja
 * Date: 06.06.13
 * Time: 17:38
 */
public enum Elements {

    NONE(' '),
    WALL('☼'),
    HERO('☺'),
    OTHER_HERO('☻'),
    DEAD_HERO('X'),
    GOLD('$'),
    BOMB('x');

    char ch;

    public char getChar() {
        return ch;
    }

    Elements(char ch) {
        this.ch = ch;
    }

    @Override
    public String toString() {
        return String.valueOf(ch);
    }

}
