package com.codenjoy.dojo.sudoku.model;

import com.codenjoy.dojo.services.CharElements;

public enum Elements implements CharElements {

    NONE(' '),   // отгадай, что тут за цифра
    BORDER('☼'), // граница, проигнорь ее ;) она не учитывается в координатах
    ONE('1'),    // циферки
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

    public static Elements valueOf(char ch) {
        for (Elements el : Elements.values()) {
            if (el.ch == ch) {
                return el;
            }
        }
        throw new IllegalArgumentException("No such element for " + ch);
    }

    public Integer value() {
        if (this == NONE) {
            return 0;
        }
        if (this == BORDER) {
            return -1;
        }
        return Integer.valueOf("" + ch);
    }
}
