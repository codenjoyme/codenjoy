package net.tetris.services.levels;

import net.tetris.dom.ChangeLevelListener;
import net.tetris.services.PlayerFigures;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotSame;
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
        assertLinesRemovedWithoutOverflown(10);
    }

    @Test
    public void shouldChangeLevelAfterOverflown() {
        assertEquals(0, levels.getCurrentLevelNumber());

        levels.linesRemoved(10);

        levels.glassOverflown();

        levels.linesRemoved(10);

        assertEquals(0, levels.getCurrentLevelNumber());

        levels.linesRemoved(10);

        assertEquals(1, levels.getCurrentLevelNumber());
    }

    @Test
    public void shouldClearLinesRemovedCounterWhenLevelChanged() {
        levels.linesRemoved(20);

        assertEquals(1, levels.getCurrentLevelNumber());
        assertLinesRemovedWithoutOverflown(0);

        levels.linesRemoved(10);

        assertLinesRemovedWithoutOverflown(10);
    }

    private void assertLinesRemovedWithoutOverflown(int expected) {
        assertEquals(expected, ((ProbabilityWithoutOverflownLevels) levels).getRemovedLinesWithoutOverflown());
    }
}
