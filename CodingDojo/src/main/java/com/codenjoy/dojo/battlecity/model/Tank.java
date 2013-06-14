package com.codenjoy.dojo.battlecity.model;

import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Joystick;
import com.codenjoy.dojo.services.Point;

import java.util.LinkedList;
import java.util.List;

public class Tank extends MovingObject implements Joystick {

    private List<Bullet> bullets;
    private Field field;

    public Tank(int x, int y, Direction direction) {
        super(x, y, direction);
        bullets = new LinkedList<Bullet>();
        speed = 1;
        moving = false;
    }

    @Override
    public void up() {
        direction = Direction.UP;
        moving = true;
    }

    @Override
    public void down() {
       direction = Direction.DOWN;
        moving = true;
    }

    @Override
    public void right() {
        direction = Direction.RIGHT;
        moving = true;
    }

    @Override
    public void left() {
        direction = Direction.LEFT;
        moving = true;
    }

    @Override
    public void moving(int newX, int newY) {
        if (field.isBarrier(newX, newY)) {
            // do nothing
        } else {
            x = newX;
            y = newY;
        }
        moving = false;
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
