package com.codenjoy.dojo.minesweeper.client.ai.vaa25.logic;

import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: Vlasov Alexander
 * Date: 03.08.2014
 * Time: 10:55
 * To change this template use File | Settings | File Templates.
 * 0..8 - value
 * 9 - unopened
 * 10 - bomb
 * 11 - flag
 * 12 - explode
 *
 * @author Alexander Vlasov
 */
public class PlayField {
    public static final int UNOPENED = 9;
    public static final int FLAG = 11;
    public static final int BOMB = 10;
    public static final int EXPLODE = 12;
    int width, height, amount;
    private int[][] field;    //0..8 - value , 10 - bomb
    private boolean[][] opened;  //false = unopened
    private boolean[][] flags;// false = not flagged
    private int minesLeft;

    public PlayField(int width, int height, int amount) {
        this.width = width;
        this.height = height;
        this.amount = amount;
        field = new int[width][height];
        opened = new boolean[width][height];
        flags = new boolean[width][height];
    }

    public PlayField(int[][] customField, int minesLeft) {
        this(customField.length, customField[0].length, 0);
        field = customField;
        amount = getAmount(customField) + minesLeft;
        this.minesLeft = minesLeft;
    }

    public boolean isValid() {
        if (minesLeft < 0) return false;
        if (field.length > 30 || field[0].length > 30) return false;
        for (int i = 0; i < field.length; i++) {
            if (field[i].length != field[0].length) return false;
            for (int j = 0; j < field[i].length; j++) {
                int value = field[i][j];
                if (value > 0 && value <= 8) {
                    int count = 0;
                    if (i > 0 && isBomb(i - 1, j)) count++;
                    if (j > 0 && isBomb(i, j - 1)) count++;
                    if (i < field.length - 1 && isBomb(i + 1, j)) count++;
                    if (j < field[i].length - 1 && isBomb(i, j + 1)) count++;

                    if (i > 0 && j > 0 && isBomb(i - 1, j - 1)) count++;
                    if (j > 0 && i < field.length - 1 && isBomb(i + 1, j - 1)) count++;
                    if (i < field.length - 1 && j < field[i].length - 1 && isBomb(i + 1, j + 1)) count++;
                    if (i > 0 && j < field[i].length - 1 && isBomb(i - 1, j + 1)) count++;

                    if (count > value) return false;
                }
            }
        }
        return true;
    }

    private int getAmount(int[][] customField) {
        int amount2 = 0;
        for (int[] ints : customField) {
            for (int anInt : ints) {
                if (anInt == FLAG) {
                    amount2++;
                }
            }
        }
        return amount2;
    }

    public void setRandomMines() {
        int left = amount;
        while (left > 0) {
            Random random = new Random();
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            if (field[x][y] != BOMB) {
                field[x][y] = BOMB;
                left--;
            }
        }
        setDigits();
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    private boolean isBomb(int x, int y) {
        return field[x][y] == BOMB;
    }

    private void setDigits() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (field[x][y] != BOMB) {
                    field[x][y] = 0;
                    if (x > 0) if (isBomb(x - 1, y)) field[x][y]++;
                    if (y > 0) if (isBomb(x, y - 1)) field[x][y]++;
                    if (x > 0 && y > 0) if (isBomb(x - 1, y - 1)) field[x][y]++;
                    if (x < width - 1) if (isBomb(x + 1, y)) field[x][y]++;
                    if (y < height - 1) if (isBomb(x, y + 1)) field[x][y]++;
                    if (x < width - 1 && y < height - 1) if (isBomb(x + 1, y + 1)) field[x][y]++;
                    if (x > 0 && y < height - 1) if (isBomb(x - 1, y + 1)) field[x][y]++;
                    if (x < width - 1 && y > 0) if (isBomb(x + 1, y - 1)) field[x][y]++;
                }
            }
        }
    }

    public void click(Pair<Integer, Integer> coord) {
        click(coord.getKey(), coord.getValue());
    }

    public void click(int x, int y) {
        if (opened[x][y]) return;
        opened[x][y] = true;
        if (field[x][y] == BOMB) {
            field[x][y] = EXPLODE;
            return;
        }
        if (field[x][y] == 0) {
            if (x > 0) click(x - 1, y);
            if (y > 0) click(x, y - 1);
            if (x > 0 && y > 0) click(x - 1, y - 1);
            if (x < width - 1) click(x + 1, y);
            if (y < height - 1) click(x, y + 1);
            if (x < width - 1 && y < height - 1) click(x + 1, y + 1);
            if (x > 0 && y < height - 1) click(x - 1, y + 1);
            if (x < width - 1 && y > 0) click(x + 1, y - 1);
        }
    }

    public void click2(Pair<Integer, Integer> coord) {
        click2(coord.getKey(), coord.getValue());
    }

    public void click2(int x, int y) {
        if (!opened[x][y]) {
            flags[x][y] = !flags[x][y];
            opened[x][y] = !opened[x][y];
        }
    }

    public int get(int x, int y) {
        return field[x][y];

    }

    public void print() {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (field[x][y] == BOMB) System.out.print("* ");
                else if (field[x][y] == 0) System.out.print("  ");
                else System.out.print(field[x][y] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }
}
