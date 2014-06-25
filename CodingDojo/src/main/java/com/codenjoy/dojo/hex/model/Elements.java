package com.codenjoy.dojo.hex.model;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public enum Elements {

    NONE(' '),
    WALL('☼'),
    HERO1('☺'),
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

    char ch;

    public char getChar() {
        return ch;
    }

    Elements(char ch) {
        this.ch = ch;
    }

    @Override
    public String toString() {
        return String.valueOf(ch);
    }

    public static List<Elements> heroesElements() {
        List<Elements> result = new LinkedList<Elements>(Arrays.asList(Elements.values()));
        result.remove(Elements.NONE);
        result.remove(Elements.WALL);
        return result;
    }

}
