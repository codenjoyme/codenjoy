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

import com.codenjoy.dojo.services.PlayerGame;
import com.codenjoy.dojo.services.PlayerGames;
import com.codenjoy.dojo.services.SaveService;
import com.codenjoy.dojo.services.TeamService;
import com.codenjoy.dojo.web.rest.pojo.PTeam;
import org.json.JSONArray;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private TeamService teamService;
    @Autowired
    private PlayerGames playerGames;
    @Autowired
    private SaveService saveService;

    private static String ip = "ip";
    private static String game = "first";
    private static String room = "test";

    @Before
    public void setUp() {
        super.setUp();
        playerService.removeAll();
        roomService.removeAll();
        registration.removeAll();

        asAdmin();
    }

    private void givenPlayers(PTeam... teams) {
        for (PTeam team : teams) {
            for (String playerId : team.getPlayers()) {
                register(playerId, ip, room, game);
            }
        }
        teamService.distributePlayersByTeam(room, Arrays.asList(teams));
    }

    private void assertTeamPlayers(String expected) {
        String actual = playerGames.all().stream()
                .collect(groupingBy(PlayerGame::getPlayerTeamId, TreeMap::new, toSet()))
                .entrySet().stream()
                .map(e -> {
                    Integer teamId = e.getKey();
                    String players = e.getValue().stream()
                            .map(PlayerGame::getPlayerId)
                            .sorted()
                            .collect(joining(","));
                    return String.format("[%d: %s]", teamId, players);
                })
                .collect(joining());
        assertEquals(expected, actual);
    }

    private void get(PTeam... teams) {
        String expected = new JSONArray(Arrays.asList(teams)).toString();
        String actual = new JSONArray(get("/rest/team/room/test")).toString();
        assertEquals(expected, actual);
    }

    private void post(PTeam... teams) {
        post(202, "/rest/team/room/test", toStringSorted(Arrays.asList(teams)));
    }

    @Test
    public void get_logout_join_post() {
        givenPlayers(new PTeam(1, "player1", "player2"), new PTeam(2, "player3", "player4"));

        get(new PTeam(1, "player1", "player2"), new PTeam(2, "player3", "player4"));
        assertTeamPlayers("[1: player1,player2][2: player3,player4]");

        playerService.remove("player3");
        assertTeamPlayers("[1: player1,player2][2: player4]");

        saveService.load("player3");
        assertTeamPlayers("[1: player1,player2][2: player3,player4]");

        post(new PTeam(3, "player1", "player2"), new PTeam(4, "player3", "player4"));
        assertTeamPlayers("[3: player1,player2][4: player3,player4]");
    }

    @Test
    public void get_logout_post() {
        givenPlayers(new PTeam(1, "player1", "player2"), new PTeam(2, "player3", "player4"));

        get(new PTeam(1, "player1", "player2"), new PTeam(2, "player3", "player4"));
        assertTeamPlayers("[1: player1,player2][2: player3,player4]");

        playerService.remove("player3");
        assertTeamPlayers("[1: player1,player2][2: player4]");

        post(new PTeam(3, "player1", "player2"), new PTeam(4, "player3", "player4"));
        assertTeamPlayers("[3: player1,player2][4: player4]");
    }

    @Test
    public void get_post_logout_join() {
        givenPlayers(new PTeam(1, "player1", "player2"), new PTeam(2, "player3", "player4"));

        get(new PTeam(1, "player1", "player2"), new PTeam(2, "player3", "player4"));
        assertTeamPlayers("[1: player1,player2][2: player3,player4]");

        post(new PTeam(3, "player1", "player2"), new PTeam(4, "player3", "player4"));
        assertTeamPlayers("[3: player1,player2][4: player3,player4]");

        playerService.remove("player3");
        assertTeamPlayers("[3: player1,player2][4: player4]");

        saveService.load("player3");
        assertTeamPlayers("[3: player1,player2][4: player3,player4]");
    }

    @Test
    public void logout_get_post_join() {
        givenPlayers(new PTeam(1, "player1", "player2"), new PTeam(2, "player3", "player4"));

        playerService.remove("player3");
        assertTeamPlayers("[1: player1,player2][2: player4]");

        get(new PTeam(1, "player1", "player2"), new PTeam(2, "player4"));
        assertTeamPlayers("[1: player1,player2][2: player4]");

        post(new PTeam(3, "player1", "player2"), new PTeam(4, "player4"));
        assertTeamPlayers("[3: player1,player2][4: player4]");

        saveService.load("player3");
        assertTeamPlayers("[2: player3][3: player1,player2][4: player4]");
    }

    @Test
    public void get_join_post() {
        givenPlayers(new PTeam(1, "player1", "player2"), new PTeam(2, "player3", "player4"));

        get(new PTeam(1, "player1", "player2"), new PTeam(2, "player3", "player4"));
        assertTeamPlayers("[1: player1,player2][2: player3,player4]");

        register("player5", ip, room, game);
        assertTeamPlayers("[0: player5][1: player1,player2][2: player3,player4]");

        post(new PTeam(3, "player1", "player2"), new PTeam(4, "player3", "player4"));
        assertTeamPlayers("[0: player5][3: player1,player2][4: player3,player4]");
    }

    @Test
    public void get_post_join() {
        givenPlayers(new PTeam(1, "player1", "player2"), new PTeam(2, "player3", "player4"));

        get(new PTeam(1, "player1", "player2"), new PTeam(2, "player3", "player4"));
        assertTeamPlayers("[1: player1,player2][2: player3,player4]");

        post(new PTeam(3, "player1", "player2"), new PTeam(4, "player3", "player4"));
        assertTeamPlayers("[3: player1,player2][4: player3,player4]");

        register("player5", ip, room, game);
        assertTeamPlayers("[0: player5][3: player1,player2][4: player3,player4]");
    }

    @Test
    public void logout_get_join_post() {
        givenPlayers(new PTeam(1, "player1", "player2"), new PTeam(2, "player3", "player4"));

        playerService.remove("player3");
        assertTeamPlayers("[1: player1,player2][2: player4]");

        get(new PTeam(1, "player1", "player2"), new PTeam(2, "player4"));
        assertTeamPlayers("[1: player1,player2][2: player4]");

        saveService.load("player3");
        assertTeamPlayers("[1: player1,player2][2: player3,player4]");

        post(new PTeam(3, "player1", "player2"), new PTeam(4, "player4"));
        assertTeamPlayers("[2: player3][3: player1,player2][4: player4]");
    }
}
