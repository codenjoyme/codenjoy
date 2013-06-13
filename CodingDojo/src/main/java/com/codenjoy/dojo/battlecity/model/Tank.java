package com.codenjoy.dojo.battlecity.model;

import com.codenjoy.dojo.services.Joystick;

import java.util.LinkedList;
import java.util.List;

public class Tank extends MovingObject implements Joystick {

    private List<Bullet> bullets;
    private Field field;

    public Tank(int coordinateX, int coordinateY, Direction direction) {
        super(coordinateX, coordinateY, direction);
        this.bullets = new LinkedList<Bullet>();
        this.speed = 1;
    }

    @Override
    public void up() {
        if (y + 1 + speed < field.getSize()) {
            tryToveTo(x, y + speed);
        }
        direction = Direction.UP;
    }

    @Override
    public void down() {
        if (y - 1 > 0) {
            tryToveTo(x, y - speed);
        }
        direction = Direction.DOWN;
    }

    @Override
    public void right() {
        if (x + 1 + speed < field.getSize()) {
            tryToveTo(x + speed, y);
        }
        direction = Direction.RIGHT;
    }

    @Override
    public void left() {
        if (x - 1 > 0) {
            tryToveTo(x - speed, y);
        }
        direction = Direction.LEFT;
    }

    @Override
    public void move() {
        if (!field.isBreakAt(newPosition)) {
            super.move();
        } else {
            newPosition = null;
        }
    }

    @Override
    public void act() {
        bullets.add(new Bullet(field, direction, copy(), new OnDestroy() {
            @Override
            public void destroy(Object bullet) {
                Tank.this.bullets.remove(bullet);
            }
        }));
    }

    public Iterable<Bullet> getBullets() {
        return new LinkedList<Bullet>(bullets);
    }

    public void setField(Field field) {
        this.field = field;
    }
}
