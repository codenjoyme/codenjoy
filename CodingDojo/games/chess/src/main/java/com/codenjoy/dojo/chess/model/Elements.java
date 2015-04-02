package com.codenjoy.dojo.chess.model;

import com.codenjoy.dojo.services.CharElements;

public enum Elements implements CharElements {

    NONE(' '),

    WHITE_SLON('s'),
    WHITE_LADIA('t'),
    WHITE_PESHKA('p'),
    WHITE_FERZ('f'),
    WHITE_KOROL('a'),
    WHITE_KON('k'),

    BLACK_SLON('S'),
    BLACK_LADIA('T'),
    BLACK_PESHKA('P'),
    BLACK_FERZ('F'),
    BLACK_KOROL('A'),
    BLACK_KON('K');

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

}
