package net.tetris.services;

import net.tetris.dom.GameLevel;
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
    private GameLevel level;

    @Before
    public void setUp() throws Exception {
        playerScores = new PlayerScores(0);
        playerScores.levelChanged(level);
    }

    @Test
    public void shouldCalcScoreWhen1LineRemoved(){
        setFiguresToOpenCount(1);
        playerScores.linesRemoved(1);

        assertEquals(100, playerScores.getScore());
    }

    @Test
    public void shouldCalcScoreWhen1LineRemovedWithLevel(){
        setFiguresToOpenCount(2);
        playerScores.linesRemoved(1);

        assertEquals((1 + 1) * 100, playerScores.getScore());
    }

    @Test
    public void shouldCalcScoreWhen2LineRemoved(){
        setFiguresToOpenCount(2);
        playerScores.linesRemoved(2);

        assertEquals((1 + 1) * 300, playerScores.getScore());
    }

    @Test
    public void shouldCalcScoreWhen3LineRemoved(){
        setFiguresToOpenCount(3);
        playerScores.linesRemoved(3);

        assertEquals((2 + 1) * 700, playerScores.getScore());
    }

    @Test
    public void shouldCalcScoreWhen4LineRemoved() {
        setFiguresToOpenCount(4);
        playerScores.linesRemoved(4);

        assertEquals((3 + 1) * 1500, playerScores.getScore());
    }

    private OngoingStubbing<Integer> setFiguresToOpenCount(int FiguresToOpenCount) {
        return when(level.getFigureTypesToOpenCount()).thenReturn(FiguresToOpenCount);
    }

    @Test
    public void shouldAccumulateScoreWhenLineRemoved(){
        setFiguresToOpenCount(1);

        playerScores.linesRemoved(1);
        playerScores.linesRemoved(3);

        assertEquals(100 + 700, playerScores.getScore());
    }

    @Test
    public void shouldCalculateGlassOwerFlow(){
        setFiguresToOpenCount(1);

        playerScores.glassOverflown();

        assertEquals(-500, playerScores.getScore());
    }

    @Test
    public void shouldAccumulateGlassOwerFlow(){
        setFiguresToOpenCount(1);

        playerScores.linesRemoved(1);

        playerScores.glassOverflown();

        assertEquals(100 - 500, playerScores.getScore());
    }

    @Test
    public void shouldCalculateWhenFigureDropped(){
        setFiguresToOpenCount(1);

        playerScores.linesRemoved(1);

        playerScores.figureDropped(new TetrisFigure(0,0, "#"));

        assertEquals(100 + 10, playerScores.getScore());
    }

    @Test
    public void shouldCalculateWithBaseScore(){
        setFiguresToOpenCount(1);

        playerScores = new PlayerScores(-5000);
        playerScores.levelChanged(level);

        assertEquals(-5000, playerScores.getScore());

        playerScores.linesRemoved(1);

        assertEquals(-5000 + 100, playerScores.getScore());
    }



}
