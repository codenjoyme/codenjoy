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
import com.codenjoy.dojo.services.Point;
import com.epam.dojo.icancode.model.Elements;

import java.util.List;

import static com.epam.dojo.icancode.model.Elements.*;
import static com.epam.dojo.icancode.model.Elements.Layers.*;

public class Board extends AbstractBoard<Elements> {

    @Override
    public Elements valueOf(char ch) {
        return Elements.valueOf(ch);
    }

    public boolean isBarrierAt(int x, int y) {
        return !(isAt(LAYER2, x, y, EMPTY, GOLD, ROBO_OTHER)
                && isAt(LAYER1, x, y, FLOOR, START, EXIT, Elements.GOLD));
    }

    public boolean isHoleAt(int x, int y) {
        return isAt(LAYER1, x, y, HOLE);
    }

    public Point getMe() {
        return get(LAYER2,
                ROBO_FALLING,
                ROBO_FLYING,
                ROBO_FLYING_ON_BOX,
                ROBO_LASER,
                ROBO).get(0);
    }

    public List<Point> getOtherHeroes() {
        return get(LAYER2,
                ROBO_OTHER_FALLING,
                ROBO_OTHER_FLYING,
                ROBO_OTHER_FLYING_ON_BOX,
                ROBO_OTHER_LASER,
                ROBO_OTHER);
    }

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

    public List<Point> getLaser() {
        return get(LAYER2,
                LASER_LEFT,
                LASER_RIGHT,
                LASER_UP,
                LASER_DOWN);
    }

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

    public List<Point> getBoxes() {
        return get(LAYER2,
                BOX,
                ROBO_FLYING_ON_BOX,
                ROBO_OTHER_FLYING_ON_BOX);
    }

    public List<Point> getHoles() {
        return get(LAYER1,
                HOLE,
                ROBO_FALLING,
                ROBO_OTHER_FALLING);
    }

    public List<Point> getExit() {
        return get(LAYER1, EXIT);
    }

    public List<Point> getStart() {
        return get(LAYER1, START);
    }

    public List<Point> getGold() {
        return get(LAYER1, GOLD);
    }

    public boolean isMeAlive() {
        return get(LAYER2, ROBO_FALLING, ROBO_LASER).isEmpty();
    }

    @Override
    public String toString() {
        String firstPart = "Layer1 Layer2\n 01234567890   01234567890";

        StringBuilder builder = new StringBuilder();
        String[] layer1 = boardAsString(LAYER1).split("\n");
        String[] layer2 = boardAsString(LAYER2).split("\n");

        for (int i = 0; i < layer1.length; ++i) {
            builder.append((i < 10 ? " " : "") + i + layer1[i] + " " + (i < 10 ? " " : "") + i + layer2[i]);

            switch (i) {
                case 0:
                    builder.append(" Robots: " + getMe() + "," + listToString(getOtherHeroes()));
                    break;
                case 1:
                    builder.append(" Gold: " + listToString(getGold()));
                    break;
                case 2:
                    builder.append(" Start: " + listToString(getStart()));
                    break;
                case 3:
                    builder.append(" Exit: " + listToString(getExit()));
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
                    builder.append(" Laser: " + listToString(getLaser()));
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
}