package com.epam.dojo.icancode.client;

import com.codenjoy.dojo.client.AbstractBoard;
import com.codenjoy.dojo.services.CharElements;
import com.codenjoy.dojo.services.Point;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

import static com.codenjoy.dojo.services.PointImpl.pt;

/**
 * Created by Mikhail_Udalyi on 20.09.2016.
 */
public abstract class AbstractBoard2<E extends CharElements> extends AbstractBoard<E> {
    protected int size;
    protected char[][][] field;

    public AbstractBoard forString(String boardString) {
        if (boardString.indexOf("layer") != -1) {
            JSONObject source = new JSONObject(boardString);
            JSONArray layers = source.getJSONArray("layers");

            return forString(layers.getString(0), layers.getString(1));
        } else {
            return forString(new String[]{boardString});
        }
    }

    public AbstractBoard forString(String... layers) {
        String board = layers[0].replaceAll("\n", "");
        size = (int) Math.sqrt(board.length());
        field = new char[layers.length][size][size];

        for (int i = 0; i < layers.length; ++i) {
            board = layers[i].replaceAll("\n", "");

            char[] temp = board.toCharArray();
            for (int y = 0; y < size; y++) {
                int dy = y * size;
                for (int x = 0; x < size; x++) {
                    field[i][x][y] = temp[dy + x];
                }
            }
        }

        return this;
    }

    public abstract E valueOf(char ch);

    public int size() {
        return size;
    }

    public static Set<Point> removeDuplicates(Collection<Point> all) {
        Set<Point> result = new TreeSet<Point>();
        for (Point point : all) {
            result.add(point);
        }
        return result;
    }

    public List<Point> get(E... elements) {
        return get(0, elements);
    }

    public List<Point> get(int numLayer, E... elements) {
        List<Point> result = new LinkedList<Point>();

        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                for (E element : elements) {
                    if (valueOf(field[numLayer][x][y]).equals(element)) {
                        result.add(pt(x, y));
                    }
                }
            }
        }

        return result;
    }

    public boolean isAt(int x, int y, E element) {
        return isAt(0, x, y, element);
    }

    public boolean isAt(int numLayer, int x, int y, E element) {
        if (pt(x, y).isOutOf(size)) {
            return false;
        }
        return getAt(numLayer, x, y).equals(element);
    }

    public E getAt(int x, int y) {
        return getAt(0, x, y);
    }

    public E getAt(int numLayer, int x, int y) {
        return valueOf(field[numLayer][x][y]);
    }

    public String boardAsString() {
        return boardAsString(0);
    }

    public String boardAsString(int numLayer) {
        StringBuffer result = new StringBuffer();
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                result.append(field[numLayer][x][y]);
            }
            result.append("\n");
        }
        return result.toString();
    }

    @Override
    public String toString() {
        return String.format("Board:\n%s\n\n%s",
                boardAsString(0), boardAsString(1));
    }

    public boolean isAt(int x, int y, E... elements) {
        return isAt(0, x, y, elements);
    }

    public boolean isAt(int numLayer, int x, int y, E... elements) {
        for (E c : elements) {
            if (isAt(numLayer, x, y, c)) {
                return true;
            }
        }
        return false;
    }

    public boolean isNear(int x, int y, E element) {
        return isNear(0, x, y, element);
    }

    public boolean isNear(int numLayer, int x, int y, E element) {
        if (pt(x, y).isOutOf(size)) {
            return false;
        }
        return isAt(numLayer, x + 1, y, element) || isAt(numLayer, x - 1, y, element) || isAt(numLayer, x, y + 1, element) || isAt(numLayer, x, y - 1, element);
    }

    public int countNear(int x, int y, E element) {
        return countNear(0, x, y, element);
    }

    public int countNear(int numLayer, int x, int y, E element) {
        if (pt(x, y).isOutOf(size)) {
            return 0;
        }
        int count = 0;
        if (isAt(numLayer, x - 1, y, element)) count++;
        if (isAt(numLayer, x + 1, y, element)) count++;
        if (isAt(numLayer, x, y - 1, element)) count++;
        if (isAt(numLayer, x, y + 1, element)) count++;
        return count;
    }

    public List<E> getNear(int x, int y) {
        return getNear(0, x, y);
    }

    public List<E> getNear(int numLayer, int x, int y) {
        List<E> result = new LinkedList<E>();

        int radius = 1;
        for (int dx = -radius; dx <= radius; dx++) {
            for (int dy = -radius; dy <= radius; dy++) {
                result.add(getAt(numLayer, x + dx, y + dy));
            }
        }

        return result;
    }

    public boolean isOutOfField(int x, int y) {
        return pt(x, y).isOutOf(size);
    }

    public void set(int x, int y, char ch) {
        set(0, x, y, ch);
    }

    public void set(int numLayer, int x, int y, char ch) {
        field[numLayer][x][y] = ch;
    }

    public char[][] getField() {
        return getField(0);
    }

    public char[][] getField(int numLayer) {
        return field[numLayer];
    }
}
