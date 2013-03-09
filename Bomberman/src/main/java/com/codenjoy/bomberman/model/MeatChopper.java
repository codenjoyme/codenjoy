package com.codenjoy.bomberman.model;

/**
 * User: oleksandr.baglai
 * Date: 3/8/13
 * Time: 8:22 PM
 */
public class MeatChopper extends Wall {

    private Direction direction;

    public MeatChopper(int x, int y) {
        super(x, y);
    }

    @Override
    public Wall copy() {
        return new MeatChopper(this.x, this.y);
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }
}
