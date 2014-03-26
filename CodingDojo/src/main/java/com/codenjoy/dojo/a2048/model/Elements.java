package com.codenjoy.dojo.a2048.model;

public enum Elements {

    _2('2'),
    _4('4'),
    _8('8'),
    _16('A'),
    _32('B'),
    _64('C'),
    _128('D'),
    _256('E'),
    _1024('F'),
    _2048('G'),
    _4096('H'),
    NONE(' ');

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

    public static Elements valueOf(Number number) {
        return Elements.valueOf("_" + String.valueOf(number.get()));
    }

    public int number() {
        return Integer.valueOf(super.toString().substring(1));
    }
}
