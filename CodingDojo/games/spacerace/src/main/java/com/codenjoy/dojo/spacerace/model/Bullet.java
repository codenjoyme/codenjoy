package com.codenjoy.dojo.spacerace.model;

import com.codenjoy.dojo.services.*;

/**
 * Артефакт пуля на поле
 */
public class Bullet extends PointImpl implements Tickable, State<Elements, Player> {

    private Direction direction;
    private Hero hero;

    public Bullet(Point pt, Hero hero) {
        super(pt);
        this.hero = hero;
    }

    public Bullet(int x, int y, Hero hero) {
        super(x, y);
        direction = Direction.UP;
        this.hero = hero;
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

    public Hero getOwner() {
        return hero;
    }
}