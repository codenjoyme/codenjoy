package com.codenjoy.dojo.hex.model;

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
