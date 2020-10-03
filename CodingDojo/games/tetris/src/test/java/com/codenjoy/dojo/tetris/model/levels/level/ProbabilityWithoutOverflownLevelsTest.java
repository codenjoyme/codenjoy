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
import com.codenjoy.dojo.tetris.model.ChangeLevelListener;
import com.codenjoy.dojo.tetris.model.Figures;
import com.codenjoy.dojo.tetris.model.levels.random.TrueRandomDice;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class ProbabilityWithoutOverflownLevelsTest extends ProbabilityLevelsTest {

    @Before
    public void setUp() {
        Dice dice = new TrueRandomDice();
        levels = new ProbabilityWithoutOverflownLevels(dice, new Figures());
        levels.onChangeLevel(mock(ChangeLevelListener.class));
    }

    @Test
    public void shouldClearLinesRemovedCounterWhenOverflown() {
        assertEquals(0, levels.getCurrentLevelNumber());

        levels.linesRemoved(10);

        levels.glassOverflown();

        levels.linesRemoved(10);
        assertLinesRemovedWithoutOverflown(10);
    }

    @Test
    public void shouldChangeLevelAfterOverflown() {
        assertEquals(0, levels.getCurrentLevelNumber());

        levels.linesRemoved(10);
        levels.tryGoNextLevel();

        levels.glassOverflown();
        levels.tryGoNextLevel();

        levels.linesRemoved(10);
        levels.tryGoNextLevel();

        assertEquals(0, levels.getCurrentLevelNumber());

        levels.linesRemoved(10);
        levels.tryGoNextLevel();

        assertEquals(1, levels.getCurrentLevelNumber());
    }

    @Test
    public void shouldClearLinesRemovedCounterWhenLevelChanged() {
        levels.linesRemoved(20);
        levels.tryGoNextLevel();

        assertEquals(1, levels.getCurrentLevelNumber());
        assertLinesRemovedWithoutOverflown(0);

        levels.linesRemoved(10);
        levels.tryGoNextLevel();

        assertLinesRemovedWithoutOverflown(10);
    }

    private void assertLinesRemovedWithoutOverflown(int expected) {
        assertEquals(expected, ((ProbabilityWithoutOverflownLevels) levels).getRemovedLinesWithoutOverflown());
    }
}
