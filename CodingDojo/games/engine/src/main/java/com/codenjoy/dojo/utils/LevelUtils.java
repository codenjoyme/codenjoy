package com.codenjoy.dojo.utils;

import com.codenjoy.dojo.services.LengthToXY;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.printer.CharElements;
import lombok.experimental.UtilityClass;

import java.util.LinkedList;
import java.util.List;
import java.util.function.BiFunction;

@UtilityClass
public class LevelUtils {

    public <T, E extends CharElements> List<T> getObjects(LengthToXY xy, String map,
                                                           BiFunction<Point, E, T> objects,
                                                           E... elements)
    {
        List<T> result = new LinkedList<>();
        for (int index = 0; index < map.length(); index++) {
            for (E el : elements) {
                if (map.charAt(index) == el.ch()) {
                    Point pt = xy.getXY(index);
                    result.add(objects.apply(pt, el));
                }
            }
        }
        return result;
    }

}
