package com.codenjoy.dojo.minesweeper.model;

import com.codenjoy.dojo.services.CharElements;

/**
 * User: oleksandr.baglai
 * Date: 3/24/13
 * Time: 12:03 AM
 */
public enum Elements implements CharElements {

    BANG('Ѡ'),
    HERE_IS_BOMB('☻'),
    DETECTOR('☺'),
    FLAG('‼'),
    HIDDEN('*'),

    ONE_MINE('1'),
    TWO_MINES('2'),
    THREE_MINES('3'),
    FOUR_MINES('4'),
    FIVE_MINES('5'),
    SIX_MINES('6'),
    SEVEN_MINES('7'),
    EIGHT_MINES('8'),

    BORDER('☼'),
    NONE(' '),
    DESTROYED_BOMB('x');

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

    public static Elements printMinesCount(int minesNear) {
        switch (minesNear) {
            case 1 : return Elements.ONE_MINE;
            case 2 : return Elements.TWO_MINES;
            case 3 : return Elements.THREE_MINES;
            case 4 : return Elements.FOUR_MINES;
            case 5 : return Elements.FIVE_MINES;
            case 6 : return Elements.SIX_MINES;
            case 7 : return Elements.SEVEN_MINES;
            case 8 : return Elements.EIGHT_MINES;
            default : return Elements.NONE;
        }
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
