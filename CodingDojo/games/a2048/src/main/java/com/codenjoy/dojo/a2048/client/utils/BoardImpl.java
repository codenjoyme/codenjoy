package com.codenjoy.dojo.a2048.client.utils;


import com.codenjoy.dojo.a2048.client.Direction;
import com.codenjoy.dojo.a2048.client.Element;

import java.util.LinkedList;
import java.util.List;

import static com.codenjoy.dojo.a2048.client.utils.Point.pt;

/**
 * User: oleksandr.baglai
 */
public class BoardImpl {
    private String board;
    private LengthToXY xyl;
    private int size;

    public BoardImpl(String boardString) {
        board = boardString.replaceAll("\n", "");
        size = size();
        xyl = new LengthToXY(size);
    }

    public List<Point> get(Element... elements) {
        List<Point> result = new LinkedList<Point>();
        for (Element e : elements) {
            result.addAll(findAll(e));
        }
        return result;
    }

    public boolean isAt(int x, int y, Element element) {
        if (pt(x, y).isBad(size)) {
            return false;
        }
        return getAt(x, y).equals(element);
    }

    public Element getAt(int x, int y) {
        return Element.valueOf(board.charAt(xyl.getLength(x, y)));
    }

    public int size() {
        return (int) Math.sqrt(board.length());
    }

    private String boardAsString() {
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

    private List<Point> findAll(Element element) {
        List<Point> result = new LinkedList<Point>();
        for (int i = 0; i < size*size; i++) {
            Point pt = xyl.getXY(i);
            if (isAt(pt.getX(), pt.getY(), element)) {
                result.add(pt);
            }
        }
        return result;
    }

    public boolean isAt(int x, int y, Element... elements) {
        for (Element c : elements) {
            if (isAt(x, y, c)) {
                return true;
            }
        }
        return false;
    }

    public boolean isNear(int x, int y, Element element) {
        if (pt(x, y).isBad(size)) {
            return false;
        }
        return isAt(x + 1, y, element) || isAt(x - 1, y, element) || isAt(x, y + 1, element) || isAt(x, y - 1, element);
    }

    public int countNear(int x, int y, Element element) {
        if (pt(x, y).isBad(size)) {
            return 0;
        }
        int count = 0;
        if (isAt(x - 1, y    , element)) count ++;
        if (isAt(x + 1, y    , element)) count ++;
        if (isAt(x    , y - 1, element)) count ++;
        if (isAt(x    , y + 1, element)) count ++;
        return count;
    }

    public int getSumCountFor(Direction direction) {
        int result = 0;

        for (int y = 0; y < size; y++) {
            int fromX = 0;
            int toX = 0;
            while (fromX < size && toX < size - 1) {
                toX++;

                Element at = getAt(fromX, y);
                Element at2 = getAt(toX, y);
                if (at == Element.NONE) {
                    fromX++;
                    continue;
                }
                if (at2 == Element.NONE) {
                    continue;
                }

                if (at != Element.NONE && at == at2) {
                    result++;
                    fromX = toX + 1;
                    toX = fromX;
                }
            }
        }
        return result;
    }
}