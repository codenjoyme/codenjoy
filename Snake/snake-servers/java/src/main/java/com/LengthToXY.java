package com;

/**
 * User: oleksandr.baglai
 * Date: 10/2/12
 * Time: 12:07 AM
 */
public class LengthToXY {
    private int boardSize;

    public LengthToXY(int boardSize) {
        this.boardSize = boardSize;
    }

    public Point getXY(int length) {
        return new Point(length % boardSize, boardSize - 1 - length / boardSize);
    }

    public int getLength(int x, int y) {
        return (boardSize - 1 - y)*boardSize + x;
    }
}