package com.codenjoy.dojo.battlecity.model;

import com.codenjoy.dojo.services.Joystick;

import java.util.Iterator;
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
