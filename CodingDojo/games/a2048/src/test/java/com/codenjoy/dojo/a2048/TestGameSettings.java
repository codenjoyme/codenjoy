package com.codenjoy.dojo.a2048;

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

import com.codenjoy.dojo.a2048.services.GameSettings;

import static com.codenjoy.dojo.a2048.services.GameSettings.BreaksMode.BREAKS_NOT_EXISTS;
import static com.codenjoy.dojo.a2048.services.GameSettings.Keys.*;
import static com.codenjoy.dojo.a2048.services.GameSettings.NumbersMode.NEW_NUMBERS_IN_CORNERS;

public class TestGameSettings extends GameSettings {

    /**
     * Here you can override the settings for all tests.
     */
    public TestGameSettings() {
        integer(SIZE, 5);
        mode(NEW_NUMBERS_IN_CORNERS, 4);
        mode(BREAKS_NOT_EXISTS, 5);
    }

    public GameSettings mode(GameSettings.NumbersMode mode, int count) {
        return string(NUMBERS_MODE, mode.key())
                .integer(NEW_NUMBERS, count);
    }

    public void mode(GameSettings.BreaksMode mode, int size) {
        integer(SIZE, size)
                .string(BREAKS_MODE, mode.key());
    }
}