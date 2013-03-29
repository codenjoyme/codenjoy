package com.javatrainee.tanks;

public class Tank extends MovingObject{
    private int size;
    private Bullet bullet = null;

    public Tank(int coordinateX, int coordinateY) {
        super(coordinateX, coordinateY, Direction.UP);
        this.movingSpeed = 1;
        this.size = 1;
    }

    public int getSize() {
        return size;
    }

    public void moveUp() {
        if(coordinateY > 0) {
            coordinateY-=movingSpeed;
        }
        direction = Direction.UP;
    }

    public void moveDown() {
        if(coordinateY + 1 < Tanks.BATTLE_FIELD_SIZE) {
            coordinateY+=movingSpeed;
        }
        direction = Direction.DOWN;
    }

    public void moveRight() {
        if(coordinateX + 1 < Tanks.BATTLE_FIELD_SIZE) {
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

    public void fire() {
        bullet = new Bullet(direction, getCoordinates());
    }

    public Bullet getBullet() {
        return bullet;
    }
}
