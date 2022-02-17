package com.codenjoy.dojo.collapse.model;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2012 - 2022 Codenjoy
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


import com.codenjoy.dojo.services.dice.MockDice;
import com.codenjoy.dojo.utils.TestUtils;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LevelBuilderTest {

    private MockDice dice;
    private LevelBuilder builder;
    private final int SIZE = 4;

    @Before
    public void setup() {
        dice = new MockDice();
    }

    @Test
    public void testGetBoard() {
        dice.whenThen(8,
                0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 2, 2, 2, 2, 3);
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