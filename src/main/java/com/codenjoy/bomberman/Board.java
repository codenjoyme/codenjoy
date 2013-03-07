package com.codenjoy.bomberman;

import java.util.LinkedList;
import java.util.List;

/**
 * User: oleksandr.baglai
 * Date: 3/7/13
 * Time: 9:11 AM
 */
public class Board {
    private int size;
    private MyBomberman bomberman;
    private List<Bomb> bombs;

    public Board(int size) {
        this.size = size;
        bombs = new LinkedList<Bomb>();
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
        return bombs; // TODO return clone
    }

    public void drop(Bomb bomb) {
        bombs.add(bomb);
    }
}
