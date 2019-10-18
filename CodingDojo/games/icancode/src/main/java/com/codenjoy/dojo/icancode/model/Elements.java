package com.codenjoy.dojo.icancode.model;

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


import com.codenjoy.dojo.services.printer.CharElements;

import java.util.Arrays;
import java.util.Dictionary;
import java.util.Hashtable;

import static com.codenjoy.dojo.icancode.model.Elements.Layers.*;

/**
 * All possible elements on board.
 */
public enum Elements implements CharElements {
    // empty space where player can go
    EMPTY(LAYER2, '-'),
    FLOOR(LAYER1, '.'),

    // walls
    ANGLE_IN_LEFT(LAYER1, '╔'),
    WALL_FRONT(LAYER1, '═'),
    ANGLE_IN_RIGHT(LAYER1, '┐'),
    WALL_RIGHT(LAYER1, '│'),
    ANGLE_BACK_RIGHT(LAYER1, '┘'),
    WALL_BACK(LAYER1, '─'),
    ANGLE_BACK_LEFT(LAYER1, '└'),
    WALL_LEFT(LAYER1, '║'),
    WALL_BACK_ANGLE_LEFT(LAYER1, '┌'),
    WALL_BACK_ANGLE_RIGHT(LAYER1, '╗'),
    ANGLE_OUT_RIGHT(LAYER1, '╝'),
    ANGLE_OUT_LEFT(LAYER1, '╚'),
    SPACE(LAYER1, ' '),

    // laser machine
    LASER_MACHINE_CHARGING_LEFT(LAYER1, '˂'),
    LASER_MACHINE_CHARGING_RIGHT(LAYER1, '˃'),
    LASER_MACHINE_CHARGING_UP(LAYER1, '˄'),
    LASER_MACHINE_CHARGING_DOWN(LAYER1, '˅'),

    // lase machine ready
    LASER_MACHINE_READY_LEFT(LAYER1, '◄'),
    LASER_MACHINE_READY_RIGHT(LAYER1, '►'),
    LASER_MACHINE_READY_UP(LAYER1, '▲'),
    LASER_MACHINE_READY_DOWN(LAYER1, '▼'),

    // other stuff
    START(LAYER1, 'S'),
    EXIT(LAYER1, 'E'),
    HOLE(LAYER1, 'O'),
    BOX(LAYER2, 'B'),
    ZOMBIE_START(LAYER1, 'Z'),
    GOLD(LAYER1, '$'),

    // your robot
    ROBO(LAYER2, '☺'),
    ROBO_FALLING(LAYER2, 'o'),
    ROBO_FLYING(LAYER3, '*'),
    ROBO_LASER(LAYER2, '☻'),

    // other robot
    ROBO_OTHER(LAYER2, 'X'),
    ROBO_OTHER_FALLING(LAYER2, 'x'),
    ROBO_OTHER_FLYING(LAYER3, '^'),
    ROBO_OTHER_LASER(LAYER2, '&'),

    // laser
    LASER_LEFT(LAYER2, '←'),
    LASER_RIGHT(LAYER2, '→'),
    LASER_UP(LAYER2, '↑'),
    LASER_DOWN(LAYER2, '↓'),

    // zombie
    FEMALE_ZOMBIE(LAYER2, '♀'),
    MALE_ZOMBIE(LAYER2, '♂'),
    ZOMBIE_DIE(LAYER2, '✝'),

    // system elements, don't touch it
    FOG(LAYER1, 'F'),
    BACKGROUND(LAYER2, 'G');

    public static class Layers {
        public final static int LAYER1 = 0;
        public final static int LAYER2 = 1;
        public final static int LAYER3 = 2;
    }

    private static volatile Dictionary<String, Elements> elementsMap;

    private final char ch;
    private final int layer;

    Elements(int layer, char ch) {
        this.layer = layer;
        this.ch = ch;
    }

    @Override
    public char ch() {
        return ch;
    }

    @Override
    public String toString() {
        return String.valueOf(ch);
    }

    public static Elements valueOf(char ch) {
        if (elementsMap == null) {
            makeElementsMap();
        }

        Elements result = elementsMap.get(String.valueOf(ch));

        if (result == null) {
            throw new IllegalArgumentException("No such element for '" + ch + "'");
        }

        return result;
    }

    private static void makeElementsMap() {
        elementsMap = new Hashtable<>();

        for (Elements el : Elements.values()) {
            elementsMap.put(el.toString(), el);
        }
    }

    public int getLayer() {
        return layer;
    }

    // TODO duplicate logic with ElementsMapper but we cant add it to client because a lot of dependencies
    public static boolean isWall(Elements el) {
        return Arrays.asList(ANGLE_IN_LEFT,
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
                SPACE).contains(el);
    }

}
