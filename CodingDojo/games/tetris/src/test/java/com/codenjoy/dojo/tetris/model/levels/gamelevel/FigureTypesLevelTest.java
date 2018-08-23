package com.codenjoy.dojo.tetris.model.levels.gamelevel;

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


import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.tetris.model.GlassEvent;
import com.codenjoy.dojo.tetris.model.Figures;
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
        Figures queue = mock(Figures.class);
        Dice dice = mock(Dice.class);

        assertEquals("Remove 4 lines together",
                new FigureTypesLevel(dice, queue, new GlassEvent(LINES_REMOVED, 4)).nextLevelCriteria());

        assertEquals("Remove 13 lines",
                new FigureTypesLevel(dice, queue, new GlassEvent(TOTAL_LINES_REMOVED, 13)).nextLevelCriteria());

        assertEquals("This is last level",
                new FigureTypesLevel(dice, queue, new GlassEvent(FIGURE_DROPPED, 0)).nextLevelCriteria());

        assertEquals("This is last level",
                new NullGameLevel().nextLevelCriteria());

        assertEquals("Remove 77 lines without overflown",
                new FigureTypesLevel(dice, queue, new GlassEvent(WITHOUT_OVERFLOWN_LINES_REMOVED, 77)).nextLevelCriteria());
    }
}
