package com.codenjoy.dojo.minesweeper.client.ai.logic;

public class PlayField {

    int width;
    int height;
    int amount;
    private int[][] field;

    public PlayField(int width, int height, int amount) {
        this.width = width;
        this.height = height;
        this.amount = amount;
        this.field = new int[width][height];
    }

    public PlayField(int[][] customField, int minesLeft) {
        this(customField.length, customField[0].length, 0);
        this.field = customField;
        this.amount = this.getAmount(customField) + minesLeft;
    }

    private int getAmount(int[][] customField) {
        int amount2 = 0;
        int[][] arr$ = customField;
        int len$ = customField.length;

        for (int i$ = 0; i$ < len$; ++i$) {
            int[] ints = arr$[i$];
            int[] arr$$ = ints;
            int len$$ = ints.length;

            for (int i$$ = 0; i$$ < len$$; ++i$$) {
                int anInt = arr$$[i$$];
                if (anInt == 11) {
                    ++amount2;
                }
            }
        }

        return amount2;
    }

    public int get(int x, int y) {
        return this.field[x][y];
    }

}
