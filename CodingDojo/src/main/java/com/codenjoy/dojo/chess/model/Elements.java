package com.codenjoy.dojo.chess.model;

import com.codenjoy.dojo.chess.model.figures.Figure;
import com.codenjoy.dojo.chess.model.figures.Kon;
import com.codenjoy.dojo.chess.model.figures.Slon;

public enum Elements {

    NONE(' '),
    SLON('s'),
    LADIA('t'),
    PESHKA('p'),
    FERZ('f'),
    KOROL('a'),
    KON('k');

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

    public static Elements valueOf(Figure figure) {
        if (figure.getClass().equals(Kon.class)) return KON;
        if (figure.getClass().equals(Slon.class)) return SLON;
        if (figure.getClass().equals(Kon.class)) return PESHKA;
        if (figure.getClass().equals(Kon.class)) return LADIA;
        if (figure.getClass().equals(Kon.class)) return FERZ;
        return KOROL;
    }
}
