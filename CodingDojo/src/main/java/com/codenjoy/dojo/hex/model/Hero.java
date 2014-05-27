package com.codenjoy.dojo.hex.model;

import com.codenjoy.dojo.services.*;

public class Hero extends PointImpl implements Joystick, Tickable {

    private Field field;

    private Direction direction;

    public Hero(Point xy) {
        super(xy);
        direction = null;
    }

    public void init(Field field) {
        this.field = field;
    }

    @Override
    public void down() {
        direction = Direction.DOWN;
    }

    @Override
    public void up() {
        direction = Direction.UP;
    }

    @Override
    public void left() {
        direction = Direction.LEFT;
    }

    @Override
    public void right() {
        direction = Direction.RIGHT;
    }

    @Override
    public void act(int... p) {
        // do nothing
    }

    public Direction getDirection() {
        return direction;
    }

    @Override
    public void tick() {
        if (direction != null) {
            int newX = direction.changeX(x);
            int newY = direction.changeY(y);

            if (!field.isBarrier(newX, newY)) {
                field.addHero(newX, newY, this);
            }
        }
        direction = null;
    }

}

