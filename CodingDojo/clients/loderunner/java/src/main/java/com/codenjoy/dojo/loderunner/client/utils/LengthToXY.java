package com.codenjoy.dojo.loderunner.client.utils;

/**
 * User: oleksandr.baglai
 */
public class LengthToXY {
    private int boardSize;

    public LengthToXY(int boardSize) {
        this.boardSize = boardSize;
    }

    public Point getXY(int length) {
        if (length == -1) {
            return null;
        }
        return new Point(length % boardSize, length / boardSize);
    }

    public int getLength(int x, int y) {
        return (y)*boardSize + x;
    }
}