package com.codenjoy.dojo.snake.client;

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
        if (length == -1) {
            return null;
        }
        return new Point(length % boardSize, boardSize - 1 - length / boardSize);
    }

    public int getLength(int x, int y) {
        return (boardSize - 1 - y)*boardSize + x; // TODO привести змейку к общим координатам - 0,0 - левый верхний угол
    }
}