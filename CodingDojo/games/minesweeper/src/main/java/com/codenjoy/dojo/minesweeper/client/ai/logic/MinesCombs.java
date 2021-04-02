package com.codenjoy.dojo.minesweeper.client.ai.logic;

import java.util.Arrays;

public class MinesCombs {

    private final int mines;
    private int combs;
    private Indefinite indefinite;

    public MinesCombs(int mines) {
        this.mines = mines;
    }

    public void setIndefinite(Indefinite indefinite) {
        this.indefinite = indefinite;
    }

    public void addComb(int value) {
        combs += value;
    }

    public int getMines() {
        return mines;
    }

    public void addArrayComb(int[] array) {
        indefinite.addArrayComb(array);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(toShortString());
        sb.append(" (");
        sb.append(Arrays.toString(indefinite.getMines()));
        sb.append(')');
        return sb.toString();
    }

    public String toShortString() {
        StringBuilder sb = new StringBuilder();
        sb.append(mines).append("->").append(combs);
        return sb.toString();
    }
}
