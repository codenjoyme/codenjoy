package com.codenjoy.dojo.client;

import com.codenjoy.dojo.services.Point;

import java.util.LinkedList;
import java.util.List;

import static com.codenjoy.dojo.services.PointImpl.pt;

public abstract class AbstractBoard<E> {
    protected String board;
    protected LengthToXY xyl;
    protected int size;

    public AbstractBoard forString(String boardString) {
        board = boardString.replaceAll("\n", "");
        size = size();
        xyl = new LengthToXY(size);
        return this;
    }

    public abstract E valueOf(char ch);

    public static List<Point> removeDuplicates(List<Point> all) {
        List<Point> result = new LinkedList<Point>();
        for (Point point : all) {
            if (!result.contains(point)) {
                result.add(point);
            }
        }
        return result;
    }

    public List<Point> get(E... elements) {
        List<Point> result = new LinkedList<Point>();
        for (E e : elements) {
            result.addAll(findAll(e));
        }
        return result;
    }

    public boolean isAt(int x, int y, E element) {
        if (pt(x, y).isOutOf(size)) {
            return false;
        }
        return getAt(x, y).equals(element);
    }

    public E getAt(int x, int y) {
        return valueOf(board.charAt(xyl.getLength(x, y)));
    }

    public int size() {
        return (int) Math.sqrt(board.length());
    }

    public String boardAsString() {
        StringBuffer result = new StringBuffer();
        for (int i = 0; i <= size - 1; i++) {
            result.append(board.substring(i * size, (i + 1) * size));
            result.append("\n");
        }
        return result.toString();
    }

    @Override
    public String toString() {
        return String.format("Board:\n%s\n",
                boardAsString());
    }

    public List<Point> findAll(E element) {
        List<Point> result = new LinkedList<Point>();
        for (int i = 0; i < size*size; i++) {
            Point pt = xyl.getXY(i);
            if (isAt(pt.getX(), pt.getY(), element)) {
                result.add(pt);
            }
        }
        return result;
    }

    public boolean isAt(int x, int y, E... elements) {
        for (E c : elements) {
            if (isAt(x, y, c)) {
                return true;
            }
        }
        return false;
    }

    public boolean isNear(int x, int y, E element) {
        if (pt(x, y).isOutOf(size)) {
            return false;
        }
        return isAt(x + 1, y, element) || isAt(x - 1, y, element) || isAt(x, y + 1, element) || isAt(x, y - 1, element);
    }

    public int countNear(int x, int y, E element) {
        if (pt(x, y).isOutOf(size)) {
            return 0;
        }
        int count = 0;
        if (isAt(x - 1, y    , element)) count ++;
        if (isAt(x + 1, y    , element)) count ++;
        if (isAt(x    , y - 1, element)) count ++;
        if (isAt(x    , y + 1, element)) count ++;
        return count;
    }

    public List<E> getNear(int x, int y) {
        List<E> result = new LinkedList<E>();

        int radius = 1;
        for (int dx = -radius; dx <= radius; dx++) {
            for (int dy = -radius; dy <= radius; dy++) {
                result.add(getAt(x + dx, y + dy));
            }
        }

        return result;
    }

    public boolean isOutOfField(int x, int y) {
        return pt(x, y).isOutOf(size);
    }
}