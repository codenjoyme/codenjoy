package com.codenjoy.dojo.spacerace;

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


import com.codenjoy.dojo.games.spacerace.Board;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.spacerace.services.GameRunner;
import com.codenjoy.dojo.spacerace.services.GameSettings;
import com.codenjoy.dojo.spacerace.services.ai.AI2Solver;
import com.codenjoy.dojo.spacerace.services.ai.AI3Solver;
import com.codenjoy.dojo.spacerace.services.ai.AISolver;
import com.codenjoy.dojo.utils.Smoke;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static com.codenjoy.dojo.spacerace.services.GameSettings.Keys.LEVEL_MAP;

public class SmokeTest {

    private Smoke smoke;
    private Dice dice;

    @Before
    public void setup() {
        smoke = new Smoke();
        dice = smoke.dice(1000, 200);
    }

    @Test
    public void test() {
        // about 2.5 sec
        int ticks = 1000;

        smoke.settings().showPlayers("1,2");

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
                                        "☼                  \n☼" +
                                        "☼                  \n☼" +
                                        "☼                  \n☼" +
                                        "☼                  \n☼" +
                                        "☼                  \n☼" +
                                        "☼                  \n☼" +
                                        "☼                  \n☼" +
                                        "☼                  \n☼" +
                                        "☼                  \n☼" +
                                        "☼                  \n☼" +
                                        "☼                  \n☼" +
                                        "☼                  \n☼" +
                                        "☼                  \n☼" +
                                        "☼                  \n☼" +
                                        "☼                  \n☼" +
                                        "☼                  \n☼" +
                                        "☼                  \n☼" +
                                        "☼                  \n☼" +
                                        "☼                  \n☼" +
                                        "☼                  \n☼");
                    }
                },
                Arrays.asList(new AISolver(dice), new AI2Solver(dice), new AI3Solver(dice)),
                Arrays.asList(new Board(), new Board(), new Board()));
    }
}