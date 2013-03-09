package com.codenjoy.dojo.bomberman.services;

/**
 * User: oleksandr.baglai
 * Date: 3/9/13
 * Time: 08:04 PM
 */
public enum BombermanPlotColor {
    BOMBERMAN, BOMB_BOMBERMAN, DEAD_BOMBERMAN,
    BOOM, BOMB5, BOMB4, BOMB3, BOMB2, BOMB1,
    WALL, DESTROY_WALL, MEAT_CHOPPER, DEAD_MEAT_CHOPPER,
    EMPTY;

    @Override
    public String toString() {
        return this.name().toLowerCase();
    }
}
