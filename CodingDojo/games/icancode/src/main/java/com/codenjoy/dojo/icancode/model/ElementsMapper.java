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


import com.codenjoy.dojo.icancode.model.items.*;

import java.util.EnumMap;
import java.util.Map;

import static com.codenjoy.dojo.icancode.model.Elements.*;

public class ElementsMapper {

    public static Map<Elements, Class<? extends BaseItem>> map =
            new EnumMap<Elements, Class<? extends BaseItem>>(Elements.class)
    {{
        put(EMPTY, Air.class);
        put(FLOOR, Floor.class);

        put(ANGLE_IN_LEFT, Wall.class);
        put(WALL_FRONT, Wall.class);
        put(ANGLE_IN_RIGHT, Wall.class);
        put(WALL_RIGHT, Wall.class);
        put(ANGLE_BACK_RIGHT, Wall.class);
        put(WALL_BACK, Wall.class);
        put(ANGLE_BACK_LEFT, Wall.class);
        put(WALL_LEFT, Wall.class);
        put(WALL_BACK_ANGLE_LEFT, Wall.class);
        put(WALL_BACK_ANGLE_RIGHT, Wall.class);
        put(ANGLE_OUT_RIGHT, Wall.class);
        put(ANGLE_OUT_LEFT, Wall.class);
        put(SPACE, Wall.class);

        put(LASER_MACHINE_CHARGING_LEFT, LaserMachine.class);
        put(LASER_MACHINE_CHARGING_RIGHT, LaserMachine.class);
        put(LASER_MACHINE_CHARGING_UP, LaserMachine.class);
        put(LASER_MACHINE_CHARGING_DOWN, LaserMachine.class);

        put(LASER_MACHINE_READY_LEFT, LaserMachine.class);
        put(LASER_MACHINE_READY_RIGHT, LaserMachine.class);
        put(LASER_MACHINE_READY_UP, LaserMachine.class);
        put(LASER_MACHINE_READY_DOWN, LaserMachine.class);

        put(START, Start.class);
        put(EXIT, Exit.class);
        put(HOLE, Hole.class);
        put(BOX, Box.class);
        put(GOLD, Gold.class);

        put(ROBO, HeroItem.class);
        put(ROBO_FALLING, HeroItem.class);
        put(ROBO_FLYING, HeroItem.class);
        put(ROBO_LASER, HeroItem.class);

        put(ROBO_OTHER, HeroItem.class);
        put(ROBO_OTHER_FALLING, HeroItem.class);
        put(ROBO_OTHER_FLYING, HeroItem.class);
        put(ROBO_OTHER_LASER, HeroItem.class);

        put(LASER_LEFT, Laser.class);
        put(LASER_RIGHT, Laser.class);
        put(LASER_UP, Laser.class);
        put(LASER_DOWN, Laser.class);

        put(ZOMBIE_START, ZombiePot.class);
        put(FEMALE_ZOMBIE, Zombie.class);
        put(MALE_ZOMBIE, Zombie.class);

        put(FOG, Wall.class);
        put(BACKGROUND, Wall.class);
    }};

    public static Class<? extends BaseItem> getItsClass(Elements element) {
        Class<? extends BaseItem> result = map.get(element);
        if (result == null) {
            throw new IllegalArgumentException("Please add element class: " + element.getClass());
        }
        return result;
    }

    public static int levelFor(Class<? extends BaseItem> clazz) {
        return map.entrySet().stream()
                .filter(entry -> entry.getValue().equals(clazz))
                .findFirst()
                .get()
                .getKey()
                .getLayer();
    }
}
