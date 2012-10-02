package com;

import java.util.*;

/**
 * User: oleksandr.baglai
 * Date: 10/2/12
 * Time: 12:07 AM
 */
public class Board {
    public final static char APPLE = '@';
    public final static char STONE = 'X';
    public final static char BODY = '0';
    public final static char EMPTY = ' ';
    public final static char HEAD = '#';
    public final static char WALL = '*';

    private String board;
    private LengthToXY xyl;
    private int size;

    public Board(String boardString) {
        board = boardString;
        size = size();
        xyl = new LengthToXY(size);
    }

    public Point getApple() {
        return xyl.getXY(board.indexOf(APPLE));
    }

    public String getSnakeDirection() {
        Point head = getHead();
        Map<String, Integer> bodyAt = new HashMap<String, Integer>();

        if (head.y != 0) {
            if (isAt(head.x, head.y - 1, BODY)) {
                bodyAt.put(Direction.DOWN, xyl.getLength(head.x, head.y - 1));
            }
        }
        if (head.y != size - 1) {
            if (isAt(head.x, head.y + 1, BODY)) {
                bodyAt.put(Direction.UP, xyl.getLength(head.x, head.y + 1));
            }
        }
        if (head.x != 0) {
            if (isAt(head.x - 1, head.y, BODY)) {
                bodyAt.put(Direction.LEFT, xyl.getLength(head.x - 1, head.y));
            }
        }
        if (head.x != size - 1) {
            if (isAt(head.x + 1, head.y, BODY)) {
                bodyAt.put(Direction.RIGHT, xyl.getLength(head.x + 1, head.y));
            }
        }

        if (bodyAt.size() == 1) {
            return Direction.inverted(bodyAt.entrySet().iterator().next().getKey());
        }
        return "";
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
        return xyl.getXY(board.indexOf(HEAD));
    }

    public List<Point> getBarriers() {
        List<Point> result = getSnake();
        result.addAll(getStones());
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
}