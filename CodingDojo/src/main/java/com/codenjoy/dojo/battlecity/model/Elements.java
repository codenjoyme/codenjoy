package com.codenjoy.dojo.battlecity.model;

/**
 * User: sanja
 * Date: 06.06.13
 * Time: 17:38
 */
public enum Elements {

    GROUND(' '),
    WALL('☼'),
    CONSTRUCTION('■'),
    BULLET('•'),
    TANK_UP('▲'),
    TANK_RIGHT('►'),
    TANK_DOWN('▼'),
    TANK_LEFT('◄');

    char ch;

    Elements(char ch) {
        this.ch = ch;
    }

}
