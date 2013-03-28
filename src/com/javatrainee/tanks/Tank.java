package com.javatrainee.tanks;

public class Tank {
    private int size;
    private int coordinateX = 0;
    private int coordinateY = 0;
    private Direction direction;
    private Bullet bullet = null;

    public Tank(int coordinateX, int coordinateY) {
        this.size = 1;
        this.coordinateX = coordinateX;
        this.coordinateY = coordinateY;
        direction = Direction.UP;
    }

    public int getSize() {
        return size;
    }

    public int[] getCoordinates() {
        return new int[] { coordinateX, coordinateY};
    }

    public void moveUp() {
        if(coordinateY > 0) {
            coordinateY--;
        }
        direction = Direction.UP;
    }

    public void moveDown() {
        if(coordinateY + 1 < Tanks.BATTLE_FIELD_SIZE) {
            coordinateY++;
        }
        direction = Direction.DOWN;
    }

    public void moveRight() {
        if(coordinateX + 1 < Tanks.BATTLE_FIELD_SIZE) {
            coordinateX++;
        }
        direction = Direction.RIGHT;
    }

    public void moveLeft() {
        if(coordinateX > 0) {
            coordinateX--;
        }
        direction =Direction.LEFT;
    }

    public Direction getDirection() {
        return direction;
    }

    public void fire() {
        bullet = new Bullet(direction, getCoordinates());
    }

    public Bullet getBullet() {
        return bullet;
    }
}
