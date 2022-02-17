package com.codenjoy.dojo.expansion.services;

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
        assertEquals("BOARD_SIZE                  =Board size\n" +
                    "SINGLE_TRAINING_MODE        =Single training mode\n" +
                    "WAITING_OTHERS              =Waiting others\n" +
                    "SHUFFLE_PLAYERS_AFTER_LOBBY =Shuffle players after lobby\n" +
                    "WIN_ON_MULTIPLE_SCORE       =Win on multiple score\n" +
                    "DRAW_ON_MULTIPLE_SCORE      =Draw on multiple score\n" +
                    "TICKS_PER_ROUND             =Ticks per round\n" +
                    "LEAVE_FORCES                =Leave forces count\n" +
                    "INITIAL_FORCES              =Initial forces count\n" +
                    "INCREASE_FORCES_PER_TICK    =Increase forces per tick count\n" +
                    "INCREASE_FORCES_GOLD_SCORE  =Increase forces gold score\n" +
                    "REGION_SCORE                =Total count territories is occupied by you increase force score\n" +
                    "DEFENDER_HAS_ADVANTAGE      =Defender has advantage\n" +
                    "DEFENDER_ATTACK_ADVANTAGE   =Defender attack advantage\n" +
                    "COMMAND                     =Command\n" +
                    "GAME_LOGGING_ENABLE         =Game logging enable\n" +
                    "DELAY_REPLAY                =Clean scores to run all replays\n" +
                    "MULTIPLE_LEVEL              =Multiple level\n" +
                    "MULTIPLE_LEVEL_SIZE         =Multiple levels size\n" +
                    "SINGLE_LEVEL                =Single level\n" +
                    "SINGLE_LEVEL_SIZE           =Single levels size",
                TestUtils.toString(new GameSettings().allKeys()));
    }
}