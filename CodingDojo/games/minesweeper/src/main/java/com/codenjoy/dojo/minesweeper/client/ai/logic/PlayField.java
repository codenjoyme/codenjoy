package com.codenjoy.dojo.minesweeper.client.ai.logic;

import static com.codenjoy.dojo.minesweeper.client.ai.AISolver.FLAG_VALUE;

public class PlayField {

    private int width;
    private int height;
    private int amount;
    private int[][] field;

    public PlayField(int[][] field) {
        width = field.length;
        height = field[0].length;
        this.field = field;
        amount = getAmount();
    }

    private int getAmount() {
        int result = 0;
        for (int x = 0; x < width; ++x) {
            int[] arr = field[x];
            for (int y = 0; y < height; ++y) {
                if (arr[y] == FLAG_VALUE) {
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
