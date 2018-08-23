package com.codenjoy.dojo.tetris.model.levels.random;

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


import com.codenjoy.dojo.services.Dice;

/**
 * User: oleksandr.baglai
 * Date: 9/25/12
 * Time: 10:36 AM
 */
public class ProbabilityRandomizer implements Randomizer {

    private Dice dice;
    private int lastFigureProbability;

    public ProbabilityRandomizer(Dice dice, int lastFigureProbability) {
        this.dice = dice;
        this.lastFigureProbability = lastFigureProbability;
    }

    @Override
    public int getNextNumber(int count) {
        int i = dice.next(count);
        if (i == count - 1) {
            int j = dice.next(100);
            if (j <= lastFigureProbability) {
                return i;
            } else {
                return dice.next(count - 1);
            }
        }
        return i;
    }
}
