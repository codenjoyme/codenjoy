package com.codenjoy.dojo.sudoku.model;

import com.codenjoy.dojo.services.CharElements;

public enum Elements implements CharElements {

    NONE(' '),
    BORDER('☼'),
    ONE('1'),
    TWO('2'),
    THREE('3'),
    FOUR('4'),
    FIVE('5'),
    SIX('6'),
    SEVEN('7'),
    EIGHT('8'),
    NINE('9');

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

    public static Elements valueOf(int n) {
        for (Elements el : Elements.values()) {
            if (String.valueOf(n).equals("" + el.ch)) {
                return el;
            }
        }
        throw new IllegalArgumentException("Нет такого елемента: " + n);
    }

}
