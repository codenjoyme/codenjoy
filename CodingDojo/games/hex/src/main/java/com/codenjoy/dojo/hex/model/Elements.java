package com.codenjoy.dojo.hex.model;

import com.codenjoy.dojo.services.CharElements;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public enum Elements implements CharElements {

    NONE(' '),
    WALL('☼'),
    MY_HERO('☺'),
    HERO1('☻'),
    HERO2('♥'),
    HERO3('♦'),
    HERO4('♣'),
    HERO5('♠'),
    HERO6('•'),
    HERO7('◘'),
    HERO8('○'),
    HERO9('◙'),
    HERO10('♂'),
    HERO11('♀');

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

    public static List<Elements> heroesElements() {
        List<Elements> result = new LinkedList<Elements>(Arrays.asList(Elements.values()));
        result.remove(Elements.NONE);
        result.remove(Elements.WALL);
        result.remove(Elements.MY_HERO);
        return result;
    }

    public static Elements valueOf(char ch) {
        for (Elements el : Elements.values()) {
            if (el.ch == ch) {
                return el;
            }
        }
        throw new IllegalArgumentException("No such element for " + ch);
    }

}
