package com.epam.dojo.icancode.client;

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
import com.epam.dojo.icancode.model.Elements;

import java.util.List;

import static com.epam.dojo.icancode.model.Elements.*;
import static com.epam.dojo.icancode.model.Elements.Layers.*;

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
        return !(isAt(LAYER2, x, y, EMPTY, GOLD, ROBO_OTHER)
                && isAt(LAYER1, x, y, FLOOR, START, EXIT, GOLD));
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
        return get(LAYER2,
                ROBO_FALLING,
                ROBO_FLYING,
                ROBO_FLYING_ON_BOX,
                ROBO_LASER,
                ROBO).get(0);
    }

    /**
     * @return Returns list of coordinates for all visible enemy Robots.
     */
    public List<Point> getOtherHeroes() {
        return get(LAYER2,
                ROBO_OTHER_FALLING,
                ROBO_OTHER_FLYING,
                ROBO_OTHER_FLYING_ON_BOX,
                ROBO_OTHER_LASER,
                ROBO_OTHER);
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
    public List<Point> getLaser() {
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
                BOX,
                ROBO_FLYING_ON_BOX,
                ROBO_OTHER_FLYING_ON_BOX);
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

    /**
     * @return Returns list of coordinates for all visible Exit points.
     */
    public List<Point> getExit() {
        return get(LAYER1, EXIT);
    }

    /**
     * @return Returns list of coordinates for all visible Start points.
     */
    public List<Point> getStart() {
        return get(LAYER1, START);
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
        return get(LAYER2, ROBO_FALLING, ROBO_LASER).isEmpty();
    }

    @Override
    public String toString() {
        return String.format(
                "Board layer 1:\n%s\n" +
                "Board layer 2:\n%s\n" +

                "Start at: %s\n" +
                "Exit at: %s\n" +

                "Gold at: %s\n" +
                "Boxes at: %s\n" +
                "Holes at: %s\n" +

                "Robot at: %s\n" +
                "Other robots at: %s\n" +

                "LaserMachine at: %s\n" +
                "Laser at: %s",

                boardAsString(LAYER1),
                boardAsString(LAYER2),

                getStart(),
                getExit(),

                getGold(),
                getBoxes(),
                getHoles(),

                getMe(),
                getOtherHeroes(),

                getLaserMachines(),
                getLaser());
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
                if (isBarrierAt(atWay.getX(), atWay.getY())) return false;
                return true;
            }
        };
    }

    /**
     * @param to Destination point.
     * @return Shortest path (list of directions where to move) from your robot location to coordinates specified.
     */
    public List<Direction> getShortestWay(List<Point> to) {
        DeikstraFindWay.Possible map = possible();
        DeikstraFindWay findWay = new DeikstraFindWay();
        return findWay.getShortestWay(size(), getMe(), to, map);
    }
}