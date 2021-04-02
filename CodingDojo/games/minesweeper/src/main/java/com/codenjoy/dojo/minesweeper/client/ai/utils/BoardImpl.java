

package com.codenjoy.dojo.minesweeper.client.ai.utils;

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
        this.board = boardString.replaceAll("\n", "");
        this.size = this.size();
        this.xyl = new LengthToXY(this.size);
    }

    public List<Point> get(Elements... elements) {
        List<Point> result = new LinkedList();
        Elements[] arr$ = elements;
        int len$ = elements.length;

        for(int i$ = 0; i$ < len$; ++i$) {
            Elements e = arr$[i$];
            result.addAll(this.findAll(e));
        }

        return result;
    }

    public boolean isAt(int x, int y, Elements element) {
        return pt(x, y).isBad(this.size) ? false : this.getAt(x, y).equals(element);
    }

    public Elements getAt(int x, int y) {
        return Elements.valueOf(this.board.charAt(this.xyl.getLength(x, y)));
    }

    public int size() {
        return (int)Math.sqrt((double)this.board.length());
    }

    private String boardAsString() {
        StringBuffer result = new StringBuffer();

        for(int i = 0; i <= this.size - 1; ++i) {
            result.append(this.board.substring(i * this.size, (i + 1) * this.size));
            result.append("\n");
        }

        return result.toString();
    }

    public List<Point> getBarriers() {
        List<Point> all = this.getWalls();
        return this.removeDuplicates(all);
    }

    private List<Point> removeDuplicates(List<Point> all) {
        List<Point> result = new LinkedList();
        Iterator i$ = all.iterator();

        while(i$.hasNext()) {
            Point point = (Point)i$.next();
            if (!result.contains(point)) {
                result.add(point);
            }
        }

        return result;
    }

    public String toString() {
        return String.format("Board:\n%s\n", this.boardAsString());
    }

    private List<Point> findAll(Elements element) {
        List<Point> result = new LinkedList();

        for(int i = 0; i < this.size * this.size; ++i) {
            Point pt = this.xyl.getXY(i);
            if (this.isAt(pt.getX(), pt.getY(), element)) {
                result.add(pt);
            }
        }

        return result;
    }

    public List<Point> getWalls() {
        return this.findAll(Elements.BORDER);
    }

    public boolean isAt(int x, int y, Elements... elements) {
        Elements[] arr$ = elements;
        int len$ = elements.length;

        for(int i$ = 0; i$ < len$; ++i$) {
            Elements c = arr$[i$];
            if (this.isAt(x, y, c)) {
                return true;
            }
        }

        return false;
    }

    public boolean isNear(int x, int y, Elements element) {
        if (pt(x, y).isBad(this.size)) {
            return false;
        } else {
            return this.isAt(x + 1, y, element) || this.isAt(x - 1, y, element) || this.isAt(x, y + 1, element) || this.isAt(x, y - 1, element);
        }
    }

    public boolean isBarrierAt(int x, int y) {
        return this.getBarriers().contains(pt(x, y));
    }

    public int countNear(int x, int y, Elements element) {
        if (pt(x, y).isBad(this.size)) {
            return 0;
        } else {
            int count = 0;
            if (this.isAt(x - 1, y, element)) {
                ++count;
            }

            if (this.isAt(x + 1, y, element)) {
                ++count;
            }

            if (this.isAt(x, y - 1, element)) {
                ++count;
            }

            if (this.isAt(x, y + 1, element)) {
                ++count;
            }

            return count;
        }
    }

    public Point getMe() {
        return this.get(Elements.DETECTOR).get(0);
    }

    public boolean isGameOver() {
        return !this.get(Elements.BANG).isEmpty();
    }
}
