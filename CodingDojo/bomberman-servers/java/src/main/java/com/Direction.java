package com;

public class Direction {

    public static final String UP = "UP";
    public static final String DOWN = "DOWN";
    public static final String LEFT = "LEFT";
    public static final String RIGHT = "RIGHT";
    public static final String BOMB = "ACT";
    public static final String STOP = "";

    private Board board;

    public Direction(Board board) {
        this.board = board;
    }

    public String get() {
        Point bomberman = board.getBomberman();
        if (board.getFutureBlasts().contains(bomberman)) {
            return LEFT;
        }
        if (board.isAt(bomberman.x + 1, bomberman.y, Board.SPACE)) {
            return RIGHT;
        }
        if (board.isAt(bomberman.x + 1, bomberman.y, Board.DESTROY_WALL)) {
            return BOMB;
        }

        return BOMB;
    }
}
