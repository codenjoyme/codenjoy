package com.codenjoy.dojo.battlecity.model;

import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Joystick;

import java.util.LinkedList;
import java.util.List;

public class Tank extends MovingObject implements Joystick {

    private List<Bullet> bullets;
    private Field field;
    private boolean moving;

    public Tank(int coordinateX, int coordinateY, Direction direction) {
        super(coordinateX, coordinateY, direction);
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

    public void move() {
        if (!moving) {
            return;
        }

        for (int i = 0; i < speed; i++) {
            int newX = direction.changeX(x);
            int newY = direction.inverted().changeY(y); // TODO fixme
            if (field.isBarrier(newX, newY)) {
                // do nothing
            } else {
                x = newX;
                y = newY;
            }
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
