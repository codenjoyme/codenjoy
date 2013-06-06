package com.codenjoy.dojo.battlecity.model;

/**
 * User: sanja
 * Date: 06.06.13
 * Time: 17:38
 */
public enum Elements {

    GROUND_SYMBOL('*'),
    WALL_SYMBOL('X'),
    CONSTRUCTION_SYMBOL('■'),
    BULLET_SYMBOL('•'),
    TANK_UP('▲'),
    TANK_RIGHT('►'),
    TANK_DOWN('▼'),
    TANK_LEFT('◄');

    char ch;

    Elements(char ch) {
        this.ch = ch;
    }

}
