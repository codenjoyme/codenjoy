package com.codenjoy.dojo.bomberman.model;

/**
 * User: oleksandr.baglai
 * Date: 3/9/13
 * Time: 08:04 PM
 */
public enum Elements {
    BOMBERMAN('☺'), BOMB_BOMBERMAN('☻'), DEAD_BOMBERMAN('Ѡ'),
    BOOM('҉'), BOMB_FIVE('5'), BOMB_FOUR('4'), BOMB_THREE('3'), BOMB_TWO('2'), BOMB_ONE('1'),
    WALL('☼'), DESTROY_WALL('#'), DESTROYED_WALL('H'),
    MEAT_CHOPPER('&'), DEAD_MEAT_CHOPPER('x'),
    EMPTY(' '),
    OTHER_BOMBERMAN('♥'), OTHER_BOMB_BOMBERMAN('♠'), OTHER_DEAD_BOMBERMAN('♣');

    public final static String BOMBS = "12345";

    private char ch;

    Elements(char ch) {
        this.ch = ch;
    }

    @Override
    public String toString() {
        return String.valueOf(ch);
    }

    public static Elements getBomb(int timer) {
        switch (timer) {
            case 1 : return BOMB_ONE;
            case 2 : return BOMB_TWO;
            case 3 : return BOMB_THREE;
            case 4 : return BOMB_FOUR;
            case 5 : return BOMB_FIVE;
            default : return BOOM;
        }
    }

    public boolean isBomb() {
        return BOMBS.indexOf(ch) != -1;
    }

    public boolean isBomberman() {
        return this == Elements.BOMBERMAN ||
               this == Elements.BOMB_BOMBERMAN ||
               this == Elements.DEAD_BOMBERMAN;
    }

    public boolean isMeatChopper() {
        return this == Elements.MEAT_CHOPPER ||
               this == Elements.DEAD_MEAT_CHOPPER;
    }

    public boolean isDestroyWall() {
        return this == Elements.DESTROY_WALL ||
                this == Elements.DESTROYED_WALL;
    }

    public boolean isOtherBomberman() {
        return this == Elements.OTHER_BOMBERMAN ||
                this == Elements.OTHER_BOMB_BOMBERMAN ||
                this == Elements.OTHER_DEAD_BOMBERMAN;
    }
}
