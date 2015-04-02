package com.codenjoy.dojo.hex.model;

import com.codenjoy.dojo.services.CharElements;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public enum Elements implements CharElements {

    NONE(' '),
    WALL('☼'),
    HERO1('☺'),  // TODO Мои всегда должны быть моего цвета! А все остальные пусть как угодно отображаются
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
        return result;
    }

}
