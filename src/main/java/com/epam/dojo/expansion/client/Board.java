package com.epam.dojo.expansion.client;

/*-
 * #%L
 * iCanCode - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 EPAM
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
import com.codenjoy.dojo.client.Direction;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.algs.DeikstraFindWay;
import com.epam.dojo.expansion.model.Elements;
import org.apache.commons.lang.StringUtils;

import java.util.List;

import static com.epam.dojo.expansion.model.Elements.*;
import static com.epam.dojo.expansion.model.Elements.Layers.*;

public class Board extends AbstractBoard<Elements> {

    @Override
    public Elements valueOf(char ch) {
        return Elements.valueOf(ch);
    }

    /**
     * @param x X coordinate.
     * @param y Y coordinate.
     * @return Is it possible to go through the cell with {x,y} coordinates.
     */
    public boolean isBarrierAt(int x, int y) {
        return !isAt(LAYER1, x, y, FLOOR, BASE1, BASE2, BASE3, BASE4, EXIT, GOLD, HOLE) ||
                !isAt(LAYER2, x, y, EMPTY, GOLD, MY_FORCE, FORCE1, FORCE2, FORCE3, FORCE4);
    }

    /**
     * @param x X coordinate.
     * @param y Y coordinate.
     * @return Is Hole on the way?
     */
    public boolean isHoleAt(int x, int y) {
        return isAt(LAYER1, x, y, HOLE);
    }

    /**
     * @return Returns position of your forces.
     */
    public List<Point> getMyForces() {
        return get(LAYER2, MY_FORCE);
    }

    /**
     * @return Returns list of coordinates for all visible enemy forces.
     */
    public List<Point> getEnemyForces() {
        return get(LAYER2,
                FORCE1,
                FORCE2,
                FORCE3,
                FORCE4);
    }

    /**
     * @return Returns list of coordinates for all visible Walls.
     */
    public List<Point> getWalls() {
        return get(LAYER1,
                ANGLE_IN_LEFT,
                WALL_FRONT,
                ANGLE_IN_RIGHT,
                WALL_RIGHT,
                ANGLE_BACK_RIGHT,
                WALL_BACK,
                ANGLE_BACK_LEFT,
                WALL_LEFT,
                WALL_BACK_ANGLE_LEFT,
                WALL_BACK_ANGLE_RIGHT,
                ANGLE_OUT_RIGHT,
                ANGLE_OUT_LEFT,
                SPACE);
    }

    /**
     * @return Returns list of coordinates for all visible Breaks.
     */
    public List<Point> getBreaks() {
        return get(LAYER2, BREAK);
    }

    /**
     * @return Returns list of coordinates for all visible Holes.
     */
    public List<Point> getHoles() {
        return get(LAYER1,
                HOLE);
    }

    /**
     * @return Returns list of coordinates for all visible Exit points.
     */
    public List<Point> getExits() {
        return get(LAYER1, EXIT);
    }

    /**
     * @return Returns list of coordinates for all visible Start points.
     */
    public List<Point> getBases() {
        return get(LAYER1, BASE1, BASE2, BASE3, BASE4);
    }

    /**
     * @return Returns list of coordinates for all visible Gold.
     */
    public List<Point> getGold() {
        return get(LAYER1, GOLD);
    }

    /**
     * @return Checks if your robot is alive.
     */
    public boolean isMeAlive() {
        return true;
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

        StringBuilder builder = new StringBuilder();
        String[] layer1 = boardAsString(LAYER1).split("\n");
        String[] layer2 = boardAsString(LAYER2).split("\n");

        String numbers = temp.substring(0, layer1.length);
        String space = StringUtils.leftPad("", layer1.length - 5);
        String firstPart = " Layer1 " + space + " Layer2\n  " + numbers + "   " + numbers + "";

        for (int i = 0; i < layer1.length; ++i) {
            builder.append((i < 10 ? " " : "") + i + layer1[i] + " " + (i < 10 ? " " : "") + i + maskOverlay(layer2[i], layer1[i]));

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

            if (i != layer1.length - 1) {
                builder.append("\n");
            }
        }

        return firstPart + "\n" + builder.toString();
    }

    private String listToString(List<? extends Object> list) {
        String result = list.toString();

        return result.substring(1, result.length() - 1);
    }

    private DeikstraFindWay.Possible possible() {
        return new DeikstraFindWay.Possible() {
            @Override
            public boolean possible(Point from, Direction where) {
                int x = from.getX();
                int y = from.getY();
                if (isBarrierAt(x, y)) return false;

                Point newPt = where.change(from);
                int nx = newPt.getX();
                int ny = newPt.getY();

                if (isOutOfField(nx, ny)) return false;

                if (isBarrierAt(nx, ny)) return false;

                return true;
            }

            @Override
            public boolean possible(Point atWay) {
                return true;
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
}