package com.codenjoy.dojo.a2048.model.generator;

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


import com.codenjoy.dojo.a2048.model.*;
import com.codenjoy.dojo.a2048.model.Number;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Point;

import static com.codenjoy.dojo.services.PointImpl.pt;

public class RandomGenerator implements Generator {

    public static final Point NO_SPACE = pt(-1, -1);

    private int count;
    private Dice dice;

    public RandomGenerator(Dice dice, int count) {
        this.count = count;
        this.dice = dice;
    }

    @Override
    public void generate(Numbers numbers) {
        for (int i = 0; i < count; i++) {
            Point pt = getFreeRandom(numbers);
            if (!pt.itsMe(NO_SPACE)) {
                numbers.add(new Number(2, pt));
            }
        }
    }

    public Point getFreeRandom(Numbers numbers) {
        int rndX = 0;
        int rndY = 0;
        int c = 0;
        do {
            rndX = dice.next(numbers.size());
            rndY = dice.next(numbers.size());
        } while (rndX != -1 && rndY != -1 && numbers.isBusy(rndX, rndY) && c++ < 100);

        if (c >= 100) {
            return NO_SPACE;
        }

        return pt(rndX, rndY);
    }
}
