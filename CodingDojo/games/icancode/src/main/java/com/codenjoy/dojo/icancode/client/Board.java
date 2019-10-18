package com.codenjoy.dojo.icancode.client;

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
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.icancode.model.Elements;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

import static com.codenjoy.dojo.icancode.model.Elements.*;
import static com.codenjoy.dojo.icancode.model.Elements.Layers.*;
import static com.codenjoy.dojo.services.PointImpl.pt;

public class Board extends AbstractBoard<Elements> {

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
        return !isAt(LAYER1, x, y, FLOOR, START, EXIT, GOLD, HOLE) ||
                !isAt(LAYER2, x, y, EMPTY, GOLD,
                        LASER_DOWN, LASER_UP, LASER_LEFT, LASER_RIGHT,
                        ROBO_OTHER, ROBO_OTHER_FLYING, ROBO_OTHER_FALLING, ROBO_OTHER_LASER,
                        ROBO, ROBO_FLYING, ROBO_FALLING, ROBO_LASER);
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
     * @return Returns position of your robot.
     */
    public Point getMe() {
        List<Point> points = new LinkedList<Point>() {{
            addAll(Board.this.get(LAYER2,
                    ROBO_FALLING,
                    ROBO_LASER,
                    ROBO));
            addAll(Board.this.get(LAYER3,
                    ROBO_FLYING));
        }};

        if (points.isEmpty()) {
            return null;
        }
        return points.get(0);
    }

    /**
     * @return Returns list of coordinates for all visible enemy Robots.
     */
    public List<Point> getOtherHeroes() {
        return new LinkedList<Point>(){{
            addAll(Board.this.get(LAYER2,
                    ROBO_OTHER_FALLING,
                    ROBO_OTHER_LASER,
                    ROBO_OTHER));
            addAll(Board.this.get(LAYER2,
                    ROBO_OTHER_FLYING));
        }};
    }

    /**
     * @return Returns list of coordinates for all visible LaserMachines.
     */
    public List<Point> getLaserMachines() {
        return get(LAYER1,
                LASER_MACHINE_CHARGING_LEFT,
                LASER_MACHINE_CHARGING_RIGHT,
                LASER_MACHINE_CHARGING_UP,
                LASER_MACHINE_CHARGING_DOWN,

                LASER_MACHINE_READY_LEFT,
                LASER_MACHINE_READY_RIGHT,
                LASER_MACHINE_READY_UP,
                LASER_MACHINE_READY_DOWN);
    }

    /**
     * @return Returns list of coordinates for all visible Lasers.
     */
    public List<Point> getLasers() {
        return get(LAYER2,
                LASER_LEFT,
                LASER_RIGHT,
                LASER_UP,
                LASER_DOWN);
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
     * @return Returns list of coordinates for all visible Boxes.
     */
    public List<Point> getBoxes() {
        return get(LAYER2,
                BOX);
    }

    /**
     * @return Returns list of coordinates for all visible Holes.
     */
    public List<Point> getHoles() {
        return get(LAYER1,
                HOLE,
                ROBO_FALLING,
                ROBO_OTHER_FALLING);
    }

//    /**
//     * TODO
//     * @param from
//     * @param direction
//     * @return
//     */
//    public Elements inFrontOf(Point from, Direction direction) {
//        Point change = direction.change(from);
//        return getAt(change.getX(), change.getY());
//    }

    /**
     * @return Returns list of coordinates for all visible Exit points.
     */
    public List<Point> getExits() {
        return get(LAYER1, EXIT);
    }

    /**
     * @return Returns list of coordinates for all visible Start points.
     */
    public List<Point> getStarts() {
        return get(LAYER1, START);
    }

    /**
     * @return Returns list of coordinates for all visible Gold.
     */
    public List<Point> getGold() {
        return get(LAYER1, GOLD);
    }

    /**
     * @return Returns list of coordinates for all visible Zombies (even die).
     */
    public List<Point> getZombies() {
        return get(LAYER2,
                FEMALE_ZOMBIE,
                MALE_ZOMBIE,
                ZOMBIE_DIE);
    }

    /**
     * @return Checks if your robot is alive.
     */
    public boolean isMeAlive() {
        return get(LAYER2, ROBO_FALLING, ROBO_LASER).isEmpty();
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
        String[] layer3 = boardAsString(LAYER3).split("\n");

        String numbers = temp.substring(0, layer1.length);
        String space = StringUtils.leftPad("", layer1.length - 5);
        String numbersLine = numbers + "   " + numbers + "   " + numbers;
        String firstPart = " Layer1 " + space + " Layer2" + space + " Layer3" + "\n  " + numbersLine;

        for (int i = 0; i < layer1.length; ++i) {
            int ii = size - 1 - i;
            String index = (ii < 10 ? " " : "") + ii;
            builder.append(index + layer1[i]
                    + " " + index + maskOverlay(layer2[i], layer1[i])
                    + " " + index + maskOverlay(layer3[i], layer1[i]));

            switch (i) {
                case 0:
                    builder.append(" Robots: " + getMe() + "," + listToString(getOtherHeroes()));
                    break;
                case 1:
                    builder.append(" Gold: " + listToString(getGold()));
                    break;
                case 2:
                    builder.append(" Starts: " + listToString(getStarts()));
                    break;
                case 3:
                    builder.append(" Exits: " + listToString(getExits()));
                    break;
                case 4:
                    builder.append(" Boxes: " + listToString(getBoxes()));
                    break;
                case 5:
                    builder.append(" Holes: " + listToString(getHoles()));
                    break;
                case 6:
                    builder.append(" LaserMachine: " + listToString(getLaserMachines()));
                    break;
                case 7:
                    builder.append(" Lasers: " + listToString(getLasers()));
                    break;
                case 8:
                    builder.append(" Zombies: " + listToString(getZombies()));
                    break;
            }

            if (i != layer1.length - 1) {
                builder.append("\n");
            }
        }

        return firstPart + "\n" + builder.toString()+ "\n  " + numbersLine;
    }

    private String listToString(List<? extends Object> list) {
        String result = list.toString();

        return result.substring(1, result.length() - 1);
    }

    public Point getScannerOffset() {
        JSONObject offset = source.getJSONObject("offset");
        return pt(offset.getInt("x"), offset.getInt("y"));
    }
}