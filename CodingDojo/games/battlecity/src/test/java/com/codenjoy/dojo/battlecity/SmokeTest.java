package com.codenjoy.dojo.battlecity;

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


import com.codenjoy.dojo.battlecity.client.Board;
import com.codenjoy.dojo.battlecity.client.ai.AISolver;
import com.codenjoy.dojo.battlecity.services.GameRunner;
import com.codenjoy.dojo.battlecity.services.GameSettings;
import com.codenjoy.dojo.client.local.LocalGameRunner;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.utils.Smoke;
import org.junit.Test;

import java.util.Arrays;

import static com.codenjoy.dojo.battlecity.services.GameSettings.Keys.LEVEL_MAP;
import static com.codenjoy.dojo.battlecity.services.GameSettings.Keys.SHOW_MY_TANK_UNDER_TREE;
import static org.junit.Assert.assertEquals;

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
                        return new TestGameSettings()
                                .bool(SHOW_MY_TANK_UNDER_TREE, true)
                                .string(LEVEL_MAP,
                                        "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼\n" +
                                        "☼¿ ¿ ¿   ¿ ¿ ¿☼\n" +
                                        "☼ ╬ ╬%╬ ╬%╬ ╬ ☼\n" +
                                        "☼ ╬~╬%╬☼╬%╬~╬ ☼\n" +
                                        "☼#╬~╬%╬ ╬%╬~╬#☼\n" +
                                        "☼#╬~╬%   %╬~╬#☼\n" +
                                        "☼#   %╬ ╬%   #☼\n" +
                                        "☼   ╬%   %╬   ☼\n" +
                                        "☼  # %╬ ╬% #  ☼\n" +
                                        "☼ ╬#╬%╬╬╬%╬#╬ ☼\n" +
                                        "☼ ╬#╬%╬ ╬%╬#╬ ☼\n" +
                                        "☼ ╬         ╬ ☼\n" +
                                        "☼ ╬   ╬╬╬   ╬ ☼\n" +
                                        "☼     ╬ ╬     ☼\n" +
                                        "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼\n");
                    }
                },
                Arrays.asList(new AISolver(dice), new AISolver(dice)),
                Arrays.asList(new Board(), new Board()),
                (o1, o2) -> assertEquals(o1, o2));
    }
}