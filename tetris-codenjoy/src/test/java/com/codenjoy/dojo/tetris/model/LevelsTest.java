package com.codenjoy.dojo.tetris.model;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 Codenjoy
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */


import org.junit.Before;
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
        verify(levelChangedListener).levelChanged(0, level0);
        acceptLevels(true, false);

        levels.figureDropped(new FigureImpl());

        verify(levelChangedListener).levelChanged(1, level1);
        verifyNoMoreInteractions(levelChangedListener);
    }

    @Test
    public void shouldApplyNextLevelWhenAccepted() {
        acceptLevels(true, false);

        levels.glassOverflown();
        levels.figureDropped(new FigureImpl());

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
        FigureImpl droppedFigure = new FigureImpl();
        levels.figureDropped(droppedFigure);
        levels.linesRemoved(12);

        verify(level1, times(3)).accept(eventCaptor.capture());
        assertEventValues(eventCaptor.getAllValues().get(0), GlassEvent.Type.GLASS_OVERFLOW, null);
        assertEventValues(eventCaptor.getAllValues().get(1), GlassEvent.Type.FIGURE_DROPPED, droppedFigure);
        assertEventValues(eventCaptor.getAllValues().get(2), GlassEvent.Type.LINES_REMOVED, 12);
    }

    @Test
    public void shouldIncreaseTotalLinesRemovedWhenLinesRemoved() {
        levels.linesRemoved(12);
        levels.linesRemoved(21);

        assertEquals(33, levels.getTotalRemovedLines());
    }

    @Test
    public void shouldUseClientNextLevelAcceptCriteriaWhenLinesRemoved() {
        class MyLevels extends Levels {
            public MyLevels(GameLevel... levels) {
                super(levels);
            }

            protected GlassEvent nextLevelAcceptedCriteriaOnLinesRemovedEvent(int amount) {
                return new GlassEvent<>(GlassEvent.Type.TOTAL_LINES_REMOVED, amount);
            }
        }

        levels = new MyLevels(level0, level1, level2) {};
        levels.setChangeLevelListener(levelChangedListener);

        levels.linesRemoved(2);

        verify(level1, times(1)).accept(eventCaptor.capture());
        assertEventValues(eventCaptor.getAllValues().get(0),
                GlassEvent.Type.TOTAL_LINES_REMOVED, 2);
    }

    @Test
    public void shouldSetFirstLevelWhenSetListener() {
        verify(levelChangedListener).levelChanged(0, level0);
    }

    @Test
    public void shouldApplyNextLevelWhenAcceptedFirst() {
        acceptLevels(true, false);
        levels.glassOverflown();
        acceptLevel(level2, true);

        levels.linesRemoved(1);

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
        levels.linesRemoved(1);
        levels.figureDropped(new FigureImpl());

        levels.glassOverflown();

        verify(level2, times(1)).accept(Matchers.<GlassEvent>anyObject());
        verify(level2, times(1)).apply();
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

    private void gotoNextLevel() {
        acceptLevel(level1, true);
        levels.figureDropped(new FigureImpl());
        acceptLevel(level2, false);
    }

    @Test
    public void shouldDoNothingAtChangeLevelWhenNoEventListener() {
        verify(levelChangedListener).levelChanged(0, level0);
        acceptLevels(true, false);

        levels.setChangeLevelListener(null);
        levels.figureDropped(new FigureImpl());

        verifyNoMoreInteractions(levelChangedListener);
    }
}
