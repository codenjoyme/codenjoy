package com.codenjoy.dojo.minesweeper.client.ai.logic;

import com.codenjoy.dojo.minesweeper.model.Elements;
import com.codenjoy.dojo.services.LengthToXY;
import com.codenjoy.dojo.services.Point;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import static com.utils.Point.pt;

public class BoardImpl {

    private String board;
    private LengthToXY xyl;
    private int size;

    public BoardImpl(String boardString) {
        board = boardString.replaceAll("\n", "");
        size = size();
        xyl = new LengthToXY(size);
    }

    public List<Point> get(Elements... elements) {
        List<Point> result = new LinkedList();
        for (int i = 0; i < elements.length; ++i) {
            Elements e = elements[i];
            result.addAll(findAll(e));
        }

        return result;
    }

    public boolean isAt(int x, int y, Elements element) {
        return !pt(x, y).isBad(size) && getAt(x, y).equals(element);
    }

    public Elements getAt(int x, int y) {
        return Elements.valueOf(board.charAt(xyl.getLength(x, y)));
    }

    public int size() {
        return (int) Math.sqrt(board.length());
    }

    private List<Point> findAll(Elements element) {
        List<Point> result = new LinkedList();

        for (int i = 0; i < size * size; ++i) {
            Point pt = xyl.getXY(i);
            if (isAt(pt.getX(), pt.getY(), element)) {
                result.add(pt);
            }
        }

        return result;
    }

    public boolean isAt(int x, int y, Elements... elements) {
        Elements[] arr$ = elements;
        int len$ = elements.length;

        for (int i$ = 0; i$ < len$; ++i$) {
            Elements c = arr$[i$];
            if (isAt(x, y, c)) {
                return true;
            }
        }

        return false;
    }

    public Point getMe() {
        return get(Elements.DETECTOR).get(0);
    }

    public boolean isGameOver() {
        return !get(Elements.BANG).isEmpty();
    }
}
