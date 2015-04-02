package com.codenjoy.dojo.sudoku.client;

/**
 * User: oleksandr.baglai
 * Date: 21.04.13
 * Time: 3:10
 */
public enum Element {

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

    private char ch;

    Element(char ch) {
        this.ch = ch;
    }

    public char getChar() {
        return ch;
    }

    public int getValue() {
        if (ch == ' ') return 0;
        return Integer.valueOf("" + ch);
    }

    public static Element valueOf(char ch) {
        for (Element el : Element.values()) {
            if (el.ch == ch) {
                return el;
            }
        }
        throw new IllegalArgumentException("No such Elment for " + ch);
    }

    public String toString() {
        if (ch == ' ') return "?";
        return "" + ch;
    }
}
