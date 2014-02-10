package com.utils;

import com.Element;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static com.utils.Point.pt;

/**
 * User: oleksandr.baglai
 */
public class BoardImpl {
    private String board;
    private LengthToXY xyl;
    private int size;

    public BoardImpl(String boardString) {
        board = boardString.replaceAll("[\n☼]", "");
        size = size();
        xyl = new LengthToXY(size);
    }

    public List<Point> get(Element ... elements) {
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

    private String boardAsString() {
        StringBuffer result = new StringBuffer();

        result.append("☼☼☼☼☼☼☼☼☼☼☼☼☼\n");
        for (int i = 0; i <= size - 1; i++) {
            result.append('☼');
            String line = board.substring(i * size, (i + 1) * size);
            result.append(line.substring(0, 3)).append('☼');
            result.append(line.substring(3, 6)).append('☼');
            result.append(line.substring(6, 9)).append('☼');
            result.append("\n");
            if ((i + 1) % 3 == 0) {
                result.append("☼☼☼☼☼☼☼☼☼☼☼☼☼\n");
            }
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
}