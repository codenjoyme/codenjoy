package com.codenjoy.dojo.battlecity.model;

import com.codenjoy.dojo.services.Joystick;

public class Tank extends MovingObject implements Joystick {

    private Bullet bullet = null;
    private Field field;

    public Tank(int coordinateX, int coordinateY, Direction direction) {
        super(coordinateX, coordinateY, direction);
        this.speed = 1;
    }

    @Override
    public void up() {
        save();
        if (y + 1 + speed < field.getSize()) {
            y += speed;
        }
        direction = Direction.UP;
        if (field.isBreakAt(this)) {
            goBack();
        }
    }

    @Override
    public void down() {
        save();
        if (y - 1 > 0) {
            y -= speed;
        }
        direction = Direction.DOWN;
        if (field.isBreakAt(this)) {
            goBack();
        }
    }

    @Override
    public void right() {
        save();
        if (x + 1 + speed < field.getSize()) {
            x += speed;
        }
        direction = Direction.RIGHT;
        if (field.isBreakAt(this)) {
            goBack();
        }
    }

    @Override
    public void left() {
        save();
        if (x - 1 > 0) {
            x -= speed;
        }
        direction = Direction.LEFT;
        if (field.isBreakAt(this)) {
            goBack();
        }
    }

    @Override
    public void act() {
        bullet = new Bullet(field, direction, copy(), new OnDestroy() {
            @Override
            public void destroy() {
                bullet = null;
            }
        });
    }

    public Bullet getBullet() {
        return bullet;
    }

    public void setField(Field field) {
        this.field = field;
    }

}
