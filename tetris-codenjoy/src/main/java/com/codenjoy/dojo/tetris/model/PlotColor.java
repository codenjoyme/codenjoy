package com.codenjoy.dojo.tetris.model;

/**
 * User: serhiy.zelenin
 * Date: 5/9/12
 * Time: 6:56 PM
 */
public enum PlotColor {
    BLUE, CYAN, ORANGE, YELLOW, GREEN, PURPLE, RED;

    public String getName() {
        return this.name().toLowerCase();
    }
}
