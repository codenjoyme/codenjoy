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
import com.codenjoy.dojo.web.rest.pojo.PTeam;
import org.json.JSONArray;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.annotation.Import;

import java.util.Arrays;
import java.util.TreeMap;

import static com.codenjoy.dojo.utils.JsonUtils.toStringSorted;
import static java.util.stream.Collectors.*;
import static org.junit.Assert.assertEquals;

// an issue with the doc that illustrate test cases
// https://github.com/codenjoyme/codenjoy/issues/162
@Import(RestTeamControllerTest.ContextConfiguration.class)
public class RestTeamControllerTest extends AbstractRestControllerTest {

    private static String ip = "ip";
    private static String game = "first";
    private static String room = "test";

    @Before
    public void setUp() {
        super.setUp();
        players.removeAll();
        rooms.removeAll();
        registration.removeAll();

        asAdmin();
    }

    private void givenPl(PTeam... teams) {
        for (PTeam team : teams) {
            for (String playerId : team.getPlayers()) {
                register(playerId, ip, room, game);
            }
        }
        teamService.distributePlayersByTeam(room, Arrays.asList(teams));
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

    private void callGet(PTeam... teams) {
        String expected = new JSONArray(Arrays.asList(teams)).toString();
        String actual = new JSONArray(get("/rest/team/room/test")).toString();
        assertEquals(expected, actual);
    }

    private void callPost(PTeam... teams) {
        post(202, "/rest/team/room/test", toStringSorted(Arrays.asList(teams)));
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