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
import com.codenjoy.dojo.games.loderunner.Board;
import com.codenjoy.dojo.loderunner.services.GameRunner;
import com.codenjoy.dojo.loderunner.services.GameSettings;
import com.codenjoy.dojo.loderunner.services.ai.AISolver;
import com.codenjoy.dojo.loderunner.services.levels.Big;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.utils.Smoke;
import org.junit.Test;

import java.util.function.Supplier;
import java.util.stream.Stream;

import static com.codenjoy.dojo.loderunner.services.GameSettings.Keys.*;
import static com.codenjoy.dojo.services.round.RoundSettings.Keys.ROUNDS_ENABLED;
import static java.util.stream.Collectors.toList;

public class SmokeTest {

    @Test
    public void testSoft() {
        Dice dice = LocalGameRunner.getDice("435874345435874365843564398", 100, 200);

        // about 2.6 sec
        int ticks = 1000;
        int players = 2;
        Supplier<Solver> solver = () -> new AISolver(dice);

        LocalGameRunner.showPlayers = null;
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
                                        "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼\n" +
                                        "☼~~~~~~~~H   ~~~☼\n" +
                                        "☼        H###   ☼\n" +
                                        "☼   ~~~~~H    ##☼\n" +
                                        "☼H##     H##    ☼\n" +
                                        "☼H       H~~~~~ ☼\n" +
                                        "☼H       H      ☼\n" +
                                        "☼H#####  H      ☼\n" +
                                        "☼H         #####☼\n" +
                                        "☼H  ~~~»        ☼\n" +
                                        "☼H##   ######H H☼\n" +
                                        "☼H~~~        H H☼\n" +
                                        "☼H             H☼\n" +
                                        "☼H   ~~~~~~~~~ H☼\n" +
                                        "☼###H    H     H☼\n" +
                                        "☼   H    H     H☼\n" +
                                        "☼###############☼\n" +
                                        "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼\n")
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
                        .limit(players).collect(toList()));
    }

    @Test
    public void testHard() {
        Dice dice = LocalGameRunner.getDice("435874345435874365843564398", 100, 20000);

        // about 14 sec
        int ticks = 100;
        int players = 10;
        int enemies = 5;
        Supplier<Solver> solver = () -> new AISolver(dice);

        LocalGameRunner.showPlayers = "1";
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
                                .string(LEVEL_MAP, Big.all().get(0))
                                .integer(ENEMIES_COUNT, enemies);
                    }
                },
                Stream.generate(solver)
                        .limit(players).collect(toList()),
                Stream.generate(() -> new Board())
                        .limit(players).collect(toList()));
    }
}