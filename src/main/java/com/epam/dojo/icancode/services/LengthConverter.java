package com.epam.dojo.icancode.services;

import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;

/**
 * Created by Mikhail_Udalyi on 23.06.2016.
 */
public class LengthConverter {
    public static Point getXY(int index, int size) {
        //return new PointImpl(index % size, index / size);
        return new PointImpl(index % size, size - 1 - index / size);
    }

    public static int getLength(int x, int y, int size) {
        //return y * size + x;
        return (size - 1 - y) * size + x;
    }
}
