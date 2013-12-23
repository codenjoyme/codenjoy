package com.codenjoy.dojo.sample.model;

import com.codenjoy.dojo.services.*;

/**
 * User: sanja
 * Date: 17.12.13
 * Time: 5:10
 */
public class Hero extends PointImpl implements Joystick, Tickable {

    private Field field;
    private boolean alive;
    private Direction direction;

    public Hero(Point xy) {
        super(xy);
        direction = null;
        alive = true;
    }

    public void init(Field field) {
        this.field = field;
    }

    @Override
    public void down() {
        if (!alive) return;

        direction = Direction.DOWN;
    }

    @Override
    public void up() {
        if (!alive) return;

        direction = Direction.UP;
    }

    @Override
    public void left() {
        if (!alive) return;

        direction = Direction.LEFT;
    }

    @Override
    public void right() {
        if (!alive) return;

        direction = Direction.RIGHT;
    }

    @Override
    public void act() {
        if (!alive) return;

        field.setBomb(x, y);
    }

    public Direction getDirection() {
        return direction;
    }

    @Override
    public void tick() {
        if (!alive) return;

        if (direction != null) {
            int newX = direction.changeX(x);
            int newY = direction.inverted().changeY(y);

            if (field.isBomb(newX, newY)) {
                alive = false;
                field.removeBomb(newX, newY);
            }

            if (!field.isBarrier(newX, newY)) {
                move(newX, newY);
            }
        }
        direction = null;
    }

    public boolean isAlive() {
        return alive;
    }
}
