package com.codenjoy.bomberman.model;

/**
 * User: oleksandr.baglai
 * Date: 3/8/13
 * Time: 8:22 PM
 */
public class DestroyWall extends Wall {
    public DestroyWall(int x, int y) {
        super(x, y);
    }

    @Override
    public Wall copy() {
        return new DestroyWall(this.x, this.y);
    }
}
