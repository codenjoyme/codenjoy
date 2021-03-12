package com.codenjoy.dojo.bomberman;

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


import com.codenjoy.dojo.bomberman.services.GameSettings;
import org.apache.commons.lang3.StringUtils;

import static com.codenjoy.dojo.bomberman.services.GameSettings.Keys.*;
import static com.codenjoy.dojo.services.round.RoundSettings.Keys.ROUNDS_ENABLED;

public class TestGameSettings extends GameSettings {

    public TestGameSettings() {
        bool(ROUNDS_ENABLED, false);

        integer(WIN_ROUND_SCORE, 1000);
        integer(DIE_PENALTY, 50);

        integer(KILL_OTHER_HERO_SCORE, 200);
        integer(KILL_MEAT_CHOPPER_SCORE, 100);
        integer(KILL_WALL_SCORE, 10);
        integer(CATCH_PERK_SCORE, 5);

        integer(MEAT_CHOPPERS_COUNT, 10);
        integer(BOMB_POWER, 3);
        integer(BOMBS_COUNT, 1);

        int boardSize = 33;
        integer(BOARD_SIZE, boardSize);
        integer(DESTROY_WALL_COUNT, boardSize * boardSize / 10);

        bool(BIG_BADABOOM, false);
        string(DEFAULT_PERKS, StringUtils.EMPTY);
    }
}
