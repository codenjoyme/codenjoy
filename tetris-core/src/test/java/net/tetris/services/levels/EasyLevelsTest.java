package net.tetris.services.levels;

import net.tetris.dom.ChangeLevelListener;
import static net.tetris.dom.Figure.Type.*;

import net.tetris.dom.Figure;
import net.tetris.dom.FigureQueue;
import net.tetris.dom.GameLevel;
import net.tetris.services.PlayerFigures;
import net.tetris.services.randomizer.ProbabilityRandomizerTest;
import net.tetris.services.randomizer.Randomizer;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotSame;
import static org.fest.reflect.core.Reflection.field;
import static org.mockito.Mockito.mock;

public class EasyLevelsTest {

    private EasyLevels levels;
    private static final int LINES_REMOVED_FOR_NEXT_LEVEL = 10;

    @Before
    public void setUp() {
        levels = new EasyLevels(new PlayerFigures());
        levels.setChangeLevelListener(mock(ChangeLevelListener.class));
    }

    @Test
    public void checkFigures(){
        assertFigures(O);
        assertFigures(I);
        assertFigures(O, I);
        assertFigures(J);
        assertFigures(L);
        assertFigures(J, L);
        assertFigures(O, I, J, L);
        assertFigures(S);
        assertFigures(Z);
        assertFigures(S, Z);
        assertFigures(T);
        assertFigures(S, Z, T);
        assertFigures(O, I, J, L, S, Z, T);
    }

    private void assertFigures(Figure.Type... expected) {
        GameLevel currentLevel = levels.getCurrentLevel();

        Figure.Type[] openFigures = getOpenFigures(currentLevel.getFigureQueue());

        assertEquals(Arrays.toString(expected), Arrays.toString(openFigures));

        levels.linesRemoved(LINES_REMOVED_FOR_NEXT_LEVEL);
    }

    private Figure.Type[] getOpenFigures(FigureQueue queue) {
        return field("openFigures").ofType(Figure.Type[].class).in(queue).get();
    }
}
