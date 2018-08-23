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
import com.codenjoy.dojo.tetris.model.levels.random.ProbabilityRandomizerTest;
import com.codenjoy.dojo.tetris.model.levels.random.Randomizer;
import com.codenjoy.dojo.tetris.model.levels.random.TrueRandomDice;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static com.codenjoy.dojo.tetris.model.levels.random.ProbabilityRandomizerTest.COUNT_ITERATIONS;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotSame;
import static org.fest.reflect.core.Reflection.field;
import static org.mockito.Mockito.mock;

public class ProbabilityLevelsTest {

    protected Levels levels;
    private static final int LINES_REMOVED_FOR_NEXT_LEVEL = 20;

    @Before
    public void setUp() {
        Dice dice = new TrueRandomDice();
        levels = new ProbabilityLevels(dice, new Figures());
        levels.setChangeLevelListener(mock(ChangeLevelListener.class));
    }

    @Test
    public void shouldNotSameProbabilitysLevelsRandomizersinDiffeentLevels(){
        GameLevel currentLevel = levels.getCurrentLevel();
        Randomizer randomizer = getRandomizer(currentLevel);

        GameLevel nextLevel = levels.getNextLevel();
        Randomizer nextLevelRandomizer = getRandomizer(nextLevel);

        assertNotSame(randomizer, nextLevelRandomizer);
    }

    private Randomizer getRandomizer(GameLevel level) {
        return field("randomizer").ofType(Randomizer.class).in(level).get();
    }

    @Test
    public void checkProbabilitiesOnProbabilityLevels(){
        assertAllProbabilities("{0=100.0}\n" +
                "{0=92.0, 1=8.0}\n" +
                "{0=74.5, 1=25.5}\n" +
                "{0=50.0, 1=50.0}\n" +
                "{0=47.33, 1=47.32, 2=5.34}\n" +
                "{0=41.5, 1=41.5, 2=16.99}\n" +
                "{0=33.33, 1=33.33, 2=33.33}\n" +
                "{0=32.0, 1=32.0, 2=32.0, 3=4.0}\n" +
                "{0=29.08, 1=29.08, 2=29.08, 3=12.75}\n" +
                "{0=25.0, 1=25.0, 2=25.0, 3=25.0}\n" +
                "{0=24.2, 1=24.2, 2=24.2, 3=24.2, 4=3.2}\n" +
                "{0=22.45, 1=22.45, 2=22.45, 3=22.45, 4=10.2}\n" +
                "{0=20.0, 1=20.0, 2=20.0, 3=20.0, 4=20.0}\n" +
                "{0=19.46, 1=19.46, 2=19.46, 3=19.46, 4=19.46, 5=2.66}\n" +
                "{0=18.3, 1=18.3, 2=18.29, 3=18.29, 4=18.3, 5=8.5}\n" +
                "{0=16.66, 1=16.66, 2=16.66, 3=16.66, 4=16.66, 5=16.66}\n" +
                "{0=16.28, 1=16.28, 2=16.28, 3=16.28, 4=16.28, 5=16.28, 6=2.28}\n" +
                "{0=15.45, 1=15.45, 2=15.45, 3=15.45, 4=15.45, 5=15.45, 6=7.29}\n" +
                "{0=14.28, 1=14.28, 2=14.28, 3=14.28, 4=14.28, 5=14.28, 6=14.28}\n" +
                "{0=14.28, 1=14.28, 2=14.28, 3=14.28, 4=14.28, 5=14.28, 6=14.28}");
    }

    private void assertAllProbabilities(String expected) {
        List<String> actuals = new LinkedList<>();
        for (int i = 0; i < 20; i++) {
            actuals.add(calculateFiguresProbabilities());
            levels.linesRemoved(LINES_REMOVED_FOR_NEXT_LEVEL);
        }
        assertEquals(expected, String.join("\n", actuals));
    }

    private String calculateFiguresProbabilities() {
        GameLevel currentLevel = levels.getCurrentLevel();

        Randomizer randomizer = getRandomizer(currentLevel);
        Type[] figures = EasyLevelsTest.getOpenFigures(currentLevel.queue());
        int openFigures = figures.length;

        return ProbabilityRandomizerTest.calculateFiguresProbabilities(randomizer, openFigures, COUNT_ITERATIONS).toString();
    }

    @Test
    public void shouldNextLevelWhenExpectedLinesRemoved() {
        assertEquals(0, levels.getCurrentLevelNumber());

        levels.linesRemoved(LINES_REMOVED_FOR_NEXT_LEVEL);

        assertEquals(1, levels.getCurrentLevelNumber());
    }
}
