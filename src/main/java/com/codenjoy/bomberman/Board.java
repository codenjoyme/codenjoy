package com.codenjoy.bomberman;

/**
 * User: oleksandr.baglai
 * Date: 3/7/13
 * Time: 9:11 AM
 */
public class Board {
    private int size;

    public Board(int size) {
        this.size = size;
    }

    public int getSize() {
        return size;
    }

    public Bomberman getBomberman() {
        return new Bomberman() {
            @Override
            public int getX() {
                return 0;
            }

            @Override
            public int getY() {
                return 0;
            }
        };
    }
}
