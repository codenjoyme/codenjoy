package net.tetris.services.levels;

import net.tetris.dom.ChangeLevelListener;
import net.tetris.dom.Figure;
import net.tetris.dom.FigureQueue;
import net.tetris.dom.GameLevel;
import net.tetris.services.PlayerFigures;
import net.tetris.services.randomizer.ProbabilityRandomizerTest;
import net.tetris.services.randomizer.Randomizer;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotSame;
import static org.fest.reflect.core.Reflection.field;
import static org.mockito.Mockito.mock;

public class ProbabilityWithoutOverflownLevelsTest extends ProbabilityLevelsTest {

    @Before
    public void setUp() {
        levels = new ProbabilityWithoutOverflownLevels(new PlayerFigures());
        levels.setChangeLevelListener(mock(ChangeLevelListener.class));
    }

    @Test
    public void shouldClearLinesRemovedCounterWhenOverflown() {
        assertEquals(0, levels.getCurrentLevelNumber());

        levels.linesRemoved(10);

        levels.glassOverflown();

        levels.linesRemoved(10);

        assertEquals(0, levels.getCurrentLevelNumber());

        levels.linesRemoved(10);

        assertEquals(1, levels.getCurrentLevelNumber());
    }
}
