//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.codenjoy.dojo.minesweeper.client.ai.utils;

public class LengthToXY {
    private int boardSize;

    public LengthToXY(int boardSize) {
        this.boardSize = boardSize;
    }

    public Point getXY(int length) {
        return length == -1 ? null : new Point(length % this.boardSize, length / this.boardSize);
    }

    public int getLength(int x, int y) {
        return y * this.boardSize + x;
    }
}
