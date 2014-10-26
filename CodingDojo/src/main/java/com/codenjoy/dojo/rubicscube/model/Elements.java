package com.codenjoy.dojo.rubicscube.model;

import com.codenjoy.dojo.services.CharElements;

public enum Elements implements CharElements {

    NONE(' '),
    RED('R'),
    GREEN('G'),
    BLUE('B'),
    WHITE('W'),
    YELLOW('Y'),
    ORANGE('O');

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
        throw new IllegalArgumentException("Нет такого елемента: " + ch);
    }

    public char value() {
        return ch;
    }
}
