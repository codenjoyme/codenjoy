package net.tetris.levels;

import net.tetris.dom.Figure;
import net.tetris.dom.FigureQueue;
import net.tetris.dom.GameLevel;
import net.tetris.services.PlayerFigures;
import net.tetris.services.randomizer.EquiprobableRandomizer;
import net.tetris.services.randomizer.LikelihoodRandomizer;
import net.tetris.services.randomizer.LikelihoodRandomizerTest;
import net.tetris.services.randomizer.Randomizer;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotSame;
import static junit.framework.Assert.fail;
import static org.fest.reflect.core.Reflection.field;
import static org.fest.reflect.core.Reflection.property;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LikelihoodLevelsTest {

    private LikelihoodLevels levels = new LikelihoodLevels();

    @Test
    public void shouldNotSameLikelihoodsLevelsRandomizersinDiffeentLevels(){
        FigureQueue queue = levels.getCurrentLevel().getFigureQueue();
        Randomizer randomizer = getRandomizer(queue);

        FigureQueue nextLevelQueue = levels.getNextLevel().getFigureQueue();
        Randomizer nextLevelRandomizer = getRandomizer(nextLevelQueue);

        assertNotSame(randomizer, nextLevelRandomizer);
    }

    private Randomizer getRandomizer(FigureQueue queue) {
        return field("randomizer").ofType(Randomizer.class).in(queue).get();
    }

    @Test
    public void checkLikelihoodsOnLikelihoodLevels(){
        assertLikelihoods("{0=100.0}");

        levels.linesRemoved(10, 10);
        assertLikelihoods("{0=91.0, 1=8.0}");

        levels.linesRemoved(20, 10);
        assertLikelihoods("{0=74.0, 1=25.0}");

        levels.linesRemoved(30, 10);
        assertLikelihoods("{0=50.0, 1=49.0}");

        levels.linesRemoved(40, 10);
        assertLikelihoods("{0=47.0, 1=47.0, 2=5.0}");

        levels.linesRemoved(50, 10);
        assertLikelihoods("{0=41.0, 1=41.0, 2=17.0}");

        levels.linesRemoved(60, 10);
        assertLikelihoods("{0=33.0, 1=33.0, 2=33.0}");

        levels.linesRemoved(70, 10);
        assertLikelihoods("{0=31.0, 1=31.0, 2=32.0, 3=3.0}");

        levels.linesRemoved(80, 10);
        assertLikelihoods("{0=29.0, 1=29.0, 2=29.0, 3=12.0}");

        levels.linesRemoved(90, 10);
        assertLikelihoods("{0=24.0, 1=25.0, 2=25.0, 3=24.0}");

        levels.linesRemoved(100, 10);
        assertLikelihoods("{0=24.0, 1=24.0, 2=24.0, 3=24.0, 4=3.0}");

        levels.linesRemoved(110, 10);
        assertLikelihoods("{0=22.0, 1=22.0, 2=22.0, 3=22.0, 4=10.0}");

        levels.linesRemoved(120, 10);
        assertLikelihoods("{0=19.0, 1=19.0, 2=20.0, 3=20.0, 4=19.0}");

        levels.linesRemoved(130, 10);
        assertLikelihoods("{0=19.0, 1=19.0, 2=19.0, 3=19.0, 4=19.0, 5=2.0}");

        levels.linesRemoved(140, 10);
        assertLikelihoods("{0=18.0, 1=18.0, 2=18.0, 3=18.0, 4=18.0, 5=8.0}");

        levels.linesRemoved(150, 10);
        assertLikelihoods("{0=16.0, 1=16.0, 2=16.0, 3=16.0, 4=16.0, 5=16.0}");

        levels.linesRemoved(160, 10);
        assertLikelihoods("{0=16.0, 1=16.0, 2=16.0, 3=16.0, 4=16.0, 5=16.0, 6=2.0}");

        levels.linesRemoved(170, 10);
        assertLikelihoods("{0=15.0, 1=15.0, 2=15.0, 3=15.0, 4=15.0, 5=15.0, 6=7.0}");

        levels.linesRemoved(180, 10);
        assertLikelihoods("{0=14.0, 1=14.0, 2=14.0, 3=14.0, 4=14.0, 5=14.0, 6=14.0}");

        levels.linesRemoved(190, 10);
        assertLikelihoods("{0=14.0, 1=14.0, 2=14.0, 3=14.0, 4=14.0, 5=14.0, 6=14.0}");
    }

    private void assertLikelihoods(String expected) {
        String s;
        int count = 0;
        int MAX_CYCLE_COUNT = 100;
        do {
            count ++;
            s = calculateFiguresLikelihoods();
        } while (!expected.equals(s) && count < MAX_CYCLE_COUNT);
        if (count >= MAX_CYCLE_COUNT){
            assertEquals(expected, s);
        }
    }

    private String calculateFiguresLikelihoods() {
        FigureQueue queue = levels.getCurrentLevel().getFigureQueue();

        Randomizer randomizer = getRandomizer(queue);
        int openFigures = getOpenFiguresLength(queue);

        return LikelihoodRandomizerTest.calculateFiguresLikelihoods(randomizer, openFigures, 100000).toString();
    }

    private int getOpenFiguresLength(FigureQueue queue) {
        return field("openFigures").ofType(Figure.Type[].class).in(queue).get().length;
    }
}
