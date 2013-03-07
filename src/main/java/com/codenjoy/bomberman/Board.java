package com.codenjoy.bomberman;

import java.util.Arrays;
import java.util.List;

/**
 * User: oleksandr.baglai
 * Date: 3/7/13
 * Time: 9:11 AM
 */
public class Board {
    private int size;
    private MyBomberman bomberman;
    private Bomb bomb;

    public Board(int size) {
        this.size = size;
    }

    public int size() {
        return size;
    }

    public Bomberman getBomberman() {
        bomberman = new MyBomberman(this);
        return bomberman;
    }

    public void tact() {
        bomberman.apply();
    }

    public List<Bomb> getBombs() {
        return Arrays.asList(bomb);
    }

    public void drop(Bomb bomb) {
        this.bomb = bomb;
    }
}
