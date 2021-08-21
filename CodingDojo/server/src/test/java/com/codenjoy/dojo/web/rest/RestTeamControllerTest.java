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

import com.codenjoy.dojo.services.Deal;
import com.codenjoy.dojo.services.GameServiceImpl;
import com.codenjoy.dojo.services.GameType;
import com.codenjoy.dojo.services.mocks.FirstGameType;
import com.codenjoy.dojo.services.mocks.SecondGameType;
import com.codenjoy.dojo.services.mocks.ThirdGameSettings;
import com.codenjoy.dojo.services.mocks.ThirdGameType;
import com.codenjoy.dojo.services.multiplayer.GameRoom;
import com.codenjoy.dojo.web.rest.pojo.PTeam;
import org.json.JSONArray;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import java.util.*;

import static com.codenjoy.dojo.services.round.RoundSettings.Keys.ROUNDS_PLAYERS_PER_ROOM;
import static com.codenjoy.dojo.services.round.RoundSettings.Keys.ROUNDS_TEAMS_PER_ROOM;
import static com.codenjoy.dojo.stuff.SmartAssert.assertEquals;
import static com.codenjoy.dojo.utils.JsonUtils.toStringSorted;
import static java.util.stream.Collectors.*;

// an issue with the doc that illustrate some of test cases
// with name like "get_logout_join_post"
// https://github.com/codenjoyme/codenjoy/issues/162
@Import(RestTeamControllerTest.ContextConfiguration.class)
public class RestTeamControllerTest extends AbstractRestControllerTest {

    private static final String game = "third";
    private static final String ip = "ip";
    private static final String room = "test";

    private ThirdGameSettings settings;
    private ThirdGameType type;

    @TestConfiguration
    public static class ContextConfiguration {
        @Bean("gameService")
        public GameServiceImpl gameService() {
            return new GameServiceImpl(){
                @Override
                public Collection<? extends Class<? extends GameType>> findInPackage(String packageName) {
                    return Arrays.asList(
                            FirstGameType.class,
                            SecondGameType.class,
                            ThirdGameType.class);
                }
            };
        }
    }

    @Before
    public void setUp() {
        super.setUp();

        players.removeAll();
        rooms.removeAll();
        registration.removeAll();

        // when changing teams, the current state is
        // saved to the database (there is a TeamId).
        // It should also be deleted between test runs.
        saves.removeAllSaves();

        // this is how we get the room settings
        type = (ThirdGameType) games.getGameType(game);
        settings = (ThirdGameSettings) rooms.create(room, type).getSettings();

        // We want to remember every field ever created,
        // in order to count the indices. So there we want to delete it.
        type.clear();

        asAdmin();

        // for all tests
        settings.playersAndTeamsPerRoom(4, 2);
    }

    private void givenPl(PTeam... teams) {
        for (PTeam team : teams) {
            for (String playerId : team.getPlayers()) {
                register(playerId, ip, room, game);
            }
        }
        teamService.distributePlayersByTeam(room, Arrays.asList(teams));
        removeDeadFields();
    }

    /**
     * Reset uninformative indexes that
     * could have been created during the
     * registration process, changing commands.
     */
    private void removeDeadFields() {
        type.fields().clear();
        type.fields().addAll((List)deals.rooms().values().stream()
                .map(room -> room.field())
                .distinct()
                .collect(toList()));
    }

    private void asrtTms(String expected) {
        String actual = deals.all().stream()
                .collect(groupingBy(Deal::getTeamId, TreeMap::new, toSet()))
                .entrySet().stream()
                .map(entry -> {
                    Integer teamId = entry.getKey();
                    String players = entry.getValue().stream()
                            .map(Deal::getPlayerId)
                            .sorted()
                            .collect(joining(","));
                    return String.format("[%d: %s]\n", teamId, players);
                })
                .collect(joining());
        assertEquals(expected, actual);
    }

    private void asrtFld(String expected) {
        Collection<GameRoom> rooms = deals.rooms().get(room);

        Map<String, Integer> playersTeams = deals.all().stream().
                collect(toMap(deal -> deal.getPlayerId(),
                        deal -> deal.getTeamId()));

        String actual = type.fields().stream()
                .map(field -> {
                    String players = rooms.stream()
                            .filter(room -> room.field() == field)
                            .flatMap(room -> room.players().stream())
                            .map(gamePlayer -> deals.get(gamePlayer).get().getPlayerId())
                            .map(id -> String.format("%s(t%s)", id, playersTeams.get(id)))
                            .collect(joining(", "));
                    return String.format("[f%s: %s]", type.fields().indexOf(field), players);
                })
                .collect(joining("\n"));

        assertEquals(expected, actual);
    }

    private void callGet(PTeam... teams) {
        String expected = new JSONArray(Arrays.asList(teams)).toString();
        String actual = new JSONArray(get("/rest/team/room/" + room)).toString();
        assertEquals(expected, actual);
    }

    private void callPost(PTeam... teams) {
        post(202, "/rest/team/room/" + room,
                toStringSorted(Arrays.asList(teams)));
    }

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

        asrtFld("[f0: player1(t0),player4(t1)]\n" +
                "[f1: player2(t0),player3(t1)]\n" +
                "[f2: ]\n" +
                "[f3: ]");

        // when then
        // 2 уйдет в новую комнату и потащит за собой 3
        // 3 перейдет в новую комнату, т.к. вернуться к 2 не может потому что они одной команды
        callPost(new PTeam(1, "player2"));

        asrtFld("[f0: player1(t0),player4(t1)]\n" +
                "[f1: ]\n" +
                "[f2: ]\n" +
                "[f3: ]\n" +
                "[f4: player3(t1)]\n" +
                "[f5: player2(t1)]");
    }

    @Test
    public void whenTeamOfOnePlayerChanges_itIsNecessaryToResetAllOtherPlayersOnTheField() {
        givenPl(new PTeam(1, "player1", "player2", "player3", "player4"),
                new PTeam(2, "player5", "player6", "player7", "player8"));

        asrtFld("[f0: player5(t2),player1(t1),player2(t1),player6(t2)]\n" +
                "[f1: player7(t2),player3(t1),player4(t1),player8(t2)]");

        // when
        callPost(new PTeam(2, "player4"));

        // then
        // тут явно видно, что все вышли из 1й комнаты
        asrtFld("[f0: player5(t2),player1(t1),player2(t1),player6(t2)]\n" +
                "[f1: ]\n" +
                "[f2: player7(t2),player3(t1),player8(t2)]\n" +
                "[f3: player4(t2)]");
    }

    @Test
    public void get_logout_join_post() {
        givenPl(new PTeam(1, "player1", "player2"),
                new PTeam(2, "player3", "player4"));

        callGet(new PTeam(1, "player1", "player2"),
                new PTeam(2, "player3", "player4"));

        asrtTms("[1: player1,player2]\n" +
                "[2: player3,player4]\n");

        players.remove("player3");

        asrtTms("[1: player1,player2]\n" +
                "[2: player4]\n");

        saves.load("player3");

        asrtTms("[1: player1,player2]\n" +
                "[2: player3,player4]\n");

        callPost(new PTeam(3, "player1", "player2"),
                new PTeam(4, "player3", "player4"));

        asrtTms("[3: player1,player2]\n" +
                "[4: player3,player4]\n");
    }

    @Test
    public void get_logout_post() {
        givenPl(new PTeam(1, "player1", "player2"),
                new PTeam(2, "player3", "player4"));

        callGet(new PTeam(1, "player1", "player2"),
                new PTeam(2, "player3", "player4"));

        asrtTms("[1: player1,player2]\n" +
                "[2: player3,player4]\n");

        players.remove("player3");

        asrtTms("[1: player1,player2]\n" +
                "[2: player4]\n");

        callPost(new PTeam(3, "player1", "player2"),
                new PTeam(4, "player3", "player4"));

        asrtTms("[3: player1,player2]\n" +
                "[4: player4]\n");
    }

    @Test
    public void get_post_logout_join() {
        givenPl(new PTeam(1, "player1", "player2"),
                new PTeam(2, "player3", "player4"));

        callGet(new PTeam(1, "player1", "player2"),
                new PTeam(2, "player3", "player4"));

        asrtTms("[1: player1,player2]\n" +
                "[2: player3,player4]\n");

        callPost(new PTeam(3, "player1", "player2"),
                new PTeam(4, "player3", "player4"));

        asrtTms("[3: player1,player2]\n" +
                "[4: player3,player4]\n");

        players.remove("player3");

        asrtTms("[3: player1,player2]\n" +
                "[4: player4]\n");

        saves.load("player3");

        asrtTms("[3: player1,player2]\n" +
                "[4: player3,player4]\n");
    }

    @Test
    public void logout_get_post_join() {
        givenPl(new PTeam(1, "player1", "player2"),
                new PTeam(2, "player3", "player4"));

        players.remove("player3");

        asrtTms("[1: player1,player2]\n" +
                "[2: player4]\n");

        callGet(new PTeam(1, "player1", "player2"),
                new PTeam(2, "player4"));

        asrtTms("[1: player1,player2]\n" +
                "[2: player4]\n");

        callPost(new PTeam(3, "player1", "player2"),
                new PTeam(4, "player4"));

        asrtTms("[3: player1,player2]\n" +
                "[4: player4]\n");

        saves.load("player3");

        asrtTms("[2: player3]\n" +
                "[3: player1,player2]\n" +
                "[4: player4]\n");
    }

    @Test
    public void get_join_post() {
        givenPl(new PTeam(1, "player1", "player2"),
                new PTeam(2, "player3", "player4"));

        callGet(new PTeam(1, "player1", "player2"),
                new PTeam(2, "player3", "player4"));

        asrtTms("[1: player1,player2]\n" +
                "[2: player3,player4]\n");

        register("player5", ip, room, game);

        asrtTms("[0: player5]\n" +
                "[1: player1,player2]\n" +
                "[2: player3,player4]\n");

        callPost(new PTeam(3, "player1", "player2"),
                new PTeam(4, "player3", "player4"));

        asrtTms("[0: player5]\n" +
                "[3: player1,player2]\n" +
                "[4: player3,player4]\n");
    }

    @Test
    public void get_post_join() {
        givenPl(new PTeam(1, "player1", "player2"),
                new PTeam(2, "player3", "player4"));

        callGet(new PTeam(1, "player1", "player2"),
                new PTeam(2, "player3", "player4"));

        asrtTms("[1: player1,player2]\n" +
                "[2: player3,player4]\n");

        callPost(new PTeam(3, "player1", "player2"),
                new PTeam(4, "player3", "player4"));

        asrtTms("[3: player1,player2]\n" +
                "[4: player3,player4]\n");

        register("player5", ip, room, game);

        asrtTms("[0: player5]\n" +
                "[3: player1,player2]\n" +
                "[4: player3,player4]\n");
    }

    @Test
    public void logout_get_join_post() {
        givenPl(new PTeam(1, "player1", "player2"),
                new PTeam(2, "player3", "player4"));

        players.remove("player3");

        asrtTms("[1: player1,player2]\n" +
                "[2: player4]\n");

        callGet(new PTeam(1, "player1", "player2"),
                new PTeam(2, "player4"));

        asrtTms("[1: player1,player2]\n" +
                "[2: player4]\n");

        saves.load("player3");

        asrtTms("[1: player1,player2]\n" +
                "[2: player3,player4]\n");

        callPost(new PTeam(3, "player1", "player2"),
                new PTeam(4, "player4"));

        asrtTms("[2: player3]\n" +
                "[3: player1,player2]\n" +
                "[4: player4]\n");
    }
}