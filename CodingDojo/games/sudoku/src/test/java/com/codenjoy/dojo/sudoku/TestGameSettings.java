package com.codenjoy.dojo.sudoku;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2021 Codenjoy
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

import com.codenjoy.dojo.sudoku.services.GameSettings;

import static com.codenjoy.dojo.sudoku.services.GameSettings.Keys.*;

public class TestGameSettings extends GameSettings {

    /**
     * Here you can override the settings for all tests.
     */
    public TestGameSettings() {
        integer(WIN_SCORE, 10);
        integer(FAIL_PENALTY, -1);
        integer(LOSE_PENALTY, -5);
        integer(SUCCESS_SCORE, 3);
        integer(LEVELS_COUNT, 0);
    }
}
