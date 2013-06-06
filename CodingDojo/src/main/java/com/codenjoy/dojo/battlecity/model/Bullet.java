package com.codenjoy.dojo.battlecity.model;

public class Bullet extends MovingObject{

    public Bullet(Direction tankDirection, int[] tankCoordinates) {
        super(tankCoordinates[0],tankCoordinates[1],tankDirection);
        this.movingSpeed = 2;
    }

    public void moveUp() {
        if(coordinateY > 0) {
            coordinateY-=movingSpeed;
        }
        direction = Direction.UP;
    }

    public void moveDown() {
        if(coordinateY + movingSpeed < Tanks.BATTLE_FIELD_SIZE) {
            coordinateY+=movingSpeed;
        }
        direction = Direction.DOWN;
    }

    public void moveRight() {
        if(coordinateX + movingSpeed < Tanks.BATTLE_FIELD_SIZE) {
            coordinateX+=movingSpeed;
        }
        direction = Direction.RIGHT;
    }

    public void moveLeft() {
        if(coordinateX > 0) {
            coordinateX-=movingSpeed;
        }
        direction =Direction.LEFT;
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
