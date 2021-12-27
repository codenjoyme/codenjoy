package com.codenjoy.dojo.puzzlebox;

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


import com.codenjoy.dojo.games.puzzlebox.Board;
import com.codenjoy.dojo.puzzlebox.services.GameRunner;
import com.codenjoy.dojo.puzzlebox.services.GameSettings;
import com.codenjoy.dojo.puzzlebox.services.ai.AISolver;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.utils.Smoke;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static com.codenjoy.dojo.puzzlebox.services.GameSettings.Keys.LEVEL_MAP;

public class SmokeTest {

    private Smoke smoke;
    private Dice dice;

    @Before
    public void setup() {
        smoke = new Smoke();
        dice = smoke.dice();
    }

    @Test
    public void test() {
        // about 1.2 sec
        int ticks = 1000;

        smoke.play(ticks, "SmokeTest.data",
                new GameRunner() {
                    @Override
                    public Dice getDice() {
                        return dice;
                    }

                    @Override
                    public GameSettings getSettings() {
                        return new TestGameSettings()
                                .string(LEVEL_MAP,
                                        "☼☼☼☼☼☼☼☼☼☼☼\n" +
                                        "☼    ☼    ☼\n" +
                                        "☼☼☼       ☼\n" +
                                        "☼   0    ☼☼\n" +
                                        "☼        0☼\n" +
                                        "☼         ☼\n" +
                                        "☼         ☼\n" +
                                        "☼  #      ☼\n" +
                                        "☼         ☼\n" +
                                        "☼#   0  # ☼\n" +
                                        "☼☼☼☼☼☼☼☼☼☼☼\n");
                    }
                },
                Arrays.asList(new AISolver(dice)),
                Arrays.asList(new Board()));
    }
}