package com.codenjoy.bomberman;

/**
 * User: oleksandr.baglai
 * Date: 3/7/13
 * Time: 1:37 PM
 */
public class Bomb {
    private final int x;
    private final int y;

    public Bomb(int x, int y) {
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
