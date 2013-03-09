package com.codenjoy.dojo.snake.services.playerdata;

/**
 * User: oleksandr.baglai
 * Date: 10/1/12
 * Time: 3:57 AM
 */
public enum PlotColor {
    HEAD, TAIL, BODY, STONE, APPLE, EMPTY, WALL;

    public String getName() {
        return this.name().toLowerCase();
    }
}
