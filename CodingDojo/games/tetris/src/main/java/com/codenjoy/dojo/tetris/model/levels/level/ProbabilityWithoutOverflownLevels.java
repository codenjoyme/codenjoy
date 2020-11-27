package com.codenjoy.dojo.tetris.model.levels.level;

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
import com.codenjoy.dojo.tetris.model.*;
import com.codenjoy.dojo.tetris.model.levels.gamelevel.ProbabilityFigureTypesLevel;
import com.codenjoy.dojo.tetris.model.levels.random.ProbabilityRandomizer;
import com.codenjoy.dojo.tetris.model.levels.random.Randomizer;

import static com.codenjoy.dojo.tetris.model.Type.*;
import static com.codenjoy.dojo.tetris.model.GlassEvent.Type.WITHOUT_OVERFLOWN_LINES_REMOVED;


/**
 * User: oleksandr.baglai
 * Date: 9/23/12
 * Time: 3:18 PM
 */
public class ProbabilityWithoutOverflownLevels extends Levels {

    public static final int LINES_REMOVED_FOR_NEXT_LEVEL = 20;
    private static final GlassEvent.Type criteria = WITHOUT_OVERFLOWN_LINES_REMOVED;

    private int removedLinesWithoutOverflown;

    public ProbabilityWithoutOverflownLevels(Dice dice, FigureQueue queue) {
        super(new ProbabilityFigureTypesLevel(dice, queue,
                        new GlassEvent<>(criteria, 0),
                        lastFigureProbability(dice, 100),
                        O),
                // + I
                new ProbabilityFigureTypesLevel(dice, queue,
                        new GlassEvent<>(criteria, LINES_REMOVED_FOR_NEXT_LEVEL),
                        lastFigureProbability(dice, 15),
                        O, I),

                new ProbabilityFigureTypesLevel(dice, queue,
                        new GlassEvent<>(criteria, LINES_REMOVED_FOR_NEXT_LEVEL),
                        lastFigureProbability(dice, 50),
                        O, I),

                new ProbabilityFigureTypesLevel(dice, queue,
                        new GlassEvent<>(criteria, LINES_REMOVED_FOR_NEXT_LEVEL),
                        lastFigureProbability(dice, 100),
                        O, I),

                // + J
                new ProbabilityFigureTypesLevel(dice, queue,
                        new GlassEvent<>(criteria, LINES_REMOVED_FOR_NEXT_LEVEL),
                        lastFigureProbability(dice, 15),
                        O, I, J),


                new ProbabilityFigureTypesLevel(dice, queue,
                        new GlassEvent<>(criteria, LINES_REMOVED_FOR_NEXT_LEVEL),
                        lastFigureProbability(dice, 50),
                        O, I, J),

                new ProbabilityFigureTypesLevel(dice, queue,
                        new GlassEvent<>(criteria, LINES_REMOVED_FOR_NEXT_LEVEL),
                        lastFigureProbability(dice, 100),
                        O, I, J),

                // + L
                new ProbabilityFigureTypesLevel(dice, queue,
                        new GlassEvent<>(criteria, LINES_REMOVED_FOR_NEXT_LEVEL),
                        lastFigureProbability(dice, 15),
                        O, I, J, L),


                new ProbabilityFigureTypesLevel(dice, queue,
                        new GlassEvent<>(criteria, LINES_REMOVED_FOR_NEXT_LEVEL),
                        lastFigureProbability(dice, 50),
                        O, I, J, L),

                new ProbabilityFigureTypesLevel(dice, queue,
                        new GlassEvent<>(criteria, LINES_REMOVED_FOR_NEXT_LEVEL),
                        lastFigureProbability(dice, 100),
                        O, I, J, L),

                // + S
                new ProbabilityFigureTypesLevel(dice, queue,
                        new GlassEvent<>(criteria, LINES_REMOVED_FOR_NEXT_LEVEL),
                        lastFigureProbability(dice, 15),
                        O, I, J, L, S),

                new ProbabilityFigureTypesLevel(dice, queue,
                        new GlassEvent<>(criteria, LINES_REMOVED_FOR_NEXT_LEVEL),
                        lastFigureProbability(dice, 50),
                        O, I, J, L, S),


                new ProbabilityFigureTypesLevel(dice, queue,
                        new GlassEvent<>(criteria, LINES_REMOVED_FOR_NEXT_LEVEL),
                        lastFigureProbability(dice, 100),
                        O, I, J, L, S),

                // + Z
                new ProbabilityFigureTypesLevel(dice, queue,
                        new GlassEvent<>(criteria, LINES_REMOVED_FOR_NEXT_LEVEL),
                        lastFigureProbability(dice, 15),
                        O, I, J, L, S, Z),

                new ProbabilityFigureTypesLevel(dice, queue,
                        new GlassEvent<>(criteria, LINES_REMOVED_FOR_NEXT_LEVEL),
                        lastFigureProbability(dice, 50),
                        O, I, J, L, S, Z),

                new ProbabilityFigureTypesLevel(dice, queue,
                        new GlassEvent<>(criteria, LINES_REMOVED_FOR_NEXT_LEVEL),
                        lastFigureProbability(dice, 100),
                        O, I, J, L, S, Z),

                // + T
                new ProbabilityFigureTypesLevel(dice, queue,
                        new GlassEvent<>(criteria, LINES_REMOVED_FOR_NEXT_LEVEL),
                        lastFigureProbability(dice, 15),
                        O, I, J, L, S, Z, T),

                new ProbabilityFigureTypesLevel(dice, queue,
                        new GlassEvent<>(criteria, LINES_REMOVED_FOR_NEXT_LEVEL),
                        lastFigureProbability(dice, 50),
                        O, I, J, L, S, Z, T),

                new ProbabilityFigureTypesLevel(dice, queue,
                        new GlassEvent<>(criteria, LINES_REMOVED_FOR_NEXT_LEVEL),
                        lastFigureProbability(dice, 100),
                        O, I, J, L, S, Z, T));

        removedLinesWithoutOverflown = 0;
    }

    @Override
    public void glassOverflown() {
        removedLinesWithoutOverflown = 0;
        super.glassOverflown();
    }

    @Override
    public void linesRemoved(int amount) {
        removedLinesWithoutOverflown += amount;
        super.linesRemoved(amount);
    }

    @Override
    protected void onLevelChanged() {
        removedLinesWithoutOverflown = 0;
        super.onLevelChanged();
    }

    public GlassEvent nextLevelAcceptedCriteriaOnLinesRemovedEvent(int amount) {
        return new GlassEvent<>(GlassEvent.Type.WITHOUT_OVERFLOWN_LINES_REMOVED,
                removedLinesWithoutOverflown);
    }

    private static Randomizer lastFigureProbability(Dice dice, int i) {
        return new ProbabilityRandomizer(dice, i);
    }

    public int getRemovedLinesWithoutOverflown() {
        return removedLinesWithoutOverflown;
    }
}
