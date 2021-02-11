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
import com.codenjoy.dojo.tetris.model.FigureQueue;
import com.codenjoy.dojo.tetris.model.levels.gamelevel.FigureTypesLevel;
import com.codenjoy.dojo.tetris.model.GlassEvent;
import com.codenjoy.dojo.tetris.model.Levels;
import com.codenjoy.dojo.tetris.model.Figures;

import static com.codenjoy.dojo.tetris.model.Type.*;
import static com.codenjoy.dojo.tetris.model.GlassEvent.Type.TOTAL_LINES_REMOVED;

/**
 * User: oleksandr.baglai
 * Date: 9/23/12
 * Time: 3:18 PM
 */
public class EasyLevels extends Levels {

    public EasyLevels(Dice dice, FigureQueue queue) {
        super(new FigureTypesLevel(dice, queue,
                        new GlassEvent<>(TOTAL_LINES_REMOVED, 0),
                        O),

                new FigureTypesLevel(dice, queue,
                        new GlassEvent<>(TOTAL_LINES_REMOVED, 10),
                        I),

                new FigureTypesLevel(dice, queue,
                        new GlassEvent<>(TOTAL_LINES_REMOVED, 20),
                        O, I),

                new FigureTypesLevel(dice, queue,
                        new GlassEvent<>(TOTAL_LINES_REMOVED, 30),
                        J),

                new FigureTypesLevel(dice, queue,
                        new GlassEvent<>(TOTAL_LINES_REMOVED, 40),
                        L),

                new FigureTypesLevel(dice, queue,
                        new GlassEvent<>(TOTAL_LINES_REMOVED, 50),
                        J, L),

                new FigureTypesLevel(dice, queue,
                        new GlassEvent<>(TOTAL_LINES_REMOVED, 60),
                        O, I, J, L),

                new FigureTypesLevel(dice, queue,
                        new GlassEvent<>(TOTAL_LINES_REMOVED, 70),
                        S),

                new FigureTypesLevel(dice, queue,
                        new GlassEvent<>(TOTAL_LINES_REMOVED, 80),
                        Z),

                new FigureTypesLevel(dice, queue,
                        new GlassEvent<>(TOTAL_LINES_REMOVED, 90),
                        S, Z),

                new FigureTypesLevel(dice, queue,
                        new GlassEvent<>(TOTAL_LINES_REMOVED, 100),
                        T),

                new FigureTypesLevel(dice, queue,
                        new GlassEvent<>(TOTAL_LINES_REMOVED, 110),
                        S, Z, T),

                new FigureTypesLevel(dice, queue,
                        new GlassEvent<>(TOTAL_LINES_REMOVED, 120),
                        O, I, J, L,
                        S, Z, T));
    }

    public GlassEvent nextLevelAcceptedCriteriaOnLinesRemovedEvent(int amount) {
        return new GlassEvent<>(GlassEvent.Type.TOTAL_LINES_REMOVED, totalRemovedLines());
    }
}
