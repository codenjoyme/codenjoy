package com.codenjoy.dojo.minesweeper.client.ai.logic;

public class MinesCombs {

    private int mines;
    private Indefinite indefinite;

    public MinesCombs(int mines) {
        this.mines = mines;
    }

    public void setIndefinite(Indefinite indefinite) {
        this.indefinite = indefinite;
    }

    public int getMines() {
        return mines;
    }

    public void addArrayComb(int[] array) {
        indefinite.addArrayComb(array);
    }

}
