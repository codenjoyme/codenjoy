package com.codenjoy.dojo.minesweeper;

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


import com.codenjoy.dojo.client.local.LocalGameRunner;
import com.codenjoy.dojo.minesweeper.client.Board;
import com.codenjoy.dojo.minesweeper.client.ai.AISolver;
import com.codenjoy.dojo.minesweeper.services.Events;
import com.codenjoy.dojo.minesweeper.services.GameRunner;
import com.codenjoy.dojo.minesweeper.services.GameSettings;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.utils.Smoke;
import org.junit.Test;

import java.util.Arrays;

import static com.codenjoy.dojo.minesweeper.services.GameSettings.Keys.BOARD_SIZE;
import static com.codenjoy.dojo.minesweeper.services.GameSettings.Keys.MINES_ON_BOARD;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class SmokeTest {

    @Test
    public void test() {
        Dice dice = LocalGameRunner.getDice("435874345435874365843564398", 100, 200);

        // about 5 sec
        int ticks = 1000;

        Smoke.play(ticks, "SmokeTest.data",
                new GameRunner() {
                    @Override
                    public Dice getDice() {
                        return dice;
                    }

                    @Override
                    public GameSettings getSettings() {
                        return super.getSettings()
                                .integer(BOARD_SIZE, 15)
                                .integer(MINES_ON_BOARD, 10);
                    }
                },
                Arrays.asList(new AISolver(dice)),
                Arrays.asList(new Board()),
                (o1, o2) -> {
                    if (o2.toString().contains("Fire Event:")) {
                        // мы ни разу не проиграли и всегда правильно отгадывали где мины
                        assertFalse(((String)o2).contains(Events.KILL_ON_MINE.name()));
                        assertFalse(((String)o2).contains(Events.FORGET_CHARGE.name()));
                    }
                    assertEquals(o1, o2);
                });
    }
}
