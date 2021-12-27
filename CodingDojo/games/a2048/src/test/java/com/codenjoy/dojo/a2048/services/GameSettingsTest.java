package com.codenjoy.dojo.a2048.services;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2021 Codenjoy
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

import com.codenjoy.dojo.client.Utils;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class GameSettingsTest {

    @Test
    public void shouldGetAllKeys() {
        assertEquals("[SIZE, \n" +
                        "NEW_NUMBERS, \n" +
                        "NUMBERS_MODE, \n" +
                        "BREAKS_MODE, \n" +
                        "LEVEL_MAP, \n" +
                        "SCORE_COUNTING_TYPE, \n" +
                        "NEW_NUMBERS_IN_CORNERS, \n" +
                        "NEW_NUMBERS_IN_RANDOM, \n" +
                        "BREAKS_EXISTS, \n" +
                        "BREAKS_NOT_EXISTS]",
                Utils.split(new GameSettings().allKeys(), ", \n"));
    }

}
