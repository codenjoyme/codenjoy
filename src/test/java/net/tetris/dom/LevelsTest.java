package net.tetris.dom;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class LevelsTest {
    @Mock
    private GameLevel level0;
    @Mock
    private GameLevel level1;
    @Captor
    private ArgumentCaptor<GlassEvent> eventCaptor;

    @Mock
    private GameLevel level2;
    private Levels levels;
    @Mock
    private ChangeLevelListener levelChangedListener;

    @Before
    public void setUp() throws Exception {
        levels = new Levels(level0, level1, level2);
        levels.setChangeLevelListener(levelChangedListener);
    }

    @Test
    public void shouldCallChangeEventListenerWhenLevelChanged() {
        acceptLevels(true, false);

        levels.figureDropped(new TetrisFigure());

        verify(levelChangedListener).levelChanged(level1);
        verifyNoMoreInteractions(levelChangedListener);
    }

    @Test
    public void shouldApplyNextLevelWhenAccepted() {
        acceptLevels(true, false);

        levels.glassOverflown();
        levels.figureDropped(new TetrisFigure());

        verify(level1, times(1)).apply();
        verify(level2, never()).apply();
    }

    @Test
    public void shouldApplyFirstLevelAutomatically() {
        acceptLevels(false, false);

        verify(level0, times(1)).apply();
        verify(level1, never()).apply();
        verify(level2, never()).apply();
    }

    @Test
    public void shouldPassEventWhenAccept() {
        acceptLevels(false, false);

        levels.glassOverflown();
        TetrisFigure droppedFigure = new TetrisFigure();
        levels.figureDropped(droppedFigure);
        levels.linesRemoved(123, 12);

        verify(level1, times(4)).accept(eventCaptor.capture());
        assertEventValues(eventCaptor.getAllValues().get(0), GlassEvent.Type.GLASS_OVERFLOW, null);
        assertEventValues(eventCaptor.getAllValues().get(1), GlassEvent.Type.FIGURE_DROPPED, droppedFigure);
        assertEventValues(eventCaptor.getAllValues().get(2), GlassEvent.Type.TOTAL_LINES_REMOVED, 123);
        assertEventValues(eventCaptor.getAllValues().get(3), GlassEvent.Type.LINES_REMOVED, 12);
    }

    @Test
    public void shouldApplyNextLevelWhenAcceptedFirst() {
        acceptLevels(true, false);
        levels.glassOverflown();
        acceptLevel(level2, true);

        levels.linesRemoved(1, 1);

        verify(level2, times(1)).apply();
    }

    @Test
    public void shouldNotApplyLevelWhenLevelIsApplied() {
        acceptLevels(true, false);
        levels.glassOverflown();
        acceptLevels(true, true);

        levels.glassOverflown();

        verify(level1, times(1)).accept(Matchers.<GlassEvent>anyObject());
        verify(level1, times(1)).apply();
        verify(level2, times(1)).apply();
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenNoInitialLevel() {
        new Levels();
    }

    @Test
    public void shouldStayOnLastLevelWhenNoMoreLevels(){
        acceptLevels(true, true);
        levels.linesRemoved(1, 1);
        levels.figureDropped(new TetrisFigure());

        levels.glassOverflown();

        verify(level2, times(1)).accept(Matchers.<GlassEvent>anyObject());
        verify(level2, times(1)).apply();
    }

    @Test
    public void shouldApplyNextLevelWhenTotalLinesRemovedGlassEvent() {
        acceptLevel(level1, true);

        levels.linesRemoved(12, 1);

        verify(level1, times(1)).accept(eventCaptor.capture());
        assertEventValues(eventCaptor.getAllValues().get(0), GlassEvent.Type.TOTAL_LINES_REMOVED, 12);

        verify(level1, times(1)).apply();

        verifyNoMoreInteractions(level1, level2);
    }

    private void assertEventValues(GlassEvent event, GlassEvent.Type eventType, Object value) {
        assertEquals(eventType, event.getType());
        assertEquals(value, event.getValue());
    }

    private void acceptLevels(boolean acceptLevel1, boolean acceptLevel2) {
        acceptLevel(level0, acceptLevel1);
        acceptLevel(level1, acceptLevel1);
        acceptLevel(level2, acceptLevel2);
    }

    private void acceptLevel(GameLevel level, boolean accept) {
        when(level.accept(Matchers.<GlassEvent>anyObject())).thenReturn(accept);
    }

}
