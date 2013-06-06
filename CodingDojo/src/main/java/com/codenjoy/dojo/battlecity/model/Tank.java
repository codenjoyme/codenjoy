package com.codenjoy.dojo.battlecity.model;

import com.codenjoy.dojo.services.Joystick;

public class Tank extends MovingObject implements Joystick {

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

    @Override
    public void up() {
        if(coordinateY > 0) {
            coordinateY-=movingSpeed;
        }
        direction = Direction.UP;
    }

    @Override
    public void down() {
        if(coordinateY + movingSpeed < Tanks.BATTLE_FIELD_SIZE) {
            coordinateY+=movingSpeed;
        }
        direction = Direction.DOWN;
    }

    @Override
    public void right() {
        if(coordinateX + movingSpeed < Tanks.BATTLE_FIELD_SIZE) {
            coordinateX+=movingSpeed;
        }
        direction = Direction.RIGHT;
    }

    @Override
    public void left() {
        if(coordinateX > 0) {
            coordinateX-=movingSpeed;
        }
        direction =Direction.LEFT;
    }

    @Override
    public void act() {
        bullet = new Bullet(direction, getCoordinates());
    }

    public Bullet getBullet() {
        return bullet;
    }
}
