package com.codenjoy.dojo.web.rest;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2021 Codenjoy
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
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.utils.TestUtils;
import com.codenjoy.dojo.web.rest.pojo.PTeam;
import org.junit.Test;
import org.springframework.context.annotation.Import;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

@Import(RestTeamControllerApprovalsTest.ContextConfiguration.class)
public class RestTeamControllerApprovalsTest extends AbstractTeamControllerTest {

    private Dice dice = LocalGameRunner.getDice("435874345435874365843564398", 100, 200);
    private List<String> messages = new LinkedList<>();
    private Consumer<String> out = message -> {
        messages.add(message);
        System.out.println(message);
    };

    @Test
    public void test() {
        // given
        int players = 4;
        int teams = 2;
        int playersCount = 10;
        int ticks = 100;
        int teamsCount = 5;

        setupSettings(players, teams);
        registerPlayers(playersCount);

        // when
        for (int tick = 0; tick < ticks; tick++) {
            changeTeam(playersCount, teamsCount);
        }

        // then
        TestUtils.assertSmokeFile(this.getClass().getSimpleName() + ".data", messages);
    }

    private void changeTeam(int playersCount, int teamsCount) {
        int teamId = random(teamsCount);
        int playerId = random(playersCount);
        callPost(new PTeam(teamId, "p" + playerId));
        out.accept(String.format("changeTeam(p%s -> t%s)", playerId, teamId));

        printStatus();
        printSeparator();
    }

    private int random(int max) {
        return dice.next(max) + 1;
    }

    private void printSeparator() {
        out.accept("------------------------------");
    }

    private void printStatus() {
        out.accept("");
        out.accept(getFieldAndTeamInfo());
    }

    private void registerPlayers(int playersCount) {
        for (int index = 0; index < playersCount; index++) {
            int id = index + 1;
            register("p" + id, ip, room, game);
            out.accept(String.format("register(p%s)", id));

            printStatus();
            printSeparator();
        }
    }

    private void setupSettings(int players, int teams) {
        printSeparator();
        settings.playersAndTeamsPerRoom(players, teams);
        out.accept("settings.playersPerRoom = " + players);
        out.accept("settings.teamsPerRoom   = " + teams);
        printSeparator();
    }
}