package com.codenjoy.dojo.spacerace.model;

import com.codenjoy.dojo.services.*;

/**
 * Артефакт пуля на поле
 */
public class Bullet extends PointImpl implements Tickable, State<Elements, Player> {

    private Direction direction;

    public Bullet(Point pt) {
        super(pt);
    }

    public Bullet(int x, int y) {
        super(x, y);
        direction = Direction.UP;
    }

    @Override
    public Elements state(Player player, Object... alsoAtPoint) {
        return Elements.BULLET;
    }

    @Override
    public void tick() {
        int newX = direction.changeX(x);
        int newY = direction.changeY(y);
        move(newX, newY);
    }
}