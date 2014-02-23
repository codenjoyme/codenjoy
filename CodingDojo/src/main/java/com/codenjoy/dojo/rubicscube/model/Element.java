package com.codenjoy.dojo.rubicscube.model;

public enum Element {

    NONE(' '),
    RED('R'),
    GREEN('G'),
    BLUE('B'),
    WHITE('W'),
    YELLOW('Y'),
    ORANGE('O');

    private char ch;

    public char getChar() {
        return ch;
    }

    Element(char ch) {
        this.ch = ch;
    }

    @Override
    public String toString() {
        return String.valueOf(ch);
    }

    public static Element valueOf(char ch) {
        for (Element el : Element.values()) {
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
