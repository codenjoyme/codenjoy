package net.tetris.services;

/**
 * User: serhiy.zelenin
 * Date: 5/9/12
 * Time: 6:56 PM
 */
public enum PlotColor {
    BLUE, CYAN, GREEN, ORANGE, PURPLE, RED, YELLOW;

    public String getName() {
        return this.name().toLowerCase();
    }
}
