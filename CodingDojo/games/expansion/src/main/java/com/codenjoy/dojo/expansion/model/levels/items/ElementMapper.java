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


import com.codenjoy.dojo.games.expansion.Element;

import java.util.EnumMap;
import java.util.Map;

public class ElementMapper {

    public static Map<Element, Class<? extends BaseItem>> map =
            new EnumMap<>(Element.class)
    {{
        put(Element.EMPTY, None.class);
        put(Element.FLOOR, Floor.class);

        put(Element.ANGLE_IN_LEFT, Wall.class);
        put(Element.WALL_FRONT, Wall.class);
        put(Element.ANGLE_IN_RIGHT, Wall.class);
        put(Element.WALL_RIGHT, Wall.class);
        put(Element.ANGLE_BACK_RIGHT, Wall.class);
        put(Element.WALL_BACK, Wall.class);
        put(Element.ANGLE_BACK_LEFT, Wall.class);
        put(Element.WALL_LEFT, Wall.class);
        put(Element.WALL_BACK_ANGLE_LEFT, Wall.class);
        put(Element.WALL_BACK_ANGLE_RIGHT, Wall.class);
        put(Element.ANGLE_OUT_RIGHT, Wall.class);
        put(Element.ANGLE_OUT_LEFT, Wall.class);
        put(Element.SPACE, Wall.class);

        put(Element.BASE1, Start.class);
        put(Element.BASE2, Start.class);
        put(Element.BASE3, Start.class);
        put(Element.BASE4, Start.class);

        put(Element.EXIT, Exit.class);
        put(Element.HOLE, Hole.class);
        put(Element.BREAK, Box.class);
        put(Element.GOLD, Gold.class);

        put(Element.FORCE1, HeroForces.class);
        put(Element.FORCE2, HeroForces.class);
        put(Element.FORCE3, HeroForces.class);
        put(Element.FORCE4, HeroForces.class);

        put(Element.FOG, Wall.class);
        put(Element.BACKGROUND, Wall.class);
    }};

    public static Class<? extends BaseItem> getItsClass(Element element) {
        Class<? extends BaseItem> result = map.get(element);
        if (result == null) {
            throw new IllegalArgumentException(
                    String.format("Please add element '%s' in class: %s",
                    element.ch(),
                    ElementMapper.class)
            );
        }
        return result;
    }
}
