//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.codenjoy.dojo.minesweeper.client.ai;

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

    private Element(char ch) {
        this.ch = ch;
    }

    public char getChar() {
        return this.ch;
    }

    public static Element valueOf(char ch) {
        Element[] arr$ = values();
        int len$ = arr$.length;

        for(int i$ = 0; i$ < len$; ++i$) {
            Element el = arr$[i$];
            if (el.ch == ch) {
                return el;
            }
        }

        throw new IllegalArgumentException("No such Element for " + ch);
    }
}
