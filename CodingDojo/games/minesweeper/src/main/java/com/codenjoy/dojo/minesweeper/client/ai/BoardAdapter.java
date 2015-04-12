package com.codenjoy.dojo.minesweeper.client.ai;

import com.Element;
import com.codenjoy.dojo.minesweeper.client.Board;
import com.codenjoy.dojo.minesweeper.model.Elements;
import com.utils.BoardImpl;
import com.utils.Point;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by indigo on 12.04.2015.
 */
public class BoardAdapter extends BoardImpl {
    private Board board;

    public BoardAdapter(Board board) {
        super("");
        this.board = board;
    }

    @Override
    public List<Point> get(Element... elements) {
        return pts(board.get(els(elements)));
    }

    @Override
    public boolean isAt(int x, int y, Element element) {
        return board.isAt(x, y, el(element));
    }

    @Override
    public Element getAt(int x, int y) {
        return el2(board.getAt(x, y));
    }

    @Override
    public int size() {
        if (board == null) {
            return 15;
        }
        return board.size();
    }

    @Override
    public List<Point> getBarriers() {
        return pts(board.get(Elements.BORDER));
    }

    @Override
    public String toString() {
        return board.toString();
    }

    @Override
    public List<Point> getWalls() {
        return pts(board.get(Elements.BORDER));
    }

    @Override
    public boolean isAt(int x, int y, Element... elements) {
        return board.isAt(x, y, els(elements));
    }

    @Override
    public boolean isNear(int x, int y, Element element) {
        return board.isNear(x, y, el(element));
    }

    @Override
    public boolean isBarrierAt(int x, int y) {
        return board.isBarrierAt(x, y);
    }

    @Override
    public int countNear(int x, int y, Element element) {
        return board.countNear(x, y, el(element));
    }

    private Elements el(Element element) {
        return Elements.values()[Arrays.asList(Element.values()).indexOf(element)];
    }

    private Element el2(Elements element) {
        return Element.values()[Arrays.asList(Elements.values()).indexOf(element)];
    }

    @Override
    public Point getMe() {
        return pt(board.getMe());
    }

    private List<Point> pts(List<com.codenjoy.dojo.services.Point> pts) {
        List<Point> result = new ArrayList<Point>();
        for (com.codenjoy.dojo.services.Point pt : pts) {
            result.add(pt(pt));
        }
        return result;
    }

    private Elements[] els(Element[] els) {
        List<Elements> result = new ArrayList<Elements>();
        for (Element el : els) {
            result.add(el(el));
        }
        return result.toArray(new Elements[0]);
    }

    private Point pt(com.codenjoy.dojo.services.Point me) {
        return new Point(me.getX(), me.getY());
    }

    @Override
    public boolean isGameOver() {
        return board.isGameOver();
    }
}
