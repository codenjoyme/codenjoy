package com.codenjoy.dojo.collapse.model;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 Codenjoy
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
import com.codenjoy.dojo.utils.TestUtils;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LevelBuilderTest {

    private Dice dice;
    private LevelBuilder builder;
    private final int SIZE = 4;

    @Before
    public void setup() {
        dice = mock(Dice.class);
    }

    @Test
    public void testGetBoard() throws Exception {
        when(dice.next(8)).thenReturn(0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 2, 2, 2, 2, 3);
        builder = new LevelBuilder(dice, SIZE);

        assertB("☼☼☼☼" +
                "☼12☼" +
                "☼34☼" +
                "☼☼☼☼");
    }

    private void assertB(String expected) {
        assertEquals(TestUtils.injectN(expected), TestUtils.injectN(builder.getBoard()));
    }
}
