package com.codenjoy.dojo.fifteen.model;

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


import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;

import java.util.HashMap;
import java.util.Map;

import static com.codenjoy.dojo.services.PointImpl.pt;

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

    private static final Map<Elements, Point> CORRECT_POSITION = new HashMap<>();

    static {
        CORRECT_POSITION.put(Elements.A, pt(1, 4));
        CORRECT_POSITION.put(Elements.B, pt(2, 4));
        CORRECT_POSITION.put(Elements.C, pt(3, 4));
        CORRECT_POSITION.put(Elements.D, pt(4, 4));
        CORRECT_POSITION.put(Elements.E, pt(1, 3));
        CORRECT_POSITION.put(Elements.F, pt(2, 3));
        CORRECT_POSITION.put(Elements.G, pt(3, 3));
        CORRECT_POSITION.put(Elements.H, pt(4, 3));
        CORRECT_POSITION.put(Elements.I, pt(1, 2));
        CORRECT_POSITION.put(Elements.J, pt(2, 2));
        CORRECT_POSITION.put(Elements.K, pt(3, 2));
        CORRECT_POSITION.put(Elements.L, pt(4, 2));
        CORRECT_POSITION.put(Elements.M, pt(1, 1));
        CORRECT_POSITION.put(Elements.N, pt(2, 1));
        CORRECT_POSITION.put(Elements.O, pt(3, 1));
    }


    public boolean isRightPosition(Digit digit) {
        Elements element = digit.state(null);
        return digit.itsMe(CORRECT_POSITION.get(element));
    }
}
