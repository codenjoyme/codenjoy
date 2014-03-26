package com;

/**
 * User: oleksandr.baglai
 * Date: 21.04.13
 * Time: 3:10
 */
public enum Element {

    _2('2'),
    _4('4'),
    _8('8'),
    _16('A'),
    _32('B'),
    _64('C'),
    _128('D'),
    _256('E'),
    _1024('F'),
    _2048('G'),
    NONE(' ');

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
