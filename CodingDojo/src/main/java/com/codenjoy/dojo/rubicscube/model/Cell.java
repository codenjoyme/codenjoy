package com.codenjoy.dojo.rubicscube.model;

import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;

public class Cell extends PointImpl {
    private Element color;

    public Cell(Point point, Element color) {
        super(point);
        this.color = color;
    }

    @Override
    public String toString() {
        return String.format("pt%s=%s", super.toString(), color.getChar());
    }

    public char getColor() {
        return color.getChar();
    }
}
