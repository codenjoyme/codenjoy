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
    _512('F'),
    _1024('G'),
    _2048('H'),
    _4096('I'),
    _8192('J'),
    _16384('K'),
    _32768('L'),
    _65536('M'),
    _131072('N'),
    _262144('O'),
    _524288('P'),
    _1048576('Q'),
    _2097152('R'),
    _4194304('S'),
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
