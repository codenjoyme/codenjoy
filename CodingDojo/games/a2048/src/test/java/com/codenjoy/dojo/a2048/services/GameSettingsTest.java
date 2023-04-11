package com.codenjoy.dojo.a2048.services;

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

import com.codenjoy.dojo.utils.TestUtils;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class GameSettingsTest {

    @Test
    public void shouldGetAllKeys() {
        assertEquals("SIZE                   =[Level] Size\n" +
                    "NEW_NUMBERS            =[Game] New numbers\n" +
                    "NUMBERS_MODE           =[Game] Numbers mode\n" +
                    "BREAKS_MODE            =[Game] Breaks mode\n" +
                    "LEVEL_MAP              =[Level] Level map\n" +
                    "SCORE_COUNTING_TYPE    =[Score] Counting score mode\n" +
                    "NEW_NUMBERS_IN_CORNERS =Classic (corner only) mode\n" +
                    "NEW_NUMBERS_IN_RANDOM  =With random numbers mode\n" +
                    "BREAKS_EXISTS          =With breaks mode\n" +
                    "BREAKS_NOT_EXISTS      =Without breaks mode",
                TestUtils.toString(new GameSettings().allKeys()));
    }
}