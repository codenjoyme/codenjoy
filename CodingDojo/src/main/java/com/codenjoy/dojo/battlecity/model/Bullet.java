package com.codenjoy.dojo.battlecity.model;

public class Bullet extends MovingObject {

    private Field field;

    public Bullet(Field field, Direction tankDirection, Point from) {
        super(from.getX(), from.getY(), tankDirection);
        this.field = field;
        this.speed = 2;
    }

    public void moveUp() {
        if (y + speed < field.getSize()) {
            y += speed;
        }
        direction = Direction.UP;
    }

    public void moveDown() {
        if (y > 0) {
            y -= speed;
        }
        direction = Direction.DOWN;
    }

    public void moveRight() {
        if (x + speed < field.getSize()) {
            x += speed;
        }
        direction = Direction.RIGHT;
    }

    public void moveLeft() {
        if (x > 0) {
            x -= speed;
        }
        direction = Direction.LEFT;
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
