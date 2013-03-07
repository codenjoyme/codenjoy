package com.codenjoy.bomberman;

/**
 * User: oleksandr.baglai
 * Date: 3/7/13
 * Time: 9:11 AM
 */
public class Board {
    private int size;
    private MyBomberman bomberman;

    public Board(int size) {
        this.size = size;
    }

    public int getSize() {
        return size;
    }

    public Bomberman getBomberman() {
        bomberman = new MyBomberman(size);
        return bomberman;
    }

    public void tact() {
        bomberman.apply();
    }
}
