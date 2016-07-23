package com.codenjoy.dojo.tetris.model;

import com.codenjoy.dojo.services.PointImpl;

/**
 * User: serhiy.zelenin
 * Date: 5/9/12
 * Time: 6:55 PM
 */
public class Plot extends PointImpl{
    private PlotColor color;

    public Plot(int x, int y, PlotColor color) {
        super(x, y);
        this.color = color;
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
