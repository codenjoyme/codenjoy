package com.epam.dojo.expansion.model.items;

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


import com.epam.dojo.expansion.model.Elements;

import java.util.EnumMap;
import java.util.Map;

import static com.epam.dojo.expansion.model.Elements.*;

/**
 * Created by indigo on 2016-09-22.
 */
public class ElementsMapper {

    public static Map<Elements, Class<? extends BaseItem>> map =
            new EnumMap<Elements, Class<? extends BaseItem>>(Elements.class)
    {{
        put(EMPTY, None.class);
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

        put(START, Start.class);
        put(EXIT, Exit.class);
        put(HOLE, Hole.class);
        put(BOX, Box.class);
        put(GOLD, Gold.class);

        put(ROBO, HeroForces.class);

        put(ROBO_OTHER, HeroForces.class);

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
}
