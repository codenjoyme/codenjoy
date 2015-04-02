package com.codenjoy.dojo.snake.client;

import java.util.*;

/**
 * User: oleksandr.baglai
 * Date: 10/2/12
 * Time: 12:07 AM
 */
public class Board {
    public final static char APPLE = '☺';
    public final static char STONE = '☻';
    public final static char BODY = '○';
    public static final char HEAD_LEFT = '◄';
    public static final char HEAD_RIGHT = '►';
    public static final char HEAD_UP = '▲';
    public static final char HEAD_DOWN = '▼';
    public final static char WALL = '☼';

    private String board;
    private LengthToXY xyl;
    private int size;

    public Board(String boardString) {
        board = boardString.replaceAll("\n", "").replaceAll("[\\╙\\╘\\╓\\╕\\═\\║\\╗\\╝\\╔\\╚]", "" + BODY);
        size = size();
        xyl = new LengthToXY(size);
    }

    public Point getApple() {
        return xyl.getXY(board.indexOf(APPLE));
    }

    public String getSnakeDirection() {
        Point head = getHead();
        if (isAt(head.x, head.y, HEAD_LEFT)) {
            return Direction.LEFT;
        } else if (isAt(head.x, head.y, HEAD_RIGHT)) {
            return Direction.RIGHT;
        } else if (isAt(head.x, head.y, HEAD_UP)) {
            return Direction.UP;
        } else {
            return Direction.DOWN;
        }
    }

    public boolean isAt(int x, int y, char type) {
        return board.charAt(xyl.getLength(x, y)) == type;
    }

    public int size() {
        return (int) Math.sqrt(board.length());
    }

    public String fix() {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i <= size - 1; i++) {
            buffer.append(board.substring(i*size, (i + 1)*size));
            buffer.append("\n");
        }
        return buffer.toString();
    }

    public Point getHead() {
        Point result = xyl.getXY(board.indexOf(HEAD_UP));
        if (result == null) {
            result = xyl.getXY(board.indexOf(HEAD_DOWN));
        }
        if (result == null) {
            result = xyl.getXY(board.indexOf(HEAD_LEFT));
        }
        if (result == null) {
            result = xyl.getXY(board.indexOf(HEAD_RIGHT));
        }
        return result;
    }

    public List<Point> getBarriers() {
        List<Point> result = getSnake();
        result.addAll(getStones());
        result.addAll(getWalls());
        return result;
    }

    public List<Point> getSnake() {
        List<Point> result = findAll(BODY);
        result.add(0, getHead());
        return result;
    }

    @Override
    public String toString() {
        return String.format("Board:\n%s\n" +
            "Apple at: %s\n" +
            "Stones at: %s\n" +
            "Head at: %s\n" +
            "Snake at: %s\n" +
            "Current direction: %s",
                fix(), getApple(), getStones(), getHead(), getSnake(), getSnakeDirection());
    }

    public List<Point> getStones() {
        return findAll(STONE);
    }

    private List<Point> findAll(char element) {
        List<Point> result = new LinkedList<Point>();
        for (int i = 0; i < size*size; i++) {
            Point pt = xyl.getXY(i);
            if (isAt(pt.x, pt.y, element)) {
                result.add(pt);
            }
        }
        return result;
    }

    public List<Point> getWalls() {
        return findAll(WALL);
    }
}