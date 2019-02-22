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


import org.junit.Test;

import static com.codenjoy.dojo.tetris.model.GlassEvent.Type.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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
