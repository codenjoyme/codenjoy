package com.codenjoy.dojo.minesweeper.client.ai.logic;

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

    public PlayField(int[][] customField, int minesLeft) {
        this(customField.length, customField[0].length, 0);
        field = customField;
        amount = this.getAmount(customField) + minesLeft;
    }

    private int getAmount(int[][] customField) {
        int amount2 = 0;
        for (int i = 0; i < customField.length; ++i) {
            int[] ints = customField[i];
            for (int j = 0; j < ints.length; ++j) {
                int anInt = ints[j];
                if (anInt == 11) {
                    ++amount2;
                }
            }
        }

        return amount2;
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
