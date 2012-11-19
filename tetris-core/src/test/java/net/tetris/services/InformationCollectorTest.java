package net.tetris.services;

import net.tetris.dom.Figure;
import net.tetris.dom.GameLevel;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

/**
 * User: oleksandr.baglai
 * Date: 11/13/12
 * Time: 2:06 AM
 */
public class InformationCollectorTest {

    private PlayerScores playerScores;
    private InformationCollector collector;

    @Before
    public void setup() {
        playerScores = mock(PlayerScores.class);
        collector = new InformationCollector(playerScores);
    }

    @Test
     public void shouldFIFOQueue() {
        when(playerScores.getScore()).thenReturn(0).thenReturn(1).thenReturn(0).thenReturn(2).thenReturn(0).thenReturn(3);

        collector.glassOverflown();
        collector.linesRemoved(1);
        collector.figureDropped(null);
        collector.levelChanged(4 - 1, null);

        assertEquals("+1, +2, +3, Level 4", collector.getMessage());
        assertNull(collector.getMessage());
    }

    @Test
    public void shouldFIFOQueueButWhenPresentInformationAboutLevelChangedThenReturnItLast() {
        when(playerScores.getScore()).thenReturn(0).thenReturn(1).thenReturn(0).thenReturn(2).thenReturn(0).thenReturn(3);

        collector.glassOverflown();
        collector.levelChanged(4 - 1, null);
        collector.glassOverflown();
        collector.glassOverflown();

        assertEquals("+1, +2, +3, Level 4", collector.getMessage());
        assertNull(collector.getMessage());
    }

    @Test
    public void shouldCallPlayerScoreWhenGlassOverflown() {
        collector.glassOverflown();

        verify(playerScores).glassOverflown();
    }

    @Test
    public void shouldCallPlayerScoreWhenFigureDropped() {
        Figure mock = mock(Figure.class);
        collector.figureDropped(mock);

        verify(playerScores).figureDropped(mock);
    }

    @Test
    public void shouldCallPlayerScoreWhenLevelChanged() {
        GameLevel mock = mock(GameLevel.class);
        collector.levelChanged(14, mock);

        verify(playerScores).levelChanged(14, mock);
    }

    @Test
    public void shouldCallPlayerScoreWhenLinesRemoved() {
        collector.linesRemoved(13);

        verify(playerScores).linesRemoved(13);
    }

    @Test
    public void shouldClearOldInfoWhenSetNew() {
        collector.linesRemoved(10);
        collector.linesRemoved(10);

        collector.setInfo("qwe");
        assertEquals("qwe", collector.getMessage());
    }


}
