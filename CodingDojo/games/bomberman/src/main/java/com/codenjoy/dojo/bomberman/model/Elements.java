package com.codenjoy.dojo.bomberman.model;

import com.codenjoy.dojo.services.CharElements;

/**
 * User: oleksandr.baglai
 * Date: 3/9/13
 * Time: 08:04 PM
 */
public enum Elements implements CharElements {
    BOMBERMAN('☺'), BOMB_BOMBERMAN('☻'), DEAD_BOMBERMAN('Ѡ'),
    BOOM('҉'), BOMB_FIVE('5'), BOMB_FOUR('4'), BOMB_THREE('3'), BOMB_TWO('2'), BOMB_ONE('1'),
    WALL('☼'), DESTROY_WALL('#'), DESTROYED_WALL('H'),
    MEAT_CHOPPER('&'), DEAD_MEAT_CHOPPER('x'),
    NONE(' '),
    OTHER_BOMBERMAN('♥'), OTHER_BOMB_BOMBERMAN('♠'), OTHER_DEAD_BOMBERMAN('♣');

    public final static String BOMBS = "12345";

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

    public boolean isBomb() {
        return BOMBS.indexOf(ch) != -1;
    }

    public boolean isBomberman() {
        return this == Elements.BOMBERMAN ||
               this == Elements.BOMB_BOMBERMAN ||
               this == Elements.DEAD_BOMBERMAN;
    }

}
