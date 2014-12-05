package com.codenjoy.dojo.loderunner.client.utils;

import com.codenjoy.dojo.loderunner.client.Element;

import java.util.LinkedList;
import java.util.List;

import static com.codenjoy.dojo.loderunner.client.utils.Point.pt;

/**
 * User: oleksandr.baglai
 */
public class Board {
    private String board;
    private LengthToXY xyl;
    private int size;

    public Board(String boardString) {
        board = boardString.replaceAll("\n", "");
        size = size();
        xyl = new LengthToXY(size);
    }

    public List<Point> get(Element ... elements) {
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

    public List<Point> getBarriers() {
        List<Point> all = getWalls();
        return removeDuplicates(all);
    }

    private List<Point> removeDuplicates(List<Point> all) {
        List<Point> result = new LinkedList<Point>();
        for (Point point : all) {
            if (!result.contains(point)) {
                result.add(point);
            }
        }
        return result;
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

    public List<Point> getWalls() {
        return get(Element.BRICK, Element.UNDESTROYABLE_WALL);
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

    public boolean isBarrierAt(int x, int y) {
        return getBarriers().contains(pt(x, y));
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

    public Point getMe() {
        return get(Element.HERO_DIE,
                Element.HERO_DRILL_LEFT,
                Element.HERO_DRILL_RIGHT,
                Element.HERO_FALL_RIGHT,
                Element.HERO_FALL_LEFT,
                Element.HERO_LADDER,
                Element.HERO_LEFT,
                Element.HERO_RIGHT,
                Element.HERO_PIPE_LEFT,
                Element.HERO_PIPE_RIGHT).get(0);
    }

    public boolean isGameOver() {
        return !get(Element.HERO_DIE).isEmpty();
    }

    public boolean isEnemyAt(int x, int y) {
        return isAt(x, y, Element.ENEMY_LADDER) || isAt(x, y, Element.ENEMY_LEFT) || isAt(x, y, Element.ENEMY_PIPE_LEFT) ||
                isAt(x, y, Element.ENEMY_PIPE_RIGHT) || isAt(x, y, Element.ENEMY_RIGHT) || isAt(x, y, Element.ENEMY_PIT);
    }


    public boolean isOtherHeroAt(int x, int y) {
        return isAt(x, y, Element.OTHER_HERO_LEFT) || isAt(x, y, Element.OTHER_HERO_RIGHT) || isAt(x, y, Element.OTHER_HERO_LADDER) ||
                isAt(x, y, Element.OTHER_HERO_PIPE_LEFT) || isAt(x, y, Element.OTHER_HERO_PIPE_RIGHT);
    }
}