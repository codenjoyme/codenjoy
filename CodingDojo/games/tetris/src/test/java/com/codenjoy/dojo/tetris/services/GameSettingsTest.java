package com.codenjoy.dojo.tetris.services;

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
        assertEquals("GAME_LEVELS               =[Game] Game Levels\n" +
                    "GLASS_SIZE                =[Game] Glass Size\n" +
                    "SCORE_MODE                =[Score] Score mode (cumulative or maximum between overflows)\n" +
                    "FIGURE_DROPPED_SCORE      =[Score] Figure dropped score score\n" +
                    "ONE_LINE_REMOVED_SCORE    =[Score] One line removed score\n" +
                    "TWO_LINES_REMOVED_SCORE   =[Score] Two lines removed score\n" +
                    "THREE_LINES_REMOVED_SCORE =[Score] Three lines removed score\n" +
                    "FOUR_LINES_REMOVED_SCORE  =[Score] Four lines removed score\n" +
                    "GLASS_OVERFLOWN_PENALTY   =[Score] Glass overflown penalty\n" +
                    "SCORE_COUNTING_TYPE       =[Score] Counting score mode",
                TestUtils.toString(new GameSettings().allKeys()));
    }
}