package com.codenjoy.dojo.hex.model;

import com.codenjoy.dojo.services.*;

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
    public void act(int... p) {
        if (!alive) return;
    }

    public Direction getDirection() {
        return direction;
    }

    @Override
    public void tick() {
        if (!alive) return;

        if (direction != null) {
            int newX = direction.changeX(x);
            int newY = direction.changeY(y);

            if (!field.isBarrier(newX, newY)) {
                field.addHero(newX, newY, this);
            }
        }
        direction = null;
    }


    public boolean isAlive() {
        return alive;
    }
}
