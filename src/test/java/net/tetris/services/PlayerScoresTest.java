package net.tetris.services;

import net.tetris.dom.Levels;
import net.tetris.dom.TetrisFigure;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.OngoingStubbing;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class PlayerScoresTest {

    private PlayerScores playerScores;

    @Mock
    private Levels levels;

    @Before
    public void setUp() throws Exception {
        playerScores = new PlayerScores(levels);
    }

    @Test
    public void shouldCalcScoreWhen1LineRemoved(){
        setCurrentLevel(0);
        playerScores.linesRemoved(1);

        assertEquals(100, playerScores.getScore());
    }

    @Test
    public void shouldCalcScoreWhen1LineRemovedWithLevel(){
        setCurrentLevel(1);
        playerScores.linesRemoved(1);

        assertEquals((1 + 1) * 100, playerScores.getScore());
    }

    @Test
    public void shouldCalcScoreWhen2LineRemoved(){
        setCurrentLevel(1);
        playerScores.linesRemoved(2);

        assertEquals((1 + 1) * 300, playerScores.getScore());
    }

    @Test
    public void shouldCalcScoreWhen3LineRemoved(){
        setCurrentLevel(2);
        playerScores.linesRemoved(3);

        assertEquals((2 + 1) * 700, playerScores.getScore());
    }

    @Test
    public void shouldCalcScoreWhen4LineRemoved() {
        setCurrentLevel(3);
        playerScores.linesRemoved(4);

        assertEquals((3 + 1) * 1500, playerScores.getScore());
    }

    private OngoingStubbing<Integer> setCurrentLevel(int currentLevel) {
        return when(levels.getCurrentLevel()).thenReturn(currentLevel);
    }

    @Test
    public void shouldAccumulateScoreWhenLineRemoved(){
        playerScores.linesRemoved(1);
        playerScores.linesRemoved(3);

        assertEquals(100 + 700, playerScores.getScore());
    }

    @Test
    public void shouldCalculateGlassOwerFlow(){
        playerScores.glassOverflown();

        assertEquals(-500, playerScores.getScore());
    }

    @Test
    public void shouldAccumulateGlassOwerFlow(){
        playerScores.linesRemoved(1);

        playerScores.glassOverflown();

        assertEquals(100 - 500, playerScores.getScore());
    }

    @Test
    public void shouldCalculateWhenFigureDropped(){
        playerScores.linesRemoved(1);

        playerScores.figureDropped(new TetrisFigure(0,0, "#"));

        assertEquals(100 + 10, playerScores.getScore());
    }


}
