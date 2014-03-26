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
    _512('F'),
    _1024('G'),
    _2048('H'),
    _4096('I'),
    _8192('J'),
    _16384('K'),
    _32768('L'),
    _65536('M'),
    _131072('N'),
    _262144('O'),
    _524288('P'),
    _1048576('Q'),
    _2097152('R'),
    _4194304('S'),
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
