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

import com.codenjoy.dojo.web.rest.pojo.PTeam;
import org.junit.Test;
import org.springframework.context.annotation.Import;

// an issue with the doc that illustrate some of test cases
// with name like "get_logout_join_post"
// https://github.com/codenjoyme/codenjoy/issues/162
@Import(RestTeamControllerTest.ContextConfiguration.class)
public class RestTeamControllerTest extends AbstractTeamControllerTest {

    @Test
    public void checkingFieldCreationProcess_twoTeams_twoPlayersPerRoom() {
        settings.playersAndTeamsPerRoom(2, 2);

        // when then
        register("player1", ip, room, game);

        asrtFld("[f0: player1(t0)]");

        // when then
        register("player2", ip, room, game);

        asrtFld("[f0: player1(t0)]\n" +
                "[f1: player2(t0)]");

        // when then
        register("player3", ip, room, game);

        asrtFld("[f0: player1(t0)]\n" +
                "[f1: player2(t0)]\n" +
                "[f2: player3(t0)]");

        // when then
        register("player4", ip, room, game);

        asrtFld("[f0: player1(t0)]\n" +
                "[f1: player2(t0)]\n" +
                "[f2: player3(t0)]\n" +
                "[f3: player4(t0)]");

        // when then
        // 4 уйдет к 1 (там свободно) и его комната пустая самоудалится
        callPost(new PTeam(1, "player4"));

        asrtFld("[f0: player1(t0), player4(t1)]\n" +
                "[f1: player2(t0)]\n" +
                "[f2: player3(t0)]\n" +
                "[f3: ]");

        // when then
        // 3 уйдет ко 2 (там свободно) и его комната пустая самоудалится
        callPost(new PTeam(1, "player3"));

        asrtFld("[f0: player1(t0), player4(t1)]\n" +
                "[f1: player2(t0), player3(t1)]\n" +
                "[f2: ]\n" +
                "[f3: ]");

        // when then
        // 2 уйдет в новую комнату и потащит за собой 3
        // 3 перейдет в новую комнату, т.к. вернуться к 2 не может потому что они одной команды
        callPost(new PTeam(1, "player2"));

        asrtFld("[f0: player1(t0), player4(t1)]\n" +
                "[f1: ]\n" +
                "[f2: ]\n" +
                "[f3: ]\n" +
                "[f4: player3(t1)]\n" +
                "[f5: player2(t1)]");

        // when then
        // 1 уйдет так же в свою отдельную комнату, потому что два других
        // ребят 2 и 3 ждут кого-то из другой команды.
        // А вот 4й сам не останется в пустой комнате
        // и проследует в свою комнату. Причем вначале обрабатываются
        // все кто был в комнате, а потом тот кому меняем команду
        callPost(new PTeam(1, "player1"));

        asrtFld("[f3: ]\n" +
                "[f4: player3(t1)]\n" +
                "[f5: player2(t1)]\n" +
                "[f6: player4(t1)]\n" +
                "[f7: player1(t1)]");
    }

    @Test
    public void checkingFieldCreationProcess_caseTwoPlayersPerRequest_twoTeams_twoPlayersPerRoom() {
        settings.playersAndTeamsPerRoom(2, 2);

        // when then
        register("player1", ip, room, game);
        register("player2", ip, room, game);
        register("player3", ip, room, game);
        register("player4", ip, room, game);

        asrtFld("[f0: player1(t0)]\n" +
                "[f1: player2(t0)]\n" +
                "[f2: player3(t0)]\n" +
                "[f3: player4(t0)]");

        // when then
        //
        // 4 уйдет к 1 (там свободно) и его комната пустая самоудалится
        //
        // 3 уйдет ко 2 (там свободно) и его комната пустая самоудалится
        callPost(new PTeam(1, "player4"),
                new PTeam(1, "player3"));

        asrtFld("[f0: player1(t0), player4(t1)]\n" +
                "[f1: player2(t0), player3(t1)]\n" +
                "[f2: ]\n" +
                "[f3: ]");

        // when then
        //
        // 2 уйдет в новую комнату и потащит за собой 3
        // 3 перейдет в новую комнату, т.к. вернуться к 2 не может потому что они одной команды
        //
        // 1 уйдет так же в свою отдельную комнату, потому что два других
        // ребят 2 и 3 ждут кого-то из другой команды.
        // А вот 4й сам не останется в пустой комнате
        // и проследует в свою комнату. Причем вначале обрабатываются
        // все кто был в комнате, а потом тот кому меняем команду
        callPost(new PTeam(1, "player2"),
                new PTeam(1, "player1"));

        asrtFld("[f3: ]\n" +
                "[f4: player3(t1)]\n" +
                "[f5: player2(t1)]\n" +
                "[f6: player4(t1)]\n" +
                "[f7: player1(t1)]");

        // результат такой же если бы сделали запросы по очереди
    }

    @Test
    public void whenTeamOfOnePlayerChanges_itIsNecessaryToResetAllOtherPlayersOnTheField() {
        givenPl(new PTeam(1, "player1", "player2", "player3", "player4"),
                new PTeam(2, "player5", "player6", "player7", "player8"));

        asrtFld("[f0: player5(t2), player1(t1), player2(t1), player6(t2)]\n" +
                "[f1: player7(t2), player3(t1), player4(t1), player8(t2)]");

        // when
        callPost(new PTeam(2, "player4"));

        // then
        // тут явно видно, что все вышли из 1й комнаты
        asrtFld("[f0: player5(t2), player1(t1), player2(t1), player6(t2)]\n" +
                "[f1: ]\n" +
                "[f2: player7(t2), player3(t1), player8(t2)]\n" +
                "[f3: player4(t2)]");
    }

    @Test
    public void get_logout_join_post() {
        givenPl(new PTeam(1, "player1", "player2"),
                new PTeam(2, "player3", "player4"));

        callGet(new PTeam(1, "player1", "player2"),
                new PTeam(2, "player3", "player4"));

        asrtTms("[1: player1, player2]\n" +
                "[2: player3, player4]");

        players.remove("player3");

        asrtTms("[1: player1, player2]\n" +
                "[2: player4]");

        saves.load("player3");

        asrtTms("[1: player1, player2]\n" +
                "[2: player3, player4]");

        callPost(new PTeam(3, "player1", "player2"),
                new PTeam(4, "player3", "player4"));

        asrtTms("[3: player1, player2]\n" +
                "[4: player3, player4]");
    }

    @Test
    public void get_logout_post() {
        givenPl(new PTeam(1, "player1", "player2"),
                new PTeam(2, "player3", "player4"));

        callGet(new PTeam(1, "player1", "player2"),
                new PTeam(2, "player3", "player4"));

        asrtTms("[1: player1, player2]\n" +
                "[2: player3, player4]");

        players.remove("player3");

        asrtTms("[1: player1, player2]\n" +
                "[2: player4]");

        callPost(new PTeam(3, "player1", "player2"),
                new PTeam(4, "player3", "player4"));

        asrtTms("[3: player1, player2]\n" +
                "[4: player4]");
    }

    @Test
    public void get_post_logout_join() {
        givenPl(new PTeam(1, "player1", "player2"),
                new PTeam(2, "player3", "player4"));

        callGet(new PTeam(1, "player1", "player2"),
                new PTeam(2, "player3", "player4"));

        asrtTms("[1: player1, player2]\n" +
                "[2: player3, player4]");

        callPost(new PTeam(3, "player1", "player2"),
                new PTeam(4, "player3", "player4"));

        asrtTms("[3: player1, player2]\n" +
                "[4: player3, player4]");

        players.remove("player3");

        asrtTms("[3: player1, player2]\n" +
                "[4: player4]");

        saves.load("player3");

        asrtTms("[3: player1, player2]\n" +
                "[4: player3, player4]");
    }

    @Test
    public void logout_get_post_join() {
        givenPl(new PTeam(1, "player1", "player2"),
                new PTeam(2, "player3", "player4"));

        players.remove("player3");

        asrtTms("[1: player1, player2]\n" +
                "[2: player4]");

        callGet(new PTeam(1, "player1", "player2"),
                new PTeam(2, "player4"));

        asrtTms("[1: player1, player2]\n" +
                "[2: player4]");

        callPost(new PTeam(3, "player1", "player2"),
                new PTeam(4, "player4"));

        asrtTms("[3: player1, player2]\n" +
                "[4: player4]");

        saves.load("player3");

        asrtTms("[2: player3]\n" +
                "[3: player1, player2]\n" +
                "[4: player4]");
    }

    @Test
    public void get_join_post() {
        givenPl(new PTeam(1, "player1", "player2"),
                new PTeam(2, "player3", "player4"));

        callGet(new PTeam(1, "player1", "player2"),
                new PTeam(2, "player3", "player4"));

        asrtTms("[1: player1, player2]\n" +
                "[2: player3, player4]");

        register("player5", ip, room, game);

        asrtTms("[0: player5]\n" +
                "[1: player1, player2]\n" +
                "[2: player3, player4]");

        callPost(new PTeam(3, "player1", "player2"),
                new PTeam(4, "player3", "player4"));

        asrtTms("[0: player5]\n" +
                "[3: player1, player2]\n" +
                "[4: player3, player4]");
    }

    @Test
    public void get_post_join() {
        givenPl(new PTeam(1, "player1", "player2"),
                new PTeam(2, "player3", "player4"));

        callGet(new PTeam(1, "player1", "player2"),
                new PTeam(2, "player3", "player4"));

        asrtTms("[1: player1, player2]\n" +
                "[2: player3, player4]");

        callPost(new PTeam(3, "player1", "player2"),
                new PTeam(4, "player3", "player4"));

        asrtTms("[3: player1, player2]\n" +
                "[4: player3, player4]");

        register("player5", ip, room, game);

        asrtTms("[0: player5]\n" +
                "[3: player1, player2]\n" +
                "[4: player3, player4]");
    }

    @Test
    public void logout_get_join_post() {
        givenPl(new PTeam(1, "player1", "player2"),
                new PTeam(2, "player3", "player4"));

        players.remove("player3");

        asrtTms("[1: player1, player2]\n" +
                "[2: player4]");

        callGet(new PTeam(1, "player1", "player2"),
                new PTeam(2, "player4"));

        asrtTms("[1: player1, player2]\n" +
                "[2: player4]");

        saves.load("player3");

        asrtTms("[1: player1, player2]\n" +
                "[2: player3, player4]");

        callPost(new PTeam(3, "player1", "player2"),
                new PTeam(4, "player4"));

        asrtTms("[2: player3]\n" +
                "[3: player1, player2]\n" +
                "[4: player4]");
    }
}