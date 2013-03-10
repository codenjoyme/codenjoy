package com.codenjoy.dojo.bomberman.services;

/**
 * User: oleksandr.baglai
 * Date: 3/9/13
 * Time: 08:04 PM
 */
public enum BombermanPlotColor {
    BOMBERMAN, BOMB_BOMBERMAN, DEAD_BOMBERMAN,
    BOOM, BOMB_FIVE, BOMB_FOUR, BOMB_THREE, BOMB_TWO, BOMB_ONE,
    WALL, DESTROY_WALL, MEAT_CHOPPER, DEAD_MEAT_CHOPPER,
    EMPTY;

    @Override
    public String toString() {
        return this.name().toLowerCase();
    }
}
