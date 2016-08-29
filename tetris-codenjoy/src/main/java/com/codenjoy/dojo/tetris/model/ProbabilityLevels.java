package com.codenjoy.dojo.tetris.model;

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


import com.codenjoy.dojo.tetris.model.GlassEvent;
import com.codenjoy.dojo.tetris.model.Levels;
import com.codenjoy.dojo.tetris.model.PlayerFigures;
import com.codenjoy.dojo.tetris.model.ProbabilityFigureTypesLevel;
import com.codenjoy.dojo.tetris.model.ProbabilityRandomizer;
import com.codenjoy.dojo.tetris.model.Randomizer;

import static com.codenjoy.dojo.tetris.model.Figure.Type.*;
import static com.codenjoy.dojo.tetris.model.GlassEvent.Type.TOTAL_LINES_REMOVED;


/**
 * User: oleksandr.baglai
 * Date: 9/23/12
 * Time: 3:18 PM
 */
public class ProbabilityLevels extends Levels {

    public ProbabilityLevels(PlayerFigures queue) {
        super(new ProbabilityFigureTypesLevel(queue,
                        new GlassEvent<>(TOTAL_LINES_REMOVED, 0),
                        lastFigureProbability(100),
                        O),
                // + I
                new ProbabilityFigureTypesLevel(queue,
                        new GlassEvent<>(TOTAL_LINES_REMOVED, 20),
                        lastFigureProbability(15),
                        O, I),

                new ProbabilityFigureTypesLevel(queue,
                        new GlassEvent<>(TOTAL_LINES_REMOVED, 40),
                        lastFigureProbability(50),
                        O, I),

                new ProbabilityFigureTypesLevel(queue,
                        new GlassEvent<>(TOTAL_LINES_REMOVED, 60),
                        lastFigureProbability(100),
                        O, I),

                // + J
                new ProbabilityFigureTypesLevel(queue,
                        new GlassEvent<>(TOTAL_LINES_REMOVED, 80),
                        lastFigureProbability(15),
                        O, I, J),


                new ProbabilityFigureTypesLevel(queue,
                        new GlassEvent<>(TOTAL_LINES_REMOVED, 100),
                        lastFigureProbability(50),
                        O, I, J),

                new ProbabilityFigureTypesLevel(queue,
                        new GlassEvent<>(TOTAL_LINES_REMOVED, 120),
                        lastFigureProbability(100),
                        O, I, J),

                // + L
                new ProbabilityFigureTypesLevel(queue,
                        new GlassEvent<>(TOTAL_LINES_REMOVED, 140),
                        lastFigureProbability(15),
                        O, I, J, L),


                new ProbabilityFigureTypesLevel(queue,
                        new GlassEvent<>(TOTAL_LINES_REMOVED, 160),
                        lastFigureProbability(50),
                        O, I, J, L),

                new ProbabilityFigureTypesLevel(queue,
                        new GlassEvent<>(TOTAL_LINES_REMOVED, 180),
                        lastFigureProbability(100),
                        O, I, J, L),

                // + S
                new ProbabilityFigureTypesLevel(queue,
                        new GlassEvent<>(TOTAL_LINES_REMOVED, 200),
                        lastFigureProbability(15),
                        O, I, J, L, S),

                new ProbabilityFigureTypesLevel(queue,
                        new GlassEvent<>(TOTAL_LINES_REMOVED, 220),
                        lastFigureProbability(50),
                        O, I, J, L, S),


                new ProbabilityFigureTypesLevel(queue,
                        new GlassEvent<>(TOTAL_LINES_REMOVED, 240),
                        lastFigureProbability(100),
                        O, I, J, L, S),

                // + Z
                new ProbabilityFigureTypesLevel(queue,
                        new GlassEvent<>(TOTAL_LINES_REMOVED, 260),
                        lastFigureProbability(15),
                        O, I, J, L, S, Z),

                new ProbabilityFigureTypesLevel(queue,
                        new GlassEvent<>(TOTAL_LINES_REMOVED, 280),
                        lastFigureProbability(50),
                        O, I, J, L, S, Z),

                new ProbabilityFigureTypesLevel(queue,
                        new GlassEvent<>(TOTAL_LINES_REMOVED, 300),
                        lastFigureProbability(100),
                        O, I, J, L, S, Z),

                // + T
                new ProbabilityFigureTypesLevel(queue,
                        new GlassEvent<>(TOTAL_LINES_REMOVED, 320),
                        lastFigureProbability(15),
                        O, I, J, L, S, Z, T),

                new ProbabilityFigureTypesLevel(queue,
                        new GlassEvent<>(TOTAL_LINES_REMOVED, 340),
                        lastFigureProbability(50),
                        O, I, J, L, S, Z, T),

                new ProbabilityFigureTypesLevel(queue,
                        new GlassEvent<>(TOTAL_LINES_REMOVED, 360),
                        lastFigureProbability(100),
                        O, I, J, L, S, Z, T));
    }

    public GlassEvent nextLevelAcceptedCriteriaOnLinesRemovedEvent(int amount) {
        return new GlassEvent<>(GlassEvent.Type.TOTAL_LINES_REMOVED, getTotalRemovedLines());
    }

    private static Randomizer lastFigureProbability(int i) {
        return new ProbabilityRandomizer(i);
    }
}
