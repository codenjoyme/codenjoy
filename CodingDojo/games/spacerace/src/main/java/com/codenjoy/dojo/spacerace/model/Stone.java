package com.codenjoy.dojo.spacerace.model;

import com.codenjoy.dojo.services.*;

/**
 * Артефакт Камень на поле
 */
public class Stone extends PointImpl implements State<Elements, Player>, Tickable {
    private Direction direction;

    public Stone(int x, int y) {
        super(x, y);
        direction = Direction.DOWN;
    }

    public Stone(Point pt) {
        super(pt);
    }

    @Override
    public Elements state(Player player, Object... alsoAtPoint) {
        return Elements.STONE;
    }

    @Override
    public void tick() {
        if (direction != null) {

            int newX = direction.changeX(x);
            int newY = direction.changeY(y);
            move(newX, newY);
        }

    }
}
