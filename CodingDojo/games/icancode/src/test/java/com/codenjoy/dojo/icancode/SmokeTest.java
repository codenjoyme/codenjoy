package com.codenjoy.dojo.icancode;

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
import com.codenjoy.dojo.icancode.client.Board;
import com.codenjoy.dojo.icancode.client.ai.AISolver;
import com.codenjoy.dojo.icancode.model.Level;
import com.codenjoy.dojo.icancode.model.items.Zombie;
import com.codenjoy.dojo.icancode.model.items.ZombiePot;
import com.codenjoy.dojo.icancode.services.GameRunner;
import com.codenjoy.dojo.icancode.services.GameSettings;
import com.codenjoy.dojo.icancode.services.Levels;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.utils.Smoke;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static com.codenjoy.dojo.icancode.services.GameSettings.Keys.*;
import static org.junit.Assert.assertEquals;

public class SmokeTest {

    @Test
    public void test() {
        Dice dice = LocalGameRunner.getDice("435874345435874365843564398", 100, 200);

        // about 9s
        int ticks = 1000;
        Solver ai = getDummySolver(dice);

        LocalGameRunner.showPlayers = "3";
        LocalGameRunner.printScores = true;
        boolean printBoardOnly = false;
        Smoke.play(ticks, "SmokeTest.data", printBoardOnly,
                new GameRunner() {
                    @Override
                    public Dice getDice() {
                        return dice;
                    }

                    @Override
                    public GameSettings getSettings() {
                        return super.getSettings()
                                .bool(CHEATS, true)
                                .integer(PERK_ACTIVITY, 10)
                                .integer(PERK_AVAILABILITY, 10)
                                .integer(PERK_DROP_RATIO, 100)
                                .integer(DEATH_RAY_PERK_RANGE, 10)
                                .integer(GUN_RECHARGE, 2)
                                .integer(GUN_REST_TIME, 4)
                                .integer(GUN_SHOT_QUEUE, 2)
                                .integer(TICKS_PER_NEW_ZOMBIE, 5)
                                .string(DEFAULT_PERKS, "ajm,ajm");
                    }

                    @Override
                    public Level loadLevel(int level, GameSettings settings) {
                        return Levels.load(
                                "                \n" +
                                " ############## \n" +
                                " #Sl$.OB..O.˅.# \n" +
                                " #˃.....$O....# \n" +
                                " #.B$.####.B.S# \n" +
                                " #B..Z#  #Z...# \n" +
                                " #.O###  ###BO# \n" +
                                " #.$#      #.$# \n" +
                                " #$.#      #$B# \n" +
                                " #O.###  ###O.# \n" +
                                " #...Z#  #Z.fS# \n" +
                                " #BB.O####.B..# \n" +
                                " #...BO$..B..˂# \n" +
                                " #.˄.$Sr.O.B.E# \n" +
                                " ############## \n" +
                                "                \n",
                                settings);
                    }
                },
                Arrays.asList(new AISolver(dice), new AISolver(dice), ai, ai, ai),
                Arrays.asList(new Board(), new Board(), new Board(), new Board(), new Board()));
    }

    public Solver getDummySolver(Dice dice) {
        int[] index = {0};
        List<String> commands = Arrays.asList(
                "ACT(-1)",

                "ACT(0)",

                "ACT(1)",
                "ACT(1),DOWN",
                "ACT(1),UP",
                "ACT(1),LEFT",
                "ACT(1),RIGHT",

                "ACT(2)",
                "ACT(2),DOWN",
                "ACT(2),UP",
                "ACT(2),LEFT",
                "ACT(2),RIGHT",

                "ACT(3)",
                "ACT(3),DOWN",
                "ACT(3),UP",
                "ACT(3),LEFT",
                "ACT(3),RIGHT",

                "DOWN",
                "UP",
                "LEFT",
                "RIGHT"
        );
        return board -> commands.get((++index[0])*dice.next(100) % commands.size());
    }
}
