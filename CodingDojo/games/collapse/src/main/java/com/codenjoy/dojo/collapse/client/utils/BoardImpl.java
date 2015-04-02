package com.codenjoy.dojo.collapse.client.utils;

import com.codenjoy.dojo.collapse.client.Element;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static com.codenjoy.dojo.collapse.client.utils.Point.pt;

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
        Collections.sort(result);
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

    @Override
    public String toString() {
        StringBuffer result = new StringBuffer();
        for (int i = 0; i <= size - 1; i++) {
            result.append(board.substring(i * size, (i + 1) * size));
            result.append("\n");
        }
        return result.toString();
    }

    private List<Point> findAll(Element element) {
        List<Point> result = new LinkedList<Point>();
        for (int i = 0; i < size * size; i++) {
            Point pt = xyl.getXY(i);
            if (isAt(pt.getX(), pt.getY(), element)) {
                result.add(pt);
            }
        }
        return result;
    }
}