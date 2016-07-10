package net.tetris.services;

import com.codenjoy.dojo.tetris.model.PlotColor;

/**
 * User: serhiy.zelenin
 * Date: 5/9/12
 * Time: 6:55 PM
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
