package com;

public class Direction {

    public static final String UP = "UP";
    public static final String DOWN = "DOWN";
    public static final String LEFT = "LEFT";
    public static final String RIGHT = "RIGHT";
    public static final String BOMB = "ACT";
    private Board board;

    public Direction(Board board) {
        this.board = board;
    }

    public String get() {
        return BOMB;
    }
}
