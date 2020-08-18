package com.codenjoy.dojo.bomberman;

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


import com.codenjoy.dojo.bomberman.client.Board;
import com.codenjoy.dojo.bomberman.client.ai.AIPerksHunterSolver;
import com.codenjoy.dojo.bomberman.client.ai.AISolver;
import com.codenjoy.dojo.bomberman.model.Elements;
import com.codenjoy.dojo.bomberman.model.GameSettings;
import com.codenjoy.dojo.bomberman.model.perks.PerksSettingsWrapper;
import com.codenjoy.dojo.bomberman.services.DefaultGameSettings;
import com.codenjoy.dojo.bomberman.services.GameRunner;
import com.codenjoy.dojo.client.local.LocalGameRunner;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.utils.TestUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

public class SmokeTest {
    @Test
    public void test() throws IOException {
        // given
        List<String> messages = new LinkedList<>();

        LocalGameRunner.timeout = 0;
        LocalGameRunner.out = message -> {
            // System.out.println(message);
            messages.add(message);
        };
        LocalGameRunner.countIterations = 1000;
        LocalGameRunner.printConversions = false;
        LocalGameRunner.printBoardOnly = true;
        LocalGameRunner.printDice = false;
        LocalGameRunner.printTick = true;

        String soul = RandomStringUtils.randomNumeric(30);
        soul = "435874345435874365843564398";
        Dice dice = LocalGameRunner.getDice(LocalGameRunner.generateXorShift(soul, 100, 200));

        DefaultGameSettings.BOARD_SIZE = 11;
        DefaultGameSettings.BOMB_POWER = 3;
        DefaultGameSettings.BOMBS_COUNT = 1;
        DefaultGameSettings.DESTROY_WALL_COUNT = 14;
        DefaultGameSettings.MEAT_CHOPPERS_COUNT = 3;
        PerksSettingsWrapper.setDropRatio(20);
        PerksSettingsWrapper.setPickTimeout(5);
        PerksSettingsWrapper.setPerkSettings(Elements.BOMB_BLAST_RADIUS_INCREASE, 5, 10);
        PerksSettingsWrapper.setPerkSettings(Elements.BOMB_REMOTE_CONTROL, 5, 10);
        PerksSettingsWrapper.setPerkSettings(Elements.BOMB_IMMUNE, 5, 10);
        PerksSettingsWrapper.setPerkSettings(Elements.BOMB_COUNT_INCREASE, 5, 3);

        GameRunner gameType = new GameRunner() {
            @Override
            public Dice getDice() {
                return dice;
            }

            @Override
            protected GameSettings getGameSettings() {
                return new DefaultGameSettings(dice);
            }
        };

        // when
        LocalGameRunner.run(gameType,
                Arrays.asList(new AISolver(dice), new AIPerksHunterSolver(dice)),
                Arrays.asList(new Board(), new Board()));

        // then
        String expectedAll = load("src/test/resources/SmokeTest.data");
        String actualAll = String.join("\n", messages);

        TestUtils.assertSmoke(false,
                (o1, o2) -> assertEquals(o1, o2),
                expectedAll, actualAll);
    }

    private String load(String file) throws IOException {
        return Files.lines(new File(file).toPath())
                        .collect(Collectors.joining("\n"));
    }
}
