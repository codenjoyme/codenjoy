package com.codenjoy.bomberman.model;

/**
 * User: oleksandr.baglai
 * Date: 3/7/13
 * Time: 4:39 PM
 */
public class Blast {
    private final int x;
    private final int y;

    public Blast(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
