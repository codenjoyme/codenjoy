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


import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.tetris.model.levels.level.MockLevels;
import org.junit.Test;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class LevelsBuilderTest {

    private Levels levels;
    private Dice dice = mock(Dice.class);

    @Test
    public void shouldGetLevelsDataWhenCallLevelsReader() {
        levels = new MockLevels(dice, new Figures());

        gotoNextLevel();

        Levels.LevelsReader reader = levels.new LevelsReader();
        assertEquals(1, reader.getCurrentLevel());
        assertEquals(4, reader.getTotalRemovedLines());
        assertEquals("MockLevels", reader.getLevelsName());
    }

    @Test
    public void shouldSetLevelsPropertiesWhenBuildLevels() {
        Levels.LevelsBuilder builder = getBuilder(1, 4);

        levels = builder.levels();

        assertEquals(1, levels.getCurrentLevelNumber());
        assertEquals(4, levels.totalRemovedLines());
        assertEquals(MockLevels.class, levels.getClass());
    }

    @Test
    public void shouldLevelsHasOneQueueWhenBuildLevels() {
        Levels.LevelsBuilder builder = getBuilder(1, 4);

        levels = builder.levels();

        FigureQueue queue = levels.getCurrentLevel().queue();

        gotoNextLevel();

        assertEquals(2, levels.getCurrentLevelNumber());
        assertSame(queue, levels.getCurrentLevel().queue());
    }

    private Levels.LevelsBuilder getBuilder(int currentLevel, int totalRemovedLines) {
        Levels.LevelsBuilder builder = new Levels.LevelsBuilder(dice);
        builder.setCurrentLevel(currentLevel);
        builder.setLevelsName(MockLevels.class.getSimpleName());
        builder.setTotalRemovedLines(totalRemovedLines);
        return builder;
    }

    @Test
    public void shouldApplyCurrentLevelWhenBuildLevels() {
        Levels.LevelsBuilder builder = getBuilder(1, 4);

        levels = builder.levels();

        FigureQueue queue = levels.getCurrentLevel().queue();
        retrieveAllFiguresFromPreviousLevel(queue);
        assertEquals(Type.O, queue.next().type());
        assertEquals(Type.O, queue.next().type());
        assertEquals(Type.O, queue.next().type());
    }

    private void retrieveAllFiguresFromPreviousLevel(FigureQueue queue) {
        for (int i = 0; i < Figures.DEFAULT_FUTURE_COUNT; i++) {
            queue.next();
        }
    }

    private void gotoNextLevel() {
        levels.linesRemoved(MockLevels.LINES_REMOVED_FOR_NEXT_LEVEL);
        levels.tryGoNextLevel();
    }


}
