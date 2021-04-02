

package com.codenjoy.dojo.minesweeper.client.ai.logic;

import com.codenjoy.dojo.services.Dice;

public class PlayField {
    public static final int UNOPENED = 9;
    public static final int FLAG = 11;
    public static final int BOMB = 10;
    public static final int EXPLODE = 12;
    int width;
    int height;
    int amount;
    private int[][] field;
    private boolean[][] opened;
    private boolean[][] flags;
    private int minesLeft;
    private Dice dice;

    public PlayField(int width, int height, int amount, Dice dice) {
        this.width = width;
        this.height = height;
        this.amount = amount;
        this.dice = dice;
        this.field = new int[width][height];
        this.opened = new boolean[width][height];
        this.flags = new boolean[width][height];
    }

    public PlayField(int[][] customField, int minesLeft, Dice dice) {
        this(customField.length, customField[0].length, 0, dice);
        this.field = customField;
        this.amount = this.getAmount(customField) + minesLeft;
        this.minesLeft = minesLeft;
    }

    public boolean isValid() {
        if (this.minesLeft < 0) {
            return false;
        } else if (this.field.length <= 30 && this.field[0].length <= 30) {
            for(int i = 0; i < this.field.length; ++i) {
                if (this.field[i].length != this.field[0].length) {
                    return false;
                }

                for(int j = 0; j < this.field[i].length; ++j) {
                    int value = this.field[i][j];
                    if (value > 0 && value <= 8) {
                        int count = 0;
                        if (i > 0 && this.isBomb(i - 1, j)) {
                            ++count;
                        }

                        if (j > 0 && this.isBomb(i, j - 1)) {
                            ++count;
                        }

                        if (i < this.field.length - 1 && this.isBomb(i + 1, j)) {
                            ++count;
                        }

                        if (j < this.field[i].length - 1 && this.isBomb(i, j + 1)) {
                            ++count;
                        }

                        if (i > 0 && j > 0 && this.isBomb(i - 1, j - 1)) {
                            ++count;
                        }

                        if (j > 0 && i < this.field.length - 1 && this.isBomb(i + 1, j - 1)) {
                            ++count;
                        }

                        if (i < this.field.length - 1 && j < this.field[i].length - 1 && this.isBomb(i + 1, j + 1)) {
                            ++count;
                        }

                        if (i > 0 && j < this.field[i].length - 1 && this.isBomb(i - 1, j + 1)) {
                            ++count;
                        }

                        if (count > value) {
                            return false;
                        }
                    }
                }
            }

            return true;
        } else {
            return false;
        }
    }

    private int getAmount(int[][] customField) {
        int amount2 = 0;
        int[][] arr$ = customField;
        int len$ = customField.length;

        for(int i$ = 0; i$ < len$; ++i$) {
            int[] ints = arr$[i$];
            int[] arr$$ = ints;
            int len$$ = ints.length;

            for(int i$$ = 0; i$$ < len$$; ++i$$) {
                int anInt = arr$$[i$$];
                if (anInt == 11) {
                    ++amount2;
                }
            }
        }

        return amount2;
    }

    public void setRandomMines() {
        int left = this.amount;

        while(left > 0) {
            int x = dice.next(this.width);
            int y = dice.next(this.height);
            if (this.field[x][y] != 10) {
                this.field[x][y] = 10;
                --left;
            }
        }

        this.setDigits();
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    private boolean isBomb(int x, int y) {
        return this.field[x][y] == 10;
    }

    private void setDigits() {
        for(int x = 0; x < this.width; ++x) {
            for(int y = 0; y < this.height; ++y) {
                if (this.field[x][y] != 10) {
                    this.field[x][y] = 0;
                    int var10002;
                    if (x > 0 && this.isBomb(x - 1, y)) {
                        var10002 = this.field[x][y]++;
                    }

                    if (y > 0 && this.isBomb(x, y - 1)) {
                        var10002 = this.field[x][y]++;
                    }

                    if (x > 0 && y > 0 && this.isBomb(x - 1, y - 1)) {
                        var10002 = this.field[x][y]++;
                    }

                    if (x < this.width - 1 && this.isBomb(x + 1, y)) {
                        var10002 = this.field[x][y]++;
                    }

                    if (y < this.height - 1 && this.isBomb(x, y + 1)) {
                        var10002 = this.field[x][y]++;
                    }

                    if (x < this.width - 1 && y < this.height - 1 && this.isBomb(x + 1, y + 1)) {
                        var10002 = this.field[x][y]++;
                    }

                    if (x > 0 && y < this.height - 1 && this.isBomb(x - 1, y + 1)) {
                        var10002 = this.field[x][y]++;
                    }

                    if (x < this.width - 1 && y > 0 && this.isBomb(x + 1, y - 1)) {
                        var10002 = this.field[x][y]++;
                    }
                }
            }
        }

    }

    public void click(com.codenjoy.dojo.minesweeper.client.ai.logic.Pair<Integer, Integer> coord) {
        this.click(coord.getKey(), coord.getValue());
    }

    public void click(int x, int y) {
        if (!this.opened[x][y]) {
            this.opened[x][y] = true;
            if (this.field[x][y] == 10) {
                this.field[x][y] = 12;
            } else {
                if (this.field[x][y] == 0) {
                    if (x > 0) {
                        this.click(x - 1, y);
                    }

                    if (y > 0) {
                        this.click(x, y - 1);
                    }

                    if (x > 0 && y > 0) {
                        this.click(x - 1, y - 1);
                    }

                    if (x < this.width - 1) {
                        this.click(x + 1, y);
                    }

                    if (y < this.height - 1) {
                        this.click(x, y + 1);
                    }

                    if (x < this.width - 1 && y < this.height - 1) {
                        this.click(x + 1, y + 1);
                    }

                    if (x > 0 && y < this.height - 1) {
                        this.click(x - 1, y + 1);
                    }

                    if (x < this.width - 1 && y > 0) {
                        this.click(x + 1, y - 1);
                    }
                }

            }
        }
    }

    public void click2(Pair<Integer, Integer> coord) {
        this.click2(coord.getKey(), coord.getValue());
    }

    public void click2(int x, int y) {
        if (!this.opened[x][y]) {
            this.flags[x][y] = !this.flags[x][y];
            this.opened[x][y] = !this.opened[x][y];
        }

    }

    public int get(int x, int y) {
        return this.field[x][y];
    }

    public void print() {
        for(int y = 0; y < this.height; ++y) {
            for(int x = 0; x < this.width; ++x) {
                if (this.field[x][y] == 10) {
                    System.out.print("* ");
                } else if (this.field[x][y] == 0) {
                    System.out.print("  ");
                } else {
                    System.out.print(this.field[x][y] + " ");
                }
            }

            System.out.println();
        }

        System.out.println();
    }
}
