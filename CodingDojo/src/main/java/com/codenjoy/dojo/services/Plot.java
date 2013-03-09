package com.codenjoy.dojo.services;

import com.codenjoy.dojo.snake.services.playerdata.PlotColor;

/**
 * User: oleksandr.baglai
 * Date: 10/1/12
 * Time: 3:56 AM
 */
public class Plot {
    private int x;
    private int y;
    private PlotColor color;

    public Plot(int x, int y, PlotColor color) {
        this.x = x;
        this.y = y;
        this.color = color;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public PlotColor getColor() {
        return color;
    }

    @Override
    public String toString() {
        return "Plot{" +
                "x=" + x +
                ", y=" + y +
                ", color=" + color +
                '}';
    }
}
