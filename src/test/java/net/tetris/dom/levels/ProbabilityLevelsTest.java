package net.tetris.dom.levels;

import net.tetris.dom.*;
import net.tetris.services.randomizer.ProbabilityRandomizerTest;
import net.tetris.services.randomizer.Randomizer;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotSame;
import static junit.framework.Assert.fail;
import static org.fest.reflect.core.Reflection.field;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ProbabilityLevelsTest {

    private ProbabilityLevels levels;
    private static final int LINES_REMOVED_FOR_NEXT_LEVEL = 20;

    @Before
    public void setUp() {
        levels = new ProbabilityLevels();
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
        assertProbabilities("{0=100.0}");
        assertProbabilities("{0=91.0, 1=8.0}");
        assertProbabilities("{0=74.0, 1=25.0}");
        assertProbabilities("{0=50.0, 1=49.0}");
        assertProbabilities("{0=47.0, 1=47.0, 2=5.0}");
        assertProbabilities("{0=41.0, 1=41.0, 2=17.0}");
        assertProbabilities("{0=33.0, 1=33.0, 2=33.0}");
        assertProbabilities("{0=31.0, 1=31.0, 2=32.0, 3=3.0}");
        assertProbabilities("{0=29.0, 1=29.0, 2=29.0, 3=12.0}");
        assertProbabilities("{0=24.0, 1=25.0, 2=25.0, 3=24.0}");
        assertProbabilities("{0=24.0, 1=24.0, 2=24.0, 3=24.0, 4=3.0}");
        assertProbabilities("{0=22.0, 1=22.0, 2=22.0, 3=22.0, 4=10.0}");
        assertProbabilities("{0=19.0, 1=19.0, 2=20.0, 3=20.0, 4=19.0}");
        assertProbabilities("{0=19.0, 1=19.0, 2=19.0, 3=19.0, 4=19.0, 5=2.0}");
        assertProbabilities("{0=18.0, 1=18.0, 2=18.0, 3=18.0, 4=18.0, 5=8.0}");
        assertProbabilities("{0=16.0, 1=16.0, 2=16.0, 3=16.0, 4=16.0, 5=16.0}");
        assertProbabilities("{0=16.0, 1=16.0, 2=16.0, 3=16.0, 4=16.0, 5=16.0, 6=2.0}");
        assertProbabilities("{0=15.0, 1=15.0, 2=15.0, 3=15.0, 4=15.0, 5=15.0, 6=7.0}");
        assertProbabilities("{0=14.0, 1=14.0, 2=14.0, 3=14.0, 4=14.0, 5=14.0, 6=14.0}");
        assertProbabilities("{0=14.0, 1=14.0, 2=14.0, 3=14.0, 4=14.0, 5=14.0, 6=14.0}");
    }

    private void assertProbabilities(String expected) {
        String s;
        int count = 0;
        int MAX_CYCLE_COUNT = 100;
        do {
            count ++;
            s = calculateFiguresProbabilities();
        } while (!expected.equals(s) && count < MAX_CYCLE_COUNT);
        if (count >= MAX_CYCLE_COUNT){
            assertEquals(expected, s);
        }
        levels.linesRemoved(LINES_REMOVED_FOR_NEXT_LEVEL);
    }

    private String calculateFiguresProbabilities() {
        GameLevel currentLevel = levels.getCurrentLevel();

        Randomizer randomizer = getRandomizer(currentLevel);
        int openFigures = getOpenFiguresLength(currentLevel.getFigureQueue());

        return ProbabilityRandomizerTest.calculateFiguresProbabilities(randomizer, openFigures, 100000).toString();
    }

    private int getOpenFiguresLength(FigureQueue queue) {
        return field("openFigures").ofType(Figure.Type[].class).in(queue).get().length;
    }

    @Test
    public void shouldNextLevelWhenExpectedLinesRemoved() {
        assertEquals(0, levels.getCurrentLevelNumber());

        levels.linesRemoved(20);

        assertEquals(1, levels.getCurrentLevelNumber());
    }
}
