package com.codenjoy.dojo.utils;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2020 Codenjoy
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

import com.codenjoy.dojo.services.LengthToXY;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.printer.CharElements;
import lombok.experimental.UtilityClass;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;

@UtilityClass
public class LevelUtils {

    public <T, E extends CharElements> List<T> getObjects(LengthToXY xy, String map,
                                                           BiFunction<Point, E, T> objects,
                                                           E... elements)
    {
        List<T> result = new LinkedList<>();
        for (E el : elements) {
            for (int index = 0; index < map.length(); index++) {
                if (map.charAt(index) == el.ch()) {
                    Point pt = xy.getXY(index);
                    result.add(objects.apply(pt, el));
                }
            }
        }
        return result;
    }

    public <T, E extends CharElements> List<T> getObjects(LengthToXY xy, String map,
                                                          Function<Point, T> objects,
                                                          E... elements)
    {
        return getObjects(xy, map,
                (pt, el) -> objects.apply(pt),
                elements);
    }

    public static <T, E extends CharElements> List<T> getObjects(LengthToXY xy, String map,
                                                                    Map<E, Function<Point, T>> conversions)
    {
        return conversions.entrySet().stream()
                .flatMap(entry -> getObjects(xy, map,
                        entry.getValue(),
                        entry.getKey()).stream())
                .collect(toList());
    }
}
