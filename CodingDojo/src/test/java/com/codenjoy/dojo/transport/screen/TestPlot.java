package com.codenjoy.dojo.transport.screen;

/**
 * User: serhiy.zelenin
 * Date: 9/26/13
 * Time: 2:44 PM
 */
public class TestPlot {
    private int x;
    private int y;
    private TestPlotColor color;

    public TestPlot(int x, int y, TestPlotColor color) {
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

    public TestPlotColor getColor() {
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
