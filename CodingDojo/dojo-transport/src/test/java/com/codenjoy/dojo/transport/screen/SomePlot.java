package com.codenjoy.dojo.transport.screen;

/**
 * User: serhiy.zelenin
 * Date: 9/26/13
 * Time: 2:44 PM
 */
public class SomePlot {
    private int x;
    private int y;
    private SomePlotColor color;

    public SomePlot(int x, int y, SomePlotColor color) {
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

    public SomePlotColor getColor() {
        return color;
    }

    @Override
    public String toString() {
        return "SomePlot{" +
                "x=" + x +
                ", y=" + y +
                ", color=" + color +
                '}';
    }
}
