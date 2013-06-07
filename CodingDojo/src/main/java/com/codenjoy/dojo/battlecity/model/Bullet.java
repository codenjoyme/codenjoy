package com.codenjoy.dojo.battlecity.model;

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
            onDestroy.destroy();
        }
    }

    public void moveUp() {
        for (int i = 0; i < speed; i++) {
            if (destroyed) {    // TODO testme
                return;
            }
            if (y + 1 < field.getSize()) {
                direction = Direction.UP;
                y += 1;
                field.affect(this);
            } else {
                onDestroy();
            }
        }
    }

    public void moveDown() {
//        if (destroyed) {     // TODO testme
//            return;
//        }
        for (int i = 0; i < speed; i++) {
            if (y > 1) {
                direction = Direction.DOWN;
                y -= 1;
                field.affect(this);
            } else {
                onDestroy();
            }
        }
    }

    public void moveRight() {
//        if (destroyed) {    // TODO testme
//            return;
//        }
        for (int i = 0; i < speed; i++) {
            if (x + 1 < field.getSize()) {
                direction = Direction.RIGHT;
                x += 1;
                field.affect(this);
            } else {
                onDestroy();
            }
        }
    }

    public void moveLeft() {
//        if (destroyed) {     // TODO testme
//            return;
//        }
        for (int i = 0; i < speed; i++) {
            if (x > 1) {
                direction = Direction.LEFT;
                x -= 1;
                field.affect(this);
            } else {
                onDestroy();
            }
        }
    }

    public void move() {
        if (this.direction.equals(Direction.UP)) {
            moveUp();
        }
        if (this.direction.equals(Direction.DOWN)) {
            moveDown();
        }
        if (this.direction.equals(Direction.RIGHT)) {
            moveRight();
        }
        if (this.direction.equals(Direction.LEFT)) {
            moveLeft();
        }
    }
}
