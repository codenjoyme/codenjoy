package com.codenjoy.dojo.snakebattle;

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
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.games.snakebattle.Board;
import com.codenjoy.dojo.snakebattle.services.ai.AISolver;
import com.codenjoy.dojo.snakebattle.services.GameRunner;
import com.codenjoy.dojo.snakebattle.services.GameSettings;
import com.codenjoy.dojo.utils.Smoke;
import org.junit.Test;

import java.util.function.Supplier;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static org.junit.Assert.assertEquals;

public class SmokeTest {

    @Test
    public void test() {
        Dice dice = LocalGameRunner.getDice("435874345435874365843564398", 1000, 200);

        // about 11 sec
        int players = 5;
        int ticks = 1000;
        Supplier<Solver> solver = () -> new AISolver(dice);

        LocalGameRunner.showPlayers = "1,2";
        Smoke.play(ticks, "SmokeTest.data",
                new GameRunner() {
                    @Override
                    public Dice getDice() {
                        return dice;
                    }

                    @Override
                    public GameSettings getSettings() {
                        GameSettings settings = super.getSettings();
                        settings.setRoundsEnabled(true);
                        return settings;
                    }
                },
                Stream.generate(solver)
                        .limit(players).collect(toList()),
                Stream.generate(() -> new Board())
                        .limit(players).collect(toList()));
    }
}