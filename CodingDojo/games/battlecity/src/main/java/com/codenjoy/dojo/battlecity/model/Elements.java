package com.codenjoy.dojo.battlecity.model;

import com.codenjoy.dojo.services.CharElements;

/**
 * User: sanja
 * Date: 06.06.13
 * Time: 17:38
 */
public enum Elements implements CharElements {

    NONE(' '),
    BATTLE_WALL('☼'),
    BANG('Ѡ'),

    CONSTRUCTION('╬', 3),

    CONSTRUCTION_DESTROYED_DOWN('╩', 2),
    CONSTRUCTION_DESTROYED_UP('╦', 2),
    CONSTRUCTION_DESTROYED_LEFT('╠', 2),
    CONSTRUCTION_DESTROYED_RIGHT('╣', 2),

    CONSTRUCTION_DESTROYED_DOWN_TWICE('╨', 1),
    CONSTRUCTION_DESTROYED_UP_TWICE('╥', 1),
    CONSTRUCTION_DESTROYED_LEFT_TWICE('╞', 1),
    CONSTRUCTION_DESTROYED_RIGHT_TWICE('╡', 1),

    CONSTRUCTION_DESTROYED_LEFT_RIGHT('│', 1),
    CONSTRUCTION_DESTROYED_UP_DOWN('─', 1),

    CONSTRUCTION_DESTROYED_UP_LEFT('┌', 1),
    CONSTRUCTION_DESTROYED_RIGHT_UP('┐', 1),
    CONSTRUCTION_DESTROYED_DOWN_LEFT('└', 1),
    CONSTRUCTION_DESTROYED_DOWN_RIGHT('┘', 1),

    CONSTRUCTION_DESTROYED(' ', 0),

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

    public final char ch;
    int power;

    @Override
    public char ch() {
        return ch;
    }

    Elements(char ch) {
        this.ch = ch;
        this.power = -1;
    }

    Elements(char ch, int power) {
        this.ch = ch;
        this.power = power;
    }

    @Override
    public String toString() {
        return String.valueOf(ch);
    }

}
