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
        // System.out.println(message);
    };

    // play(Pc,Tc,PpR,TpR, 100);
    //      Pc/Tc>PpR/TpR     - числа участников в каждой команде должно хватить на укомплектацию борды
    //          Tc >  TpR
    //            PpR%TpR=0   - на поле все команды должны быть равноправны по количеству

    @Test
    public void test1() {
        play(10, 5, 4, 2, 100);
    }

    @Test
    public void test2() {
        play(10, 6, 3, 3, 100);
    }

    @Test
    public void test3() {
        play(20, 10, 2, 2, 100);
    }

    @Test
    public void test4() {
        play(36, 5, 6, 2, 100);
    }

    @Test
    public void test5() {
        play(12, 2, 4, 2, 100);
    }

    @Test
    public void test6() {
        play(18, 4, 3, 3, 100);
    }

    @Test
    public void test7() {
        play(22, 5, 6, 3, 100);
    }

    @Test
    public void test8() {
        play(36, 6, 6, 6, 100);
    }

    @Test
    public void test9() {
        play(15, 3, 3, 3, 100);
    }

    @Test
    public void test10() {
        play(15, 2, 2, 2, 100);
    }

    @Test
    public void test11() {
        play(15, 4, 4, 4, 100);
    }

    @Test
    public void test12() {
        play(35, 7, 10, 5, 100);
    }

    private void play(int playersCount, int teamsCount, int playersPerRoom, int teamsPerRoom, int ticks) {
        // given
        setupSettings(playersCount, teamsCount, playersPerRoom, teamsPerRoom);
        registerPlayers(playersCount);

        // when
        for (int tick = 0; tick < ticks; tick++) {
            changeTeam(playersCount, teamsCount);
        }

        // then
        TestUtils.assertSmokeFile(name(playersCount, teamsCount, playersPerRoom, teamsPerRoom), messages);
    }

    private String name(int playersCount, int teamsCount, int playersPerRoom, int teamsPerRoom) {
        return String.format("%s-players(%s from %s)-teams(%s from %s).data",
                getClass().getSimpleName(),
                playersPerRoom, playersCount,
                teamsPerRoom, teamsCount);
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

    private void setupSettings(int playersCount, int teamsCount, int playersPerRoom, int teamsPerRoom) {
        printSeparator();
        settings.playersAndTeamsPerRoom(playersPerRoom, teamsPerRoom);
        out.accept("settings.playersCount   = " + playersCount);
        out.accept("settings.teamsCount     = " + teamsCount);
        out.accept("settings.playersPerRoom = " + playersPerRoom);
        out.accept("settings.teamsPerRoom   = " + teamsPerRoom);
        printSeparator();
    }
}