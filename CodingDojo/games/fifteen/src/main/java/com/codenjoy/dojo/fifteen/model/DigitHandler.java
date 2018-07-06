package com.codenjoy.dojo.fifteen.model;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 Codenjoy
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


import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;

import java.util.HashMap;
import java.util.Map;

public class DigitHandler {
    public static final Elements[] DIGITS =
            {
                    Elements.A,
                    Elements.B,
                    Elements.C,
                    Elements.D,
                    Elements.E,
                    Elements.F,
                    Elements.G,
                    Elements.H,
                    Elements.I,
                    Elements.J,
                    Elements.K,
                    Elements.L,
                    Elements.M,
                    Elements.N,
                    Elements.O
            };

    private static final Map<Elements, Point> CORRECT_POSITION = new HashMap<Elements, Point>();

    static {
        CORRECT_POSITION.put(Elements.A, new PointImpl(1, 4));
        CORRECT_POSITION.put(Elements.B, new PointImpl(2, 4));
        CORRECT_POSITION.put(Elements.C, new PointImpl(3, 4));
        CORRECT_POSITION.put(Elements.D, new PointImpl(4, 4));
        CORRECT_POSITION.put(Elements.E, new PointImpl(1, 3));
        CORRECT_POSITION.put(Elements.F, new PointImpl(2, 3));
        CORRECT_POSITION.put(Elements.G, new PointImpl(3, 3));
        CORRECT_POSITION.put(Elements.H, new PointImpl(4, 3));
        CORRECT_POSITION.put(Elements.I, new PointImpl(1, 2));
        CORRECT_POSITION.put(Elements.J, new PointImpl(2, 2));
        CORRECT_POSITION.put(Elements.K, new PointImpl(3, 2));
        CORRECT_POSITION.put(Elements.L, new PointImpl(4, 2));
        CORRECT_POSITION.put(Elements.M, new PointImpl(1, 1));
        CORRECT_POSITION.put(Elements.N, new PointImpl(2, 1));
        CORRECT_POSITION.put(Elements.O, new PointImpl(3, 1));
    }


    public boolean isRightPosition(Digit digit) {
        Elements element = digit.state(null);
        return digit.itsMe(CORRECT_POSITION.get(element));
    }
}
