package com.codenjoy.dojo.transport.screen;

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
