package com.codenjoy.dojo.minesweeper.client.ai.logic;

import static com.codenjoy.dojo.minesweeper.client.ai.AISolver.FLAG_VALUE;

public class PlayField {

    private int width;
    private int height;
    private int amount;
    private int[][] field;

    public PlayField(int width, int height, int amount) {
        this.width = width;
        this.height = height;
        this.amount = amount;
        field = new int[width][height];
    }

    public PlayField(int[][] field, int minesLeft) {
        this(field.length, field[0].length, 0);
        this.field = field;
        amount = getAmount(field) + minesLeft;
    }

    private int getAmount(int[][] field) {
        int result = 0;
        for (int i = 0; i < field.length; ++i) {
            int[] arr = field[i];
            for (int j = 0; j < arr.length; ++j) {
                if (arr[j] == FLAG_VALUE) {
                    ++result;
                }
            }
        }

        return result;
    }

    public int get(int x, int y) {
        return field[x][y];
    }

    public int width() {
        return width;
    }

    public int height() {
        return height;
    }

    public int amount() {
        return amount;
    }
}
