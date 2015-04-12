package com.codenjoy.dojo.client;

import com.codenjoy.dojo.services.CharElements;
import com.codenjoy.dojo.services.Point;

import java.util.*;

import static com.codenjoy.dojo.services.PointImpl.pt;

/**
 * Класс, который позволяет представлять строку в виде объекта Board для удобной работы.
 * Требует доработки клиентом, потому абстрактный.
 * @param <E> Elements - enum с перечнем всех элементов, которые могут присутствовать на поле.
 */
public abstract class AbstractBoard<E extends CharElements> {
    protected int size;
    protected char[][] field;

    public AbstractBoard forString(String boardString) {
        String board = boardString.replaceAll("\n", "");
        size = (int) Math.sqrt(board.length());

        char[] temp = board.toCharArray();
        field = new char[size][size];
        for (int y = 0; y < size; y++) {
            int dy = y*size;
            for (int x = 0; x < size; x++) {
                 field[x][y] = temp[dy + x];
            }
        }
        return this;
    }

    public abstract E valueOf(char ch);

    public int size() {
        return size;
    }

    // TODO подумать над этим, а то оно так долго все делается
    public static Set<Point> removeDuplicates(Collection<Point> all) {
        Set<Point> result = new TreeSet<Point>();
        for (Point point : all) {
            result.add(point);
        }
        return result;
    }

    public List<Point> get(E... elements) {
        List<Point> result = new LinkedList<Point>();
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                for (E element : elements) {
                    if (valueOf(field[x][y]).equals(element)) {
                        result.add(pt(x, y));
                    }
                }
            }
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
        return valueOf(field[x][y]);
    }

    public String boardAsString() {
        StringBuffer result = new StringBuffer();
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                result.append(field[x][y]);
            }
            result.append("\n");
        }
        return result.toString();
    }

    @Override
    public String toString() {
        return String.format("Board:\n%s",
                boardAsString());
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

    public void set(int x, int y, char ch) {
        field[x][y] = ch;
    }
}