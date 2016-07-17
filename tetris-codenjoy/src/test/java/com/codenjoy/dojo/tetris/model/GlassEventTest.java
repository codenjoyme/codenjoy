package com.codenjoy.dojo.tetris.model;

import com.codenjoy.dojo.tetris.model.GlassEvent;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static com.codenjoy.dojo.tetris.model.GlassEvent.Type.*;

public class GlassEventTest {

    @Test
    public void shouldEqualsEventsWhenSameCountLinesRemovedInLinesRemovedEventType() {
        GlassEvent event = new GlassEvent(LINES_REMOVED, 4);

        assertTrue(event.equals(new GlassEvent(LINES_REMOVED, 4)));
    }

    @Test
    public void shouldNotEqualsEventsWhenNotSameCountLinesRemovedInLinesRemovedEventType() {
        GlassEvent event = new GlassEvent(LINES_REMOVED, 4);

        assertFalse(event.equals(new GlassEvent(LINES_REMOVED, 3)));
    }

    @Test
    public void shouldNotEqualsEventsWhenLessThanTotalCountLinesRemovedInTotalLinesRemovedEventType() {
        GlassEvent event = new GlassEvent(TOTAL_LINES_REMOVED, 4);

        assertFalse(event.equals(new GlassEvent(TOTAL_LINES_REMOVED, 3)));
    }

    @Test
    public void shouldEqualsEventsWhenMoreThanOrEqualsToTotalCountLinesRemovedInTotalLinesRemovedEventType() {
        GlassEvent event = new GlassEvent(TOTAL_LINES_REMOVED, 4);

        assertTrue(event.equals(new GlassEvent(TOTAL_LINES_REMOVED, 4)));
        assertTrue(event.equals(new GlassEvent(TOTAL_LINES_REMOVED, 15)));
    }

    @Test
    public void shouldNotEqualsEventsWhenDifferentEvents() {
        GlassEvent event = new GlassEvent(LINES_REMOVED, 4);

        assertFalse(event.equals(new GlassEvent(TOTAL_LINES_REMOVED, 4)));
    }

    @Test
    public void shouldNotEqualsEventsWhenLessThanTotalCountLinesRemovedInWithoutOverflownLinesRemovedEventType() {
        GlassEvent event = new GlassEvent(WITHOUT_OVERFLOWN_LINES_REMOVED, 10);

        assertFalse(event.equals(new GlassEvent(WITHOUT_OVERFLOWN_LINES_REMOVED, 9)));
    }

    @Test
    public void shouldEqualsEventsWhenMoreThanOrEqualsToTotalCountLinesRemovedInWithoutOverflownLinesRemovedEventType() {
        GlassEvent event = new GlassEvent(WITHOUT_OVERFLOWN_LINES_REMOVED, 10);

        assertTrue(event.equals(new GlassEvent(WITHOUT_OVERFLOWN_LINES_REMOVED, 10)));
        assertTrue(event.equals(new GlassEvent(WITHOUT_OVERFLOWN_LINES_REMOVED, 15)));
    }

}
