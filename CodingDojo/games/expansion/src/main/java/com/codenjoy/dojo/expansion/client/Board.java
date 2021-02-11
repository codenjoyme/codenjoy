package com.codenjoy.dojo.expansion.client;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 Codenjoy
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */


import com.codenjoy.dojo.client.AbstractBoard;
import com.codenjoy.dojo.expansion.model.Elements;
import com.codenjoy.dojo.expansion.model.Forces;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.algs.DeikstraFindWay;
import com.codenjoy.dojo.utils.TestUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

import static com.codenjoy.dojo.services.PointImpl.pt;

public class Board extends AbstractBoard<Elements> {

    private final int COUNT_NUMBERS = 3;

    @Override
    public Elements valueOf(char ch) {
        return Elements.valueOf(ch);
    }

    @Override
    protected int inversionY(int y) {
        return size - 1 - y;
    }

    /**
     * @param x X coordinate.
     * @param y Y coordinate.
     * @return Is it possible to go through the cell with {x,y} coordinates.
     */
    public boolean isBarrierAt(int x, int y) {
        return !isAt(Elements.Layers.LAYER1, x, y, Elements.FLOOR, Elements.BASE1, Elements.BASE2, Elements.BASE3, Elements.BASE4, Elements.EXIT, Elements.GOLD, Elements.HOLE) ||
                !isAt(Elements.Layers.LAYER2, x, y, Elements.EMPTY, Elements.GOLD, Elements.FORCE1, Elements.FORCE2, Elements.FORCE3, Elements.FORCE4);
    }

    /**
     * @param x X coordinate.
     * @param y Y coordinate.
     * @return Is Hole on the way?
     */
    public boolean isHoleAt(int x, int y) {
        return isAt(Elements.Layers.LAYER1, x, y, Elements.HOLE);
    }

    /**
     * @return My forces color
     */
    public Elements getMyForcesColor() {
        int color = source.getInt("myColor");
        return Elements.getForce(color);
    }

    /**
     * @return My forces available
     */
    public int getForcesAvailable() {
        return source.getInt("available");
    }

    /**
     * @return number of tick on server - for debug
     */
    public int getTick() {
        return source.getInt("tick");
    }

    /*
     * @return true if you are in lobby wait for another users
     */
    public boolean isInLobby() {
        return source.getBoolean("inLobby");
    }

    /*
     * @return pt.x - current round tick, pt.y - total ticks per round
     */
    public Point getRound() {
        return pt(source.getInt("round"), source.getInt("rounds"));
    }

    /**
     * @return My base base position
     */
    public Point getMyBasePosition() {
        JSONObject pt = source.getJSONObject("myBase");
        return pt(pt.getInt("x"), pt.getInt("y"));
    }

    /**
     * @return Returns position of your forces.
     */
    public List<Forces> getMyForces() {
        List<Forces> forces = getAllForces();
        List<Forces> result = new LinkedList<>();
        Elements myColor = getMyForcesColor();
        for (Forces force : forces) {
            Point pt = force.getRegion();
            if (isAt(pt.getX(), pt.getY(), myColor)) {
                result.add(force);
            }
        }
        return result;
    }

    public List<Forces> getAllForces() {
        String map = getForcesString();
        int[][] array = getForcesArray(map);
        List<Forces> result = getForces(array);
        return result;
    }

    private String getForcesString() {
        return source.getString("forces");
    }

    @NotNull
    private List<Forces> getForces(int[][] array) {
        List<Forces> result = new LinkedList<>();
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                int count = array[x][y];
                if (count > 0) {
                    Forces force = new Forces(pt(x, y), count);
                    result.add(force);
                }
            }
        }
        return result;
    }

    private int[][] getForcesArray(String map) {
        int[][] result = new int[size][size];
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                int l = y * size + x;
                String sub = map.substring(l*COUNT_NUMBERS, (l + 1)*COUNT_NUMBERS);
                int count = parseCount(sub);

                int xx = inversionX(x);
                int yy = inversionY(y);

                result[xx][yy] = count;
            }
        }
        return result;
    }

    private int parseCount(String sub) {
        if (sub.equals("-=#")) {
            return 0;
        } else {
            return Integer.parseInt(sub, Character.MAX_RADIX);
        }
    }

    /**
     * @return Returns list of coordinates for all visible enemy forces.
     */
    public List<Forces> getEnemyForces() {
        List<Forces> forces = getAllForces();
        List<Forces> result = new LinkedList<>();
        Elements myColor = getMyForcesColor();
        for (Forces force : forces) {
            Point pt = force.getRegion();
            if (!isAt(pt.getX(), pt.getY(), myColor)) {
                result.add(force);
            }
        }
        return result;
    }

    /**
     * @return Returns list of coordinates for all visible Walls.
     */
    public List<Point> getWalls() {
        return get(Elements.Layers.LAYER1,
                Elements.ANGLE_IN_LEFT,
                Elements.WALL_FRONT,
                Elements.ANGLE_IN_RIGHT,
                Elements.WALL_RIGHT,
                Elements.ANGLE_BACK_RIGHT,
                Elements.WALL_BACK,
                Elements.ANGLE_BACK_LEFT,
                Elements.WALL_LEFT,
                Elements.WALL_BACK_ANGLE_LEFT,
                Elements.WALL_BACK_ANGLE_RIGHT,
                Elements.ANGLE_OUT_RIGHT,
                Elements.ANGLE_OUT_LEFT,
                Elements.SPACE);
    }

    /**
     * @return Returns list of coordinates for all visible Breaks.
     */
    public List<Point> getBreaks() {
        return get(Elements.Layers.LAYER1, Elements.BREAK);
    }

    /**
     * @return Returns list of coordinates for all visible Holes.
     */
    public List<Point> getHoles() {
        return get(Elements.Layers.LAYER1, Elements.HOLE);
    }

    /**
     * @return Returns list of coordinates for all visible Exit points.
     */
    public List<Point> getExits() {
        return get(Elements.Layers.LAYER1, Elements.EXIT);
    }

    /**
     * @return Returns list of coordinates for all visible Start points.
     */
    public List<Point> getBases() {
        return get(Elements.Layers.LAYER1, Elements.BASE1, Elements.BASE2, Elements.BASE3, Elements.BASE4);
    }

    /**
     * @return Returns list of coordinates for all visible Gold.
     */
    public List<Point> getGold() {
        return get(Elements.Layers.LAYER1, Elements.GOLD);
    }

    /**
     * @return Checks if your forces is alive.
     */
    public boolean isMeAlive() {
        return !get(getMyForcesColor()).isEmpty();
    }

    public List<Point> getFreeSpaces() {
        List<Point> empty = get(Elements.Layers.LAYER2, Elements.EMPTY);
        List<Point> floor = get(Elements.Layers.LAYER1, Elements.FLOOR);
        List<Point> result = new LinkedList<>();
        for (Point pt : floor) {
            for (Point pt2 : empty) {
                if (pt.equals(pt2)) {
                    result.add(pt);
                }
            }
        }
        return result;
    }

    public String maskOverlay(String source, String mask) {
        StringBuilder result = new StringBuilder(source);
        for (int i = 0; i < result.length(); ++i) {
            Elements el = Elements.valueOf(mask.charAt(i));
            if (Elements.isWall(el)) {
                result.setCharAt(i, el.ch());
            }
        }

        return result.toString();
    }

    @Override
    public String toString() {
        String temp = "0123456789012345678901234567890";
        String temp2 = "  0    1    2    3    4    5    6    7    8    9" +
                "    0    1    2    3    4    5    6    7    8    9" +
                "    0    1    2    3    4    5    6    7    8    9    0";

        StringBuilder builder = new StringBuilder();
        String[] layer1 = boardAsString(Elements.Layers.LAYER1).split("\n");
        String[] layer2 = boardAsString(Elements.Layers.LAYER2).split("\n");
        String[] layer3 = TestUtils.injectNN(getForcesString()).split("\n");

        int[][] array = getForcesArray(getForcesString());
        for (int y = 0; y < size; y++) {
            String line = "";
            for (int x = 0; x < size; x++) {
                String num = StringUtils.leftPad(Integer.toString(array[x][y]), COUNT_NUMBERS + 1, ' ');
                if (num.equals("   0")) num = "    ";
                line += num + '|';
            }
            layer3[size - 1 - y] = line;
        }

        int size = layer1.length;
        String numbers = temp.substring(0, size);
        String numbers2 = temp2.substring(0, size*(COUNT_NUMBERS + 2));
        String space = StringUtils.leftPad("", size - 5);
        String numbersLine = "  " + numbers + "   " + numbers +  "   " + numbers2;
        String firstPart = " Layer1 " + space + " Layer1 " + space + " Layer3\n" + numbersLine;

        for (int i = 0; i < size; ++i) {
            int ii = size - 1 - i;
            String index = (ii < 10 ? " " : "") + ii;
            builder.append(index + layer1[i] + " " + index + maskOverlay(layer2[i], layer1[i]) + " " + index + layer3[i]);

            switch (i) {
                case 0:
                    builder.append(" My Forces: " + listToString(getMyForces()));
                    break;
                case 1:
                    builder.append(" Enemy Forces: " + listToString(getEnemyForces()));
                    break;
                case 2:
                    builder.append(" Gold: " + listToString(getGold()));
                    break;
                case 3:
                    builder.append(" Bases: " + listToString(getBases()));
                    break;
                case 4:
                    builder.append(" Exits: " + listToString(getExits()));
                    break;
                case 5:
                    builder.append(" Breaks: " + listToString(getBreaks()));
                    break;
                case 6:
                    builder.append(" Holes: " + listToString(getHoles()));
                    break;
            }

            if (i != size - 1) {
                builder.append("\n");
            }
        }

        return firstPart + "\n" + builder.toString() + "\n" + numbersLine + " Tick#" + getTick();
    }

    private String listToString(List<? extends Object> list) {
        String result = list.toString();
        result = result.replace('"', '\'');
        return result.substring(1, result.length() - 1);
    }

    private DeikstraFindWay.Possible possible() {
        return new DeikstraFindWay.Possible() {
            @Override
            public boolean possible(Point point) {
                return !isBarrierAt(point.getX(), point.getY());
            }
        };
    }

    /**
     * @param to Destination point.
     * @return Shortest path (list of directions where to move) from any location to coordinates specified.
     */
    public List<Direction> getShortestWay(Point from, List<Point> to) {
        DeikstraFindWay.Possible map = possible();
        DeikstraFindWay findWay = new DeikstraFindWay();
        List<Direction> shortestWay = findWay.getShortestWay(size(), from, to, map);
        return shortestWay;
    }

    public Forces forceAt(Point point) {
        for (Forces force : getAllForces()) {
            if (force.getRegion().itsMe(point)) {
                return force;
            }
        }
        return null;
    }
}