package com.codenjoy.dojo.services;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2019 Codenjoy
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

import lombok.experimental.UtilityClass;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;

@UtilityClass
public class BoardUtils {

    public static Optional<Point> freeRandom(int size, Dice dice, Predicate<Point> isFree) {
        return freeRandom(
                () -> dice.next(size),
                () -> dice.next(size),
                isFree);
    }

    public static Optional<Point> freeRandom(Supplier<Integer> getX,
                                             Supplier<Integer> getY,
                                             Predicate<Point> isFree)
    {
        Point pt = new PointImpl();
        int count = 0;
        int max = 100;
        do {
            pt.setX(getX.get());
            pt.setY(getY.get());
        } while (!isFree.test(pt) && count++ < max);

        if (count >= max) {
            return Optional.empty();
        }

        return Optional.of(pt);
    }

}
