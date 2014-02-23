package com;

/**
 * User: oleksandr.baglai
 * Date: 21.04.13
 * Time: 3:10
 */
public enum Element {

    NONE(' '),     // это то, на что не стоит обращать внимание
    RED('R'),      // а дальше идут цвета
    GREEN('G'),
    YELLOW('Y'),
    WHITE('W'),
    ORANGE('O'),
    BLUE('B');

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
