package com.codenjoy.dojo.tetris.model;

import com.codenjoy.dojo.tetris.model.FigureTypesLevel;
import com.codenjoy.dojo.tetris.model.GlassEvent;
import com.codenjoy.dojo.tetris.model.NullGameLevel;
import com.codenjoy.dojo.tetris.model.PlayerFigures;
import org.junit.Test;

import static com.codenjoy.dojo.tetris.model.GlassEvent.Type.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

/**
 * User: oleksandr.baglai
 * Date: 9/29/12
 * Time: 6:25 PM
 */
public class FigureTypesLevelTest {

    @Test
    public void validateNextLevelIngoingCriteria() {
        PlayerFigures queue = mock(PlayerFigures.class);

        assertEquals("Remove 4 lines together",
                new FigureTypesLevel(queue, new GlassEvent(LINES_REMOVED, 4)).getNextLevelIngoingCriteria());

        assertEquals("Remove 13 lines",
                new FigureTypesLevel(queue, new GlassEvent(TOTAL_LINES_REMOVED, 13)).getNextLevelIngoingCriteria());

        assertEquals("This is last level",
                new FigureTypesLevel(queue, new GlassEvent(FIGURE_DROPPED, 0)).getNextLevelIngoingCriteria());

        assertEquals("This is last level",
                new NullGameLevel().getNextLevelIngoingCriteria());

        assertEquals("Remove 77 lines without overflown",
                new FigureTypesLevel(queue, new GlassEvent(WITHOUT_OVERFLOWN_LINES_REMOVED, 77)).getNextLevelIngoingCriteria());
    }
}
