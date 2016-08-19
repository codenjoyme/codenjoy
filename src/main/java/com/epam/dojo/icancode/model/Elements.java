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

/**
 * Тут указана легенда всех возможных объектов на поле и их состояний.
 * Важно помнить, что для каждой енумной константы надо создать спрайт в папке \src\main\webapp\resources\sprite.
 */
public enum Elements implements CharElements {
    EMPTY('-', None.class),
    FLOOR('.', Floor.class),

    ANGLE_IN_LEFT('╔', Wall.class),
    WALL_FRONT('═', Wall.class),
    ANGLE_IN_RIGHT('┐', Wall.class),
    WALL_RIGHT('│', Wall.class),
    ANGLE_BACK_RIGHT('┘', Wall.class),
    WALL_BACK('─', Wall.class),
    ANGLE_BACK_LEFT('└', Wall.class),
    WALL_LEFT('║', Wall.class),
    WALL_BACK_ANGLE_LEFT('┌', Wall.class),
    WALL_BACK_ANGLE_RIGHT('╗', Wall.class),
    ANGLE_OUT_RIGHT('╝', Wall.class),
    ANGLE_OUT_LEFT('╚', Wall.class),
    SPACE(' ', Wall.class),

    ROBO('☺', true, Hero.class),
    ROBO_FALLING('o', true, Hero.class),
    ROBO_FLYING('*', true, Hero.class),
    ROBO_LASER('☻', true, Hero.class),

    ROBO_OTHER('X', true, Hero.class),
    ROBO_OTHER_FALLING('x', true, Hero.class),
    ROBO_OTHER_FLYING('^', true, Hero.class),
    ROBO_OTHER_LASER('&', true, Hero.class),

    LASER_MACHINE_CHARGING_LEFT('˂', LaserMachine.class),
    LASER_MACHINE_CHARGING_RIGHT('˃', LaserMachine.class),
    LASER_MACHINE_CHARGING_UP('˄', LaserMachine.class),
    LASER_MACHINE_CHARGING_DOWN('˅', LaserMachine.class),

    LASER_MACHINE_READY_LEFT('◄', LaserMachine.class),
    LASER_MACHINE_READY_RIGHT('►', LaserMachine.class),
    LASER_MACHINE_READY_UP('▲', LaserMachine.class),
    LASER_MACHINE_READY_DOWN('▼', LaserMachine.class),

    LASER_LEFT('←', true, Laser.class),
    LASER_RIGHT('→', true, Laser.class),
    LASER_UP('↑', true, Laser.class),
    LASER_DOWN('↓', true, Laser.class),

    START('S', Start.class),
    EXIT('E', Exit.class),
    GOLD('$', Gold.class),
    HOLE('O', Hole.class),
    BOX('B', true, Box.class),

    FOG('F', Wall.class),
    BACKGROUND('G', Wall.class);

    private static volatile Dictionary<String, Elements> elementsMap;

    private final Class<BaseItem> itsClass;
    private final char ch;
    private Character atBottom;

    Elements(char ch, Class itsClass) {
        this.ch = ch;
        this.atBottom = null;
        this.itsClass = itsClass;
    }

    Elements(char ch, boolean atFloor, Class itsClass) {
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
