package com.epam.dojo.icancode.model;

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


import com.codenjoy.dojo.services.CharElements;
import com.epam.dojo.icancode.model.items.BaseItem;
import com.epam.dojo.icancode.model.items.Box;
import com.epam.dojo.icancode.model.items.Exit;
import com.epam.dojo.icancode.model.items.Floor;
import com.epam.dojo.icancode.model.items.Gold;
import com.epam.dojo.icancode.model.items.Hero;
import com.epam.dojo.icancode.model.items.Hole;
import com.epam.dojo.icancode.model.items.Laser;
import com.epam.dojo.icancode.model.items.LaserMachine;
import com.epam.dojo.icancode.model.items.None;
import com.epam.dojo.icancode.model.items.Start;
import com.epam.dojo.icancode.model.items.Wall;

import java.util.Dictionary;
import java.util.Hashtable;

import static com.epam.dojo.icancode.model.Elements.Layers.LAYER1;
import static com.epam.dojo.icancode.model.Elements.Layers.LAYER2;

/**
 * Тут указана легенда всех возможных объектов на поле и их состояний.
 * Важно помнить, что для каждой енумной константы надо создать спрайт в папке \src\main\webapp\resources\sprite.
 */
public enum Elements implements CharElements {
    EMPTY(LAYER2, '-', None.class),
    FLOOR(LAYER1, '.', Floor.class),

    ANGLE_IN_LEFT(LAYER1, '╔', Wall.class),
    WALL_FRONT(LAYER1, '═', Wall.class),
    ANGLE_IN_RIGHT(LAYER1, '┐', Wall.class),
    WALL_RIGHT(LAYER1, '│', Wall.class),
    ANGLE_BACK_RIGHT(LAYER1, '┘', Wall.class),
    WALL_BACK(LAYER1, '─', Wall.class),
    ANGLE_BACK_LEFT(LAYER1, '└', Wall.class),
    WALL_LEFT(LAYER1, '║', Wall.class),
    WALL_BACK_ANGLE_LEFT(LAYER1, '┌', Wall.class),
    WALL_BACK_ANGLE_RIGHT(LAYER1, '╗', Wall.class),
    ANGLE_OUT_RIGHT(LAYER1, '╝', Wall.class),
    ANGLE_OUT_LEFT(LAYER1, '╚', Wall.class),
    SPACE(LAYER1, ' ', Wall.class),

    LASER_MACHINE_CHARGING_LEFT(LAYER1, '˂', LaserMachine.class),
    LASER_MACHINE_CHARGING_RIGHT(LAYER1, '˃', LaserMachine.class),
    LASER_MACHINE_CHARGING_UP(LAYER1, '˄', LaserMachine.class),
    LASER_MACHINE_CHARGING_DOWN(LAYER1, '˅', LaserMachine.class),

    LASER_MACHINE_READY_LEFT(LAYER1, '◄', LaserMachine.class),
    LASER_MACHINE_READY_RIGHT(LAYER1, '►', LaserMachine.class),
    LASER_MACHINE_READY_UP(LAYER1, '▲', LaserMachine.class),
    LASER_MACHINE_READY_DOWN(LAYER1, '▼', LaserMachine.class),

    START(LAYER1, 'S', Start.class),
    EXIT(LAYER1, 'E', Exit.class),
    HOLE(LAYER1, 'O', Hole.class),
    BOX(LAYER1, 'B', true, Box.class),
    GOLD(LAYER1, '$', Gold.class),

    ROBO(LAYER2, '☺', true, Hero.class),
    ROBO_FALLING(LAYER2, 'o', true, Hero.class),
    ROBO_FLYING(LAYER2, '*', true, Hero.class),
    ROBO_FLYING_ON_BOX(LAYER2, '№', true, Hero.class),
    ROBO_LASER(LAYER2, '☻', true, Hero.class),

    ROBO_OTHER(LAYER2, 'X', true, Hero.class),
    ROBO_OTHER_FALLING(LAYER2, 'x', true, Hero.class),
    ROBO_OTHER_FLYING(LAYER2, '^', true, Hero.class),
    ROBO_OTHER_FLYING_ON_BOX(LAYER2, '%', true, Hero.class),
    ROBO_OTHER_LASER(LAYER2, '&', true, Hero.class),

    LASER_LEFT(LAYER2, '←', true, Laser.class),
    LASER_RIGHT(LAYER2, '→', true, Laser.class),
    LASER_UP(LAYER2, '↑', true, Laser.class),
    LASER_DOWN(LAYER2, '↓', true, Laser.class),

    FOG(LAYER1, 'F', Wall.class),
    BACKGROUND(LAYER1, 'G', Wall.class);

    public static class Layers {
        public final static int LAYER1 = 0;
        public final static int LAYER2 = 1;
    }

    private static volatile Dictionary<String, Elements> elementsMap;

    private final Class<BaseItem> itsClass;
    private final char ch;
    private final int layer;
    private Character atBottom;

    Elements(int layer, char ch, Class itsClass) {
        this.layer = layer;
        this.ch = ch;
        this.atBottom = null;
        this.itsClass = itsClass;
    }

    Elements(int layer, char ch, boolean atFloor, Class itsClass) {
        this.layer = layer;
        this.ch = ch;
        if (atFloor) {
            this.atBottom = '.';
        }
        this.itsClass = itsClass;
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
            throw new IllegalArgumentException("No such element for " + ch);
        }

        return result;
    }

    private static void makeElementsMap() {
        elementsMap = new Hashtable<>();

        for (Elements el : Elements.values()) {
            elementsMap.put(el.toString(), el);
        }
    }

    public Character getAtBottom() {
        return atBottom;
    }

    public Class<BaseItem> getItsClass() {
        return itsClass;
    }
}
