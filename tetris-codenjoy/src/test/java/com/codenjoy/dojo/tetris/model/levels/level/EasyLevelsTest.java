package com.codenjoy.dojo.tetris.model.levels.level;

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
import com.codenjoy.dojo.tetris.model.*;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static com.codenjoy.dojo.tetris.model.Figure.Type.*;
import static junit.framework.Assert.assertEquals;
import static org.fest.reflect.core.Reflection.field;
import static org.mockito.Mockito.mock;

public class EasyLevelsTest {

    private EasyLevels levels;
    private static final int LINES_REMOVED_FOR_NEXT_LEVEL = 10;
    private Dice dice = mock(Dice.class);

    @Before
    public void setUp() {
        levels = new EasyLevels(dice, new PlayerFigures());
        levels.setChangeLevelListener(mock(ChangeLevelListener.class));
    }

    @Test
    public void checkFigures(){
        assertFigures(O);
        assertFigures(I);
        assertFigures(O, I);
        assertFigures(J);
        assertFigures(L);
        assertFigures(J, L);
        assertFigures(O, I, J, L);
        assertFigures(S);
        assertFigures(Z);
        assertFigures(S, Z);
        assertFigures(T);
        assertFigures(S, Z, T);
        assertFigures(O, I, J, L, S, Z, T);
    }

    private void assertFigures(Figure.Type... expected) {
        GameLevel currentLevel = levels.getCurrentLevel();

        Figure.Type[] openFigures = getOpenFigures(currentLevel.getFigureQueue());

        assertEquals(Arrays.toString(expected), Arrays.toString(openFigures));

        levels.linesRemoved(LINES_REMOVED_FOR_NEXT_LEVEL);
    }

    private Figure.Type[] getOpenFigures(FigureQueue queue) {
        return field("openFigures").ofType(Figure.Type[].class).in(queue).get();
    }
}
