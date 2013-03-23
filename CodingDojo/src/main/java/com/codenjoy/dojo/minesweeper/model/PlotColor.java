package com.codenjoy.dojo.minesweeper.model;

/**
 * User: oleksandr.baglai
 * Date: 3/24/13
 * Time: 12:03 AM
 */
public enum PlotColor {
    MINESWEEPER('☺'), WALL('☼'), EMPTY(' ');

    private char ch;

    PlotColor(char ch) {
        this.ch = ch;
    }

    @Override
    public String toString() {
        return String.valueOf(ch);
    }
}
