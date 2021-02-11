package com.codenjoy.dojo.expansion.model.levels.items;

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


import com.codenjoy.dojo.expansion.model.Elements;

import java.util.EnumMap;
import java.util.Map;

/**
 * Created by indigo on 2016-09-22.
 */
public class ElementsMapper {

    public static Map<Elements, Class<? extends BaseItem>> map =
            new EnumMap<Elements, Class<? extends BaseItem>>(Elements.class)
    {{
        put(Elements.EMPTY, None.class);
        put(Elements.FLOOR, Floor.class);

        put(Elements.ANGLE_IN_LEFT, Wall.class);
        put(Elements.WALL_FRONT, Wall.class);
        put(Elements.ANGLE_IN_RIGHT, Wall.class);
        put(Elements.WALL_RIGHT, Wall.class);
        put(Elements.ANGLE_BACK_RIGHT, Wall.class);
        put(Elements.WALL_BACK, Wall.class);
        put(Elements.ANGLE_BACK_LEFT, Wall.class);
        put(Elements.WALL_LEFT, Wall.class);
        put(Elements.WALL_BACK_ANGLE_LEFT, Wall.class);
        put(Elements.WALL_BACK_ANGLE_RIGHT, Wall.class);
        put(Elements.ANGLE_OUT_RIGHT, Wall.class);
        put(Elements.ANGLE_OUT_LEFT, Wall.class);
        put(Elements.SPACE, Wall.class);

        put(Elements.BASE1, Start.class);
        put(Elements.BASE2, Start.class);
        put(Elements.BASE3, Start.class);
        put(Elements.BASE4, Start.class);

        put(Elements.EXIT, Exit.class);
        put(Elements.HOLE, Hole.class);
        put(Elements.BREAK, Box.class);
        put(Elements.GOLD, Gold.class);

        put(Elements.FORCE1, HeroForces.class);
        put(Elements.FORCE2, HeroForces.class);
        put(Elements.FORCE3, HeroForces.class);
        put(Elements.FORCE4, HeroForces.class);

        put(Elements.FOG, Wall.class);
        put(Elements.BACKGROUND, Wall.class);
    }};

    public static Class<? extends BaseItem> getItsClass(Elements element) {
        Class<? extends BaseItem> result = map.get(element);
        if (result == null) {
            throw new IllegalArgumentException(
                    String.format("Please add element '%s' in class: %s",
                    element.ch(),
                    ElementsMapper.class)
            );
        }
        return result;
    }
}
