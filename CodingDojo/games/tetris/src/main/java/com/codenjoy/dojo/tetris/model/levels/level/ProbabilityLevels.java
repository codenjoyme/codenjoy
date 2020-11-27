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
import static com.codenjoy.dojo.tetris.model.GlassEvent.Type.TOTAL_LINES_REMOVED;


/**
 * User: oleksandr.baglai
 * Date: 9/23/12
 * Time: 3:18 PM
 */
public class ProbabilityLevels extends Levels {

    public ProbabilityLevels(Dice dice, FigureQueue queue) {
        super(new ProbabilityFigureTypesLevel(dice, queue,
                        new GlassEvent<>(TOTAL_LINES_REMOVED, 0),
                        lastFigureProbability(dice, 100),
                        O),
                // + I
                new ProbabilityFigureTypesLevel(dice, queue,
                        new GlassEvent<>(TOTAL_LINES_REMOVED, 20),
                        lastFigureProbability(dice, 15),
                        O, I),

                new ProbabilityFigureTypesLevel(dice, queue,
                        new GlassEvent<>(TOTAL_LINES_REMOVED, 40),
                        lastFigureProbability(dice, 50),
                        O, I),

                new ProbabilityFigureTypesLevel(dice, queue,
                        new GlassEvent<>(TOTAL_LINES_REMOVED, 60),
                        lastFigureProbability(dice, 100),
                        O, I),

                // + J
                new ProbabilityFigureTypesLevel(dice, queue,
                        new GlassEvent<>(TOTAL_LINES_REMOVED, 80),
                        lastFigureProbability(dice, 15),
                        O, I, J),


                new ProbabilityFigureTypesLevel(dice, queue,
                        new GlassEvent<>(TOTAL_LINES_REMOVED, 100),
                        lastFigureProbability(dice, 50),
                        O, I, J),

                new ProbabilityFigureTypesLevel(dice, queue,
                        new GlassEvent<>(TOTAL_LINES_REMOVED, 120),
                        lastFigureProbability(dice, 100),
                        O, I, J),

                // + L
                new ProbabilityFigureTypesLevel(dice, queue,
                        new GlassEvent<>(TOTAL_LINES_REMOVED, 140),
                        lastFigureProbability(dice, 15),
                        O, I, J, L),


                new ProbabilityFigureTypesLevel(dice, queue,
                        new GlassEvent<>(TOTAL_LINES_REMOVED, 160),
                        lastFigureProbability(dice, 50),
                        O, I, J, L),

                new ProbabilityFigureTypesLevel(dice, queue,
                        new GlassEvent<>(TOTAL_LINES_REMOVED, 180),
                        lastFigureProbability(dice, 100),
                        O, I, J, L),

                // + S
                new ProbabilityFigureTypesLevel(dice, queue,
                        new GlassEvent<>(TOTAL_LINES_REMOVED, 200),
                        lastFigureProbability(dice, 15),
                        O, I, J, L, S),

                new ProbabilityFigureTypesLevel(dice, queue,
                        new GlassEvent<>(TOTAL_LINES_REMOVED, 220),
                        lastFigureProbability(dice, 50),
                        O, I, J, L, S),


                new ProbabilityFigureTypesLevel(dice, queue,
                        new GlassEvent<>(TOTAL_LINES_REMOVED, 240),
                        lastFigureProbability(dice, 100),
                        O, I, J, L, S),

                // + Z
                new ProbabilityFigureTypesLevel(dice, queue,
                        new GlassEvent<>(TOTAL_LINES_REMOVED, 260),
                        lastFigureProbability(dice, 15),
                        O, I, J, L, S, Z),

                new ProbabilityFigureTypesLevel(dice, queue,
                        new GlassEvent<>(TOTAL_LINES_REMOVED, 280),
                        lastFigureProbability(dice, 50),
                        O, I, J, L, S, Z),

                new ProbabilityFigureTypesLevel(dice, queue,
                        new GlassEvent<>(TOTAL_LINES_REMOVED, 300),
                        lastFigureProbability(dice, 100),
                        O, I, J, L, S, Z),

                // + T
                new ProbabilityFigureTypesLevel(dice, queue,
                        new GlassEvent<>(TOTAL_LINES_REMOVED, 320),
                        lastFigureProbability(dice, 15),
                        O, I, J, L, S, Z, T),

                new ProbabilityFigureTypesLevel(dice, queue,
                        new GlassEvent<>(TOTAL_LINES_REMOVED, 340),
                        lastFigureProbability(dice, 50),
                        O, I, J, L, S, Z, T),

                new ProbabilityFigureTypesLevel(dice, queue,
                        new GlassEvent<>(TOTAL_LINES_REMOVED, 360),
                        lastFigureProbability(dice, 100),
                        O, I, J, L, S, Z, T));
    }

    public GlassEvent nextLevelAcceptedCriteriaOnLinesRemovedEvent(int amount) {
        return new GlassEvent<>(GlassEvent.Type.TOTAL_LINES_REMOVED, totalRemovedLines());
    }

    private static Randomizer lastFigureProbability(Dice dice, int i) {
        return new ProbabilityRandomizer(dice, i);
    }
}
