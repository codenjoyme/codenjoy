package com.codenjoy.dojo.battlecity.model;

import com.codenjoy.dojo.services.Direction;

public class Bullet extends MovingObject {

    private Field field;
    private OnDestroy onDestroy;
    private boolean destroyed;

    public Bullet(Field field, Direction tankDirection, Point from, OnDestroy onDestroy) {
        super(from.getX(), from.getY(), tankDirection);
        this.field = field;
        destroyed = false;
        this.onDestroy = onDestroy;
        this.speed = 2;
    }

    public void onDestroy() {
        destroyed = true;
        if (onDestroy != null) {
            onDestroy.destroy(this);
        }
    }

    public void moveUp() {
        for (int i = 0; i < speed; i++) {
            if (destroyed) {
                return;
            }
            if (y + 1 < field.getSize()) {
                go();
            } else {
                onDestroy();
            }
        }
    }

    private void go() {
        x = direction.changeX(x);
        y = direction.inverted().changeY(y); // TODO fixme
        field.affect(this);
    }

    public void moveDown() {
        for (int i = 0; i < speed; i++) {
            if (destroyed) {
                return;
            }
            if (y > 1) {
                go();
            } else {
                onDestroy();
            }
        }
    }

    public void moveRight() {
        for (int i = 0; i < speed; i++) {
            if (destroyed) {
                return;
            }
            if (x + 1 < field.getSize()) {
                go();
            } else {
                onDestroy();
            }
        }
    }

    public void moveLeft() {
        for (int i = 0; i < speed; i++) {
            if (destroyed) {
                return;
            }
            if (x > 1) {
                go();
            } else {
                onDestroy();
            }
        }
    }

    public void move() {
        if (direction.equals(Direction.UP)) {
            moveUp();
        }
        if (direction.equals(Direction.DOWN)) {
            moveDown();
        }
        if (direction.equals(Direction.RIGHT)) {
            moveRight();
        }
        if (direction.equals(Direction.LEFT)) {
            moveLeft();
        }
    }
}
