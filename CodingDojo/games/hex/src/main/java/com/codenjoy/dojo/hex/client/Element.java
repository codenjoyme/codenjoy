package com.codenjoy.dojo.hex.client;

/**
 * User: oleksandr.baglai
 * Date: 26.06.14
 * Time: 1:02
 */
public enum Element {

    NONE(' '),     // свободная территория
    WALL('☼'),     // стенка
    HERO1('☺'),    // игроки
    HERO2('☻'),
    HERO3('♥'),
    HERO4('♦'),
    HERO5('♣'),
    HERO6('♠'),
    HERO7('•'),
    HERO8('◘'),
    HERO9('○'),
    HERO10('◙'),
    HERO11('♂'),
    HERO12('♀');

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
