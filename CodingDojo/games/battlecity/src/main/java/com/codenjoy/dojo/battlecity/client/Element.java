package com.codenjoy.dojo.battlecity.client;

/**
 * User: oleksandr.baglai
 * Date: 21.04.13
 * Time: 3:10
 */
public enum Element {

    NONE(' '),
    WALL('☼'),
    DEAD('Ѡ'),

    CONSTRUCTION('╬'),

    CONSTRUCTION_DESTROYED_DOWN('╩'),
    CONSTRUCTION_DESTROYED_UP('╦'),
    CONSTRUCTION_DESTROYED_LEFT('╠'),
    CONSTRUCTION_DESTROYED_RIGHT('╣'),

    CONSTRUCTION_DESTROYED_DOWN_TWICE('╨'),
    CONSTRUCTION_DESTROYED_UP_TWICE('╥'),
    CONSTRUCTION_DESTROYED_LEFT_TWICE('╞'),
    CONSTRUCTION_DESTROYED_RIGHT_TWICE('╡'),

    CONSTRUCTION_DESTROYED_LEFT_RIGHT('│'),
    CONSTRUCTION_DESTROYED_UP_DOWN('─'),

    CONSTRUCTION_DESTROYED_UP_LEFT('┌'),
    CONSTRUCTION_DESTROYED_RIGHT_UP('┐'),
    CONSTRUCTION_DESTROYED_DOWN_LEFT('└'),
    CONSTRUCTION_DESTROYED_DOWN_RIGHT('┘'),

    CONSTRUCTION_DESTROYED(' '),

    BULLET('•'),

    TANK_UP('▲'),
    TANK_RIGHT('►'),
    TANK_DOWN('▼'),
    TANK_LEFT('◄'),

    OTHER_TANK_UP('˄'),
    OTHER_TANK_RIGHT('˃'),
    OTHER_TANK_DOWN('˅'),
    OTHER_TANK_LEFT('˂'),

    AI_TANK_UP('?'),
    AI_TANK_RIGHT('»'),
    AI_TANK_DOWN('¿'),
    AI_TANK_LEFT('«');

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
