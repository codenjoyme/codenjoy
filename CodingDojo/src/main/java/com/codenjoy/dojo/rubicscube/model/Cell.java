package com.codenjoy.dojo.rubicscube.model;

import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;
import com.codenjoy.dojo.services.State;

public class Cell extends PointImpl implements State<Elements, Player> {
    private Elements color;

    public Cell(Point point, Elements color) {
        super(point);
        this.color = color;
    }

    @Override
    public String toString() {
        return String.format("pt%s=%s", super.toString(), color.ch);
    }

    public char getColor() {
        return color.ch;
    }

    @Override
    public Elements state(Player player, Object... alsoAtPoint) {
        return Elements.valueOf(this.getColor());
    }
}
