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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.fest.reflect.core.Reflection.field;
import static org.mockito.Mockito.mock;

public class ProbabilityLevelsTest {

    protected Levels levels;
    private static final int LINES_REMOVED_FOR_NEXT_LEVEL = 20;

    @Before
    public void setUp() {
        Dice dice = new TrueRandomDice();
        levels = new ProbabilityLevels(dice, new Figures());
        levels.onChangeLevel(mock(ChangeLevelListener.class));
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
        assertAllProbabilities("{0=100}\n" +
                "{0=92, 1=8}\n" +
                "{0=75, 1=26}\n" +
                "{0=50, 1=50}\n" +
                "{0=47, 1=47, 2=5}\n" +
                "{0=42, 1=42, 2=17}\n" +
                "{0=33, 1=33, 2=33}\n" +
                "{0=32, 1=32, 2=32, 3=4}\n" +
                "{0=29, 1=29, 2=29, 3=13}\n" +
                "{0=25, 1=25, 2=25, 3=25}\n" +
                "{0=24, 1=24, 2=24, 3=24, 4=3}\n" +
                "{0=22, 1=22, 2=22, 3=22, 4=10}\n" +
                "{0=20, 1=20, 2=20, 3=20, 4=20}\n" +
                "{0=19, 1=19, 2=19, 3=19, 4=19, 5=3}\n" +
                "{0=18, 1=18, 2=18, 3=18, 4=18, 5=9}\n" +
                "{0=17, 1=17, 2=17, 3=17, 4=17, 5=17}\n" +
                "{0=16, 1=16, 2=16, 3=16, 4=16, 5=16, 6=2}\n" +
                "{0=15, 1=15, 2=15, 3=15, 4=15, 5=15, 6=7}\n" +
                "{0=14, 1=14, 2=14, 3=14, 4=14, 5=14, 6=14}\n" +
                "{0=14, 1=14, 2=14, 3=14, 4=14, 5=14, 6=14}");
    }

    private void assertAllProbabilities(String expected) {
        List<String> actuals = new LinkedList<>();
        for (int i = 0; i < 20; i++) {
            actuals.add(calculateFiguresProbabilities());
            levels.linesRemoved(LINES_REMOVED_FOR_NEXT_LEVEL);
            levels.tryGoNextLevel();
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
        levels.tryGoNextLevel();

        assertEquals(1, levels.getCurrentLevelNumber());
    }
}
