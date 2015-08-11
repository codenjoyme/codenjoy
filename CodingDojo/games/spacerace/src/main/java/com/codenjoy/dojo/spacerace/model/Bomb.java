package com.codenjoy.dojo.spacerace.model;

import com.codenjoy.dojo.services.*;
//test
/**
 * Артефакт Бомба на поле
 */
public class Bomb extends PointImpl implements State<Elements, Player>, Tickable {
    private Direction direction;

    public Bomb(int x, int y) {
        super(x, y);
        direction = Direction.DOWN;
    }

    public Bomb(Point point) {
        super(point);
    }

    @Override
    public Elements state(Player player, Object... alsoAtPoint) {
        return Elements.BOMB;
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
