//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.codenjoy.dojo.minesweeper.client.ai.utils;

import com.codenjoy.dojo.minesweeper.client.ai.Element;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class BoardImpl {
    private String board;
    private com.codenjoy.dojo.minesweeper.client.ai.utils.LengthToXY xyl;
    private int size;

    public BoardImpl(String boardString) {
        this.board = boardString.replaceAll("\n", "");
        this.size = this.size();
        this.xyl = new LengthToXY(this.size);
    }

    public List<com.codenjoy.dojo.minesweeper.client.ai.utils.Point> get(Element... elements) {
        List<com.codenjoy.dojo.minesweeper.client.ai.utils.Point> result = new LinkedList();
        Element[] arr$ = elements;
        int len$ = elements.length;

        for(int i$ = 0; i$ < len$; ++i$) {
            Element e = arr$[i$];
            result.addAll(this.findAll(e));
        }

        return result;
    }

    public boolean isAt(int x, int y, Element element) {
        return com.codenjoy.dojo.minesweeper.client.ai.utils.Point.pt(x, y).isBad(this.size) ? false : this.getAt(x, y).equals(element);
    }

    public Element getAt(int x, int y) {
        return Element.valueOf(this.board.charAt(this.xyl.getLength(x, y)));
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

    public List<com.codenjoy.dojo.minesweeper.client.ai.utils.Point> getBarriers() {
        List<com.codenjoy.dojo.minesweeper.client.ai.utils.Point> all = this.getWalls();
        return this.removeDuplicates(all);
    }

    private List<com.codenjoy.dojo.minesweeper.client.ai.utils.Point> removeDuplicates(List<com.codenjoy.dojo.minesweeper.client.ai.utils.Point> all) {
        List<com.codenjoy.dojo.minesweeper.client.ai.utils.Point> result = new LinkedList();
        Iterator i$ = all.iterator();

        while(i$.hasNext()) {
            com.codenjoy.dojo.minesweeper.client.ai.utils.Point point = (com.codenjoy.dojo.minesweeper.client.ai.utils.Point)i$.next();
            if (!result.contains(point)) {
                result.add(point);
            }
        }

        return result;
    }

    public String toString() {
        return String.format("Board:\n%s\n", this.boardAsString());
    }

    private List<com.codenjoy.dojo.minesweeper.client.ai.utils.Point> findAll(Element element) {
        List<com.codenjoy.dojo.minesweeper.client.ai.utils.Point> result = new LinkedList();

        for(int i = 0; i < this.size * this.size; ++i) {
            com.codenjoy.dojo.minesweeper.client.ai.utils.Point pt = this.xyl.getXY(i);
            if (this.isAt(pt.getX(), pt.getY(), element)) {
                result.add(pt);
            }
        }

        return result;
    }

    public List<com.codenjoy.dojo.minesweeper.client.ai.utils.Point> getWalls() {
        return this.findAll(Element.BORDER);
    }

    public boolean isAt(int x, int y, Element... elements) {
        Element[] arr$ = elements;
        int len$ = elements.length;

        for(int i$ = 0; i$ < len$; ++i$) {
            Element c = arr$[i$];
            if (this.isAt(x, y, c)) {
                return true;
            }
        }

        return false;
    }

    public boolean isNear(int x, int y, Element element) {
        if (com.codenjoy.dojo.minesweeper.client.ai.utils.Point.pt(x, y).isBad(this.size)) {
            return false;
        } else {
            return this.isAt(x + 1, y, element) || this.isAt(x - 1, y, element) || this.isAt(x, y + 1, element) || this.isAt(x, y - 1, element);
        }
    }

    public boolean isBarrierAt(int x, int y) {
        return this.getBarriers().contains(com.codenjoy.dojo.minesweeper.client.ai.utils.Point.pt(x, y));
    }

    public int countNear(int x, int y, Element element) {
        if (com.codenjoy.dojo.minesweeper.client.ai.utils.Point.pt(x, y).isBad(this.size)) {
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

    public com.codenjoy.dojo.minesweeper.client.ai.utils.Point getMe() {
        return (Point)this.get(Element.DETECTOR).get(0);
    }

    public boolean isGameOver() {
        return !this.get(Element.BANG).isEmpty();
    }
}
