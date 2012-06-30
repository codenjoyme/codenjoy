package net.tetris.services;

import net.tetris.dom.TetrisFigure;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;


public class PlayerScoresTest {

    private PlayerScores playerScores;

    @Before
    public void setUp() throws Exception {
        playerScores = new PlayerScores();
    }

    @Test
    public void shouldCalcScoreWhen1LineRemoved(){
        playerScores.linesRemoved(1);

        assertEquals(100, playerScores.getScore());
    }

    @Test
    public void shouldCalcScoreWhen2LineRemoved(){
        playerScores.linesRemoved(2);

        assertEquals(300, playerScores.getScore());
    }

    @Test
    public void shouldCalcScoreWhen3LineRemoved(){
        playerScores.linesRemoved(3);

        assertEquals(700, playerScores.getScore());
    }

    @Test
    public void shouldCalcScoreWhen4LineRemoved(){
        playerScores.linesRemoved(4);

        assertEquals(1500, playerScores.getScore());
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

        assertEquals(-2000, playerScores.getScore());
    }

    @Test
    public void shouldAccumulateGlassOwerFlow(){
        playerScores.linesRemoved(1);

        playerScores.glassOverflown();

        assertEquals(100 - 2000, playerScores.getScore());
    }

    @Test
    public void shouldCalculateWhenFigureDropped(){
        playerScores.linesRemoved(1);

        playerScores.figureDropped(new TetrisFigure(0,0, "#"));

        assertEquals(100 + 10, playerScores.getScore());
    }
}
