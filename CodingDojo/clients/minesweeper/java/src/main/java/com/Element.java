package com;

/**
 * User: oleksandr.baglai
 * Date: 21.04.13
 * Time: 3:10
 */
public enum Element {

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
    NO_MINE(' '),
    DESTROYED_BOMB('x');

    private char ch;

    Element(char ch) {
        this.ch = ch;
    }

    public char getChar() {
        return ch;
    }

    public static Element valueOf(char ch) {
        for (Element el : Element.values()) {
            if (el.ch == ch) {
                return el;
            }
        }
        throw new IllegalArgumentException("No such Elment for " + ch);
    }
}
