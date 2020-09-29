package com.codenjoy.dojo.battlecity.model;

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

import com.codenjoy.dojo.battlecity.model.items.Prize;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Point;

import java.util.Arrays;
import java.util.List;

import static com.codenjoy.dojo.services.PointImpl.random;

public class PrizeGenerator {

    private static final List<Elements> PRIZES = Arrays.asList(
            Elements.PRIZE_IMMORTALITY,
            Elements.PRIZE_BREAKING_WALLS,
            Elements.PRIZE_WALKING_ON_WATER);

    private Field field;
    private Dice dice;

    public PrizeGenerator(Field field, Dice dice) {
        this.field = field;
        this.dice = dice;
    }

    public void drop() {
        Point pt;
        int c = 0;
        do {
            pt = random(dice, field.size());
        } while (field.isBarrier(pt) && c++ < field.size());

        if (!field.isBarrier(pt)) {
            field.addPrize(new Prize(pt, PRIZES.get(dice.next(PRIZES.size()))));
        }
    }
}
