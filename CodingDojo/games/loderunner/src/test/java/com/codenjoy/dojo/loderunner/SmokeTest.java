package com.codenjoy.dojo.loderunner;

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


import com.codenjoy.dojo.client.Solver;
import com.codenjoy.dojo.client.local.LocalGameRunner;
import com.codenjoy.dojo.loderunner.client.Board;
import com.codenjoy.dojo.loderunner.client.ai.AISolver;
import com.codenjoy.dojo.loderunner.client.ai.DummyAISolver;
import com.codenjoy.dojo.loderunner.services.GameRunner;
import com.codenjoy.dojo.loderunner.services.GameSettings;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.utils.Smoke;
import org.junit.Test;

import java.util.function.Supplier;
import java.util.stream.Stream;

import static com.codenjoy.dojo.loderunner.services.GameSettings.Keys.*;
import static com.codenjoy.dojo.loderunner.services.GameSettings.Keys.ENEMIES_COUNT;
import static com.codenjoy.dojo.services.round.RoundSettings.Keys.ROUNDS_ENABLED;
import static java.util.stream.Collectors.toList;
import static org.junit.Assert.assertEquals;

public class SmokeTest {

    @Test
    public void testSoft() {
        Dice dice = LocalGameRunner.getDice("435874345435874365843564398", 100, 200);
        LocalGameRunner.showPlayers = null;

        // about 2.6 sec
        int ticks = 1000;
        int players = 2;
        Supplier<Solver> solver = () -> new AISolver(dice);

        Smoke.play(ticks, "SmokeTest.data",
                new GameRunner() {
                    @Override
                    public Dice getDice() {
                        return dice;
                    }

                    @Override
                    public GameSettings getSettings() {
                        return super.getSettings()
                                .bool(ROUNDS_ENABLED, false)
                                .string(LEVEL_MAP,
                                        "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼" +
                                        "☼~~~~~~~~H   ~~~☼" +
                                        "☼        H###   ☼" +
                                        "☼   ~~~~~H    ##☼" +
                                        "☼H##     H##    ☼" +
                                        "☼H       H~~~~~ ☼" +
                                        "☼H       H      ☼" +
                                        "☼H#####  H      ☼" +
                                        "☼H         #####☼" +
                                        "☼H  ~~~»        ☼" +
                                        "☼H##   ######H H☼" +
                                        "☼H~~~        H H☼" +
                                        "☼H             H☼" +
                                        "☼H   ~~~~~~~~~ H☼" +
                                        "☼###H    H     H☼" +
                                        "☼   H    H     H☼" +
                                        "☼###############☼" +
                                        "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼")
                                .integer(SHADOW_PILLS_COUNT, 1)
                                .integer(GOLD_COUNT_GREEN, 5)
                                .integer(GOLD_COUNT_RED, 6)
                                .integer(GOLD_COUNT_YELLOW, 7)
                                .integer(ENEMIES_COUNT, 2);
                    }
                },
                Stream.generate(solver)
                        .limit(players).collect(toList()),
                Stream.generate(() -> new Board())
                        .limit(players).collect(toList()),
                (o1, o2) -> assertEquals(o1, o2));
    }

    @Test
    public void testHard() {
        Dice dice = LocalGameRunner.getDice("435874345435874365843564398", 100, 20000);
        LocalGameRunner.showPlayers = "1";

        // about 15 sec
        int ticks = 100;
        int players = 10;
        int enemies = 5;
        Supplier<Solver> solver = () -> new AISolver(dice);

        Smoke.play(ticks, "SmokeTestHard.data",
                new GameRunner() {
                    @Override
                    public Dice getDice() {
                        return dice;
                    }

                    @Override
                    public GameSettings getSettings() {
                        return super.getSettings()
                                .bool(ROUNDS_ENABLED, false)
                                .integer(ENEMIES_COUNT, enemies);
                    }
                },
                Stream.generate(solver)
                        .limit(players).collect(toList()),
                Stream.generate(() -> new Board())
                        .limit(players).collect(toList()),
                (o1, o2) -> assertEquals(o1, o2));
    }
}